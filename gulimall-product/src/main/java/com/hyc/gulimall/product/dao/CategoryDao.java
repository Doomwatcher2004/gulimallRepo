package com.hyc.gulimall.product.dao;

import com.hyc.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:48
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
