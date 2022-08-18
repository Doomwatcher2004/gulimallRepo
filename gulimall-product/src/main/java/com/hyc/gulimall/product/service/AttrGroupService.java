package com.hyc.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hyc.common.utils.PageUtils;
import com.hyc.gulimall.product.entity.AttrGroupEntity;
import com.hyc.gulimall.product.vo.AttrGroupwithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:49
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupwithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

}

