package com.hyc.gulimall.ware.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyc.gulimall.ware.entity.PurchaseDetailEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-10 22:49:34
 */
@Mapper
public interface PurchaseDetailDao extends BaseMapper<PurchaseDetailEntity> {

    void updatePurchaseDetailStatus(@Param("id") Long id, @Param("Status") int Status);


}
