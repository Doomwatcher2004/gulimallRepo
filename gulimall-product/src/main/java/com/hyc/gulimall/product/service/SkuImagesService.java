package com.hyc.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:49
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

