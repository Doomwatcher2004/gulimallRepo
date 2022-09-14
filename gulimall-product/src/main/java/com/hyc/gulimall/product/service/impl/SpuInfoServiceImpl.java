package com.hyc.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.to.SkuReductionTo;
import com.hyc.common.to.SpuBoundTo;
import com.hyc.common.to.es.SkuEsModel;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

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
                skuInfoEntity.setCatelogId(spuInfoEntity.getCatelogId());
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

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 根据参数条件检索 spu
     *  catelogId: 6,//三级分类id
     *  brandId: 1,//品牌id
     *   status: 0,//商品状态
     * @date: 2022/8/24 23:35
     * @param params
     * @return: com.hyc.common.utils.PageUtils
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catelog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 商品上架功能
     * common 对象中es的规格参数传递to skuesmodel
     * 关联服务 gulimall-sreach
     * @date: 2022/9/14 23:52
     * @param spuId
     * @return: void
     */
    @Override
    public void up(Long spuId) {
        //查出对应id的sku
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        BrandEntity brandEntity = brandService.getById(skus.get(0).getBrandId());
        CategoryEntity categoryEntity = categoryService.getById(skus.get(0).getCatelogId());

        String brandname = brandEntity.getName();
        String brandlogo = brandEntity.getLogo();
        String categoryName = categoryEntity.getName();
        //todo 4. 查询当前sku的所有规格属性
        List<ProductAttrValueEntity> baseAttr = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrids = baseAttr.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> SearchId = attrService.selectSearchAttrs(attrids);
        Set<Long> idset = new HashSet<>(SearchId);
        List<SkuEsModel.Attrs> attrsList = baseAttr.stream().filter(item -> {
            return idset.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());
        //封装每一个sku的信息
        skus.stream().map((sku) -> {
            //    组装需要的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            //todo 1、发送远程调用 库存系统查询是否有库存
            //todo 2、热度评分，0，
            skuEsModel.setHotScore(0L);
            //todo 3. 查询品牌和分类的信息
            skuEsModel.setBrandName(brandname);
            skuEsModel.setBrandImg(brandlogo);
            skuEsModel.setCatalogName(categoryName);
            skuEsModel.setAttrs(attrsList);
            return skuEsModel;
        }).collect(Collectors.toList());
        //    todo 5、将数据发给es保存
    }


}