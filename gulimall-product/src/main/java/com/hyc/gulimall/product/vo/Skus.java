/**
 * Copyright 2022 json.cn 
 */
package com.hyc.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2022-07-26 22:25:46
 * 销售参数
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class Skus {
    //销售属性
    private List<Attr> attr;
    //销售属性名称
    private String skuName;
    //价格
    private BigDecimal price;
    //销售标题
    private String skuTitle;
    //销售副标题
    private String skuSubtitle;
    //图集
    private List<Images> images;
    //
    private List<String> descar;
    //满足数量
    private BigDecimal fullCount;
    //减多少
    private BigDecimal discount;
    //达到多少个
    private int countStatus;
    //需要满足的价格
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    //
    private int priceStatus;
    //会员价格
    private List<MemberPrice> memberPrice;

}