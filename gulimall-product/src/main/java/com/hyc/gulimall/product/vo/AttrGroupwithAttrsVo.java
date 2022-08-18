package com.hyc.gulimall.product.vo;

import com.hyc.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.product.vo
 * @className: AttrGroupwithAttrsVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/7/4 18:14
 * @version: 1.0
 */
@Data
public class AttrGroupwithAttrsVo {

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 所属分类列表
     * */
    private List<AttrEntity> attrs;
}
