package com.hyc.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.coupon.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-08 21:05:41
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

