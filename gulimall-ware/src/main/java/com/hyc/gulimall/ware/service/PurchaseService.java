package com.hyc.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.ware.VO.MergeVo;
import com.hyc.gulimall.ware.VO.PurchaseSDoneVo;
import com.hyc.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-10 22:49:34
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    boolean mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseSDoneVo purchaseSDoneVo);
}

