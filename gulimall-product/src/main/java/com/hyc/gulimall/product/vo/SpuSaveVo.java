/**
 * Copyright 2022 json.cn
 */
package com.hyc.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2022-07-26 22:25:46
 * 固定参数
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class SpuSaveVo {
    //基础参数名
    private String spuName;
    //基础参数描述
    private String spuDescription;
    //分类id
    private Long catelogId;
    //品牌id
    private Long brandId;
    //重量
    private BigDecimal weight;
    //销售状态
    private int publishStatus;
    //
    private List<String> decript;
    //图集
    private List<String> images;
    //积分
    private Bounds bounds;
    //基础参数合集
    private List<BaseAttrs> baseAttrs;
    //销售属性合集
    private List<Skus> skus;

}