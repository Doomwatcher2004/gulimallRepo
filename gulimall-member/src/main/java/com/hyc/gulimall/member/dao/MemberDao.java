package com.hyc.gulimall.member.dao;

import com.hyc.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-08 23:38:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
