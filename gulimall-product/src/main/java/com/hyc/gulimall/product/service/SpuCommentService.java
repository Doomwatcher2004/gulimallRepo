package com.hyc.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.product.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:48
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

