package com.hyc.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberPrice {
    //会员id
    private Long id;
    //会员名
    private String name;
    //会员价格
    private BigDecimal price;


}