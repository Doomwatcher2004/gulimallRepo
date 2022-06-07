package com.hyc.gulimall.order.dao;

import com.hyc.gulimall.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-09 18:03:32
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
