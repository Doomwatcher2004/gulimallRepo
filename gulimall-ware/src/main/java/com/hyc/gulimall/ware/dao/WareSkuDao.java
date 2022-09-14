package com.hyc.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.gulimall.ware.entity.WareSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-10 22:49:33
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addstock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);
}
