package com.hyc.gulimall.ware.VO;

import lombok.Data;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.ware.VO
 * @className: SkuHasStockVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/9/21 19:17
 * @version: 1.0
 */
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
