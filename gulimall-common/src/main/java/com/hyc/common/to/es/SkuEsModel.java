package com.hyc.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.common.to.es
 * @className: SkuEsModel
 * @author: 冷环渊 doomwatcher
 * @description: 商城系统规格参数传递到搜索服务的传输对象
 * @date: 2022/9/14 23:46
 * @version: 1.0
 */
@Data
public class SkuEsModel {

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private Long catelogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs {

        private Long attrId;

        private String attrName;

        private String attrValue;

    }

}
