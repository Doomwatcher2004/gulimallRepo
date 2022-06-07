package com.hyc.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-09 18:03:32
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

