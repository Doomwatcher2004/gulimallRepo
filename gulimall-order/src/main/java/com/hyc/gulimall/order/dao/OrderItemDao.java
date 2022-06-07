package com.hyc.gulimall.order.dao;

import com.hyc.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-09 18:03:32
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
