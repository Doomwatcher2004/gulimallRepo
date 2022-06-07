package com.hyc.gulimall.member.dao;

import com.hyc.gulimall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-08 23:38:04
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
