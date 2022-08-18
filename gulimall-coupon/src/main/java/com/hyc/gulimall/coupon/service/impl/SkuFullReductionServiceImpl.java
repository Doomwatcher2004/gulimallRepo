package com.hyc.gulimall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hyc.common.to.SkuReductionTo;
import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.Query;
import com.hyc.gulimall.coupon.dao.SkuFullReductionDao;
import com.hyc.gulimall.coupon.entity.MemberPriceEntity;
import com.hyc.gulimall.coupon.entity.SkuFullReductionEntity;
import com.hyc.gulimall.coupon.entity.SkuLadderEntity;
import com.hyc.gulimall.coupon.service.MemberPriceService;
import com.hyc.gulimall.coupon.service.SkuFullReductionService;
import com.hyc.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        System.out.println(skuReductionTo);
        // 1.(打折表 满几件打几折) sms_sku_ladder
        SkuLadderEntity ladderEntity = new SkuLadderEntity();
        ladderEntity.setSkuId(skuReductionTo.getSkuId());
        ladderEntity.setFullCount(skuReductionTo.getFullCount());
        ladderEntity.setDiscount(skuReductionTo.getDiscount());
        ladderEntity.setPrice(skuReductionTo.getReducePrice());
        //保存,远程服务也判断 是否是有意义的优惠
        if (skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(ladderEntity);
        }
        // 2.(满减优惠表) sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        System.out.println(skuFullReductionEntity);
        //判断满减
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
            this.save(skuFullReductionEntity);
        }

        // 3.(会员价格表)sms_member_price
        if (!CollectionUtils.isEmpty(skuReductionTo.getMemberPrice())) {
            List<MemberPriceEntity> memberPriceEntityList = skuReductionTo.getMemberPrice().stream().map(price -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setMemberLevelId(price.getId());
                memberPriceEntity.setMemberLevelName(price.getName());
                memberPriceEntity.setMemberPrice(price.getPrice());
                //默认叠加优惠
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).filter(item -> {
                return item.getMemberPrice().compareTo(new BigDecimal("0")) == 1;
            }).collect(Collectors.toList());
            memberPriceService.saveBatch(memberPriceEntityList);
        }


    }

}