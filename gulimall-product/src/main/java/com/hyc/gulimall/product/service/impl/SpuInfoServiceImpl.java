package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.to.SkuReductionTo;
import com.hyc.common.to.SpuBoundTo;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.common.utils.R;
import com.hyc.gulimall.product.dao.SpuInfoDao;
import com.hyc.gulimall.product.entity.*;
import com.hyc.gulimall.product.feign.CouponFeignService;
import com.hyc.gulimall.product.service.*;
import com.hyc.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 保存商品信息
     * 涉及到多个表同时操作 所以添加事务，
     * 涉及到不同数据库的操作 分布式事务
     * @date: 2022/7/28 17:16
     * @param spuVo
     * @return: void
     * //todo 高级篇 处理事务回滚问题以及失败问题
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuVo) {
        //1. 保存spu信息 操作表：pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuVo, spuInfoEntity);
        this.saveBaseSpuInfo(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        //2. 保存spu描述图片 pms_spu_info_desc
        List<String> decript = spuVo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        //使用jion来拼接
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfDesc(descEntity);
        //3. 保存spu的图片集 pms_spu_images
        List<String> images = spuVo.getImages();
        spuImagesService.saveImages(spuId, images);
        //4. 保存spu的规格参数 操作表：pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveProductAttr(collect);
        //PS：保存spu的积分信息 sms_spu_bounds
        Bounds bounds = spuVo.getBounds();
        //用于远程调用的 to 对象
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuId);
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }

        //5. 保存当前spu对应的sku的信息：
        // 5.1.sku 基本信息 pms_sku_info
        List<Skus> skus = spuVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                //图片处理，默认图片vo属性又代表默认的字段可以判断，
                String defaultimage = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultimage = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setSpuId(spuId);
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatelogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSkuDefaultImg(defaultimage);
                skuInfoService.saveSkuInfo(skuInfoEntity);

                // 5.2 sku 对应图片信息 pms_sku_images
                //首先将skuimages从vo取出到对应的 实体类中
                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imgList = item.getImages().stream().map(img -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    imagesEntity.setId(0L);
                    imagesEntity.setSkuId(skuId);
                    imagesEntity.setImgUrl(img.getImgUrl());
                    imagesEntity.setDefaultImg(img.getDefaultImg());
                    return imagesEntity;
                    //  判断非空
                }).filter(entity -> {
                    //返回true 需要 返回
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //保存操作
                skuImagesService.saveBatch(imgList);
                // 5.3 sku的销售属性信息 pms_sku_sale_attr_value
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = item.getAttr().stream().map(attr -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(saleAttrValueEntities);
                //TODO 没有图片不需要保存

                // 5.4 sku的优惠 满减等信息
                // sms:
                // 1.(打折表 满几件打几折) sms_sku_ladder
                // 2.(满减优惠表) sms_sku_full_reduction
                // 3.(会员价格表)sms_member_price
                // 4.购买积分问题 sms_spu_bounds 可以是sku固定 也可以是spu每一件都不一样
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                //判断是否需要执行远程保存优惠信息 PS  getFullPrice BigDecimal 需要用compareto 来比较
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }


}