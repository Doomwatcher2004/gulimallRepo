package com.hyc.gulimallsearch.vo;

import com.hyc.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimallsearch.vo
 * @className: SearchResponse
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/10/3 23:35
 * @version: 1.0
 */
@Data
public class SearchResponse {
    //商品信息
    private List<SkuEsModel> products;
    //当前页码
    private Integer pageNum;
    //总记录数
    private Long total;
    //总页码
    private Integer totalPages;
    //当前查询的结果所涉及的品牌
    private List<BrandVo> brands;
    //当前查询到的结果所涉及的属性
    private List<AttrVo> attrs;
    //当前查询到的结果所涉及的分类
    private List<CatelogVo> Catelogs;

    @Data
    public static class BrandVo {
        private String brandName;
        private Long brandId;
        private String brandImg;
    }

    @Data
    public static class AttrVo {
        private String brandName;
        private Long brandId;
        private String brandImg;
    }

    @Data
    public static class CatelogVo {
        private Long catelogId;
        private String catalogName;
    }
}
