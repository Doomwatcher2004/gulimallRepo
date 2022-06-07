package com.hyc.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-09 18:03:32
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

