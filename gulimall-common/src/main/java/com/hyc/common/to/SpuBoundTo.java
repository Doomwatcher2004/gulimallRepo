package com.hyc.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.to
 * @className: SpuBoundTo
 * @author: 冷环渊 doomwatcher
 * @description: TODO 远程调用 积分对象
 * @date: 2022/8/8 19:14
 * @version: 1.0
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    //    购买积分
    private BigDecimal buyBounds;
    //  增长积分
    private BigDecimal growBounds;
}
