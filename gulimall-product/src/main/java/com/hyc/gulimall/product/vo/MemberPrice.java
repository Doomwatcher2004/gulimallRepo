/**
 * Copyright 2022 json.cn
 */
package com.hyc.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Auto-generated: 2022-07-26 22:25:46
 * 会员价格
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
@Data
public class MemberPrice {
    //会员id
    private Long id;
    //会员名
    private String name;
    //会员价格
    private BigDecimal price;


}