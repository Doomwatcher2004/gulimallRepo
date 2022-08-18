package com.hyc.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.to
 * @className: SkuReductionTo
 * @author: 冷环渊 doomwatcher
 * @description: TODO 远程调用 商品属性优惠对象
 * @date: 2022/8/8 19:14
 * @version: 1.0
 */
@Data
public class SkuReductionTo {
    //商品销售属性id
    Long skuId;
    //满多少
    private int fullCount;
    //减多少
    private BigDecimal discount;
    //达到多少个
    private int countStatus;
    //最大价格
    private BigDecimal fullPrice;
    //降价多少
    private BigDecimal reducePrice;
    //
    private int priceStatus;
    //会员价格
    private List<MemberPrice> memberPrice;
}
