package com.hyc.gulimall.product.vo;

import lombok.Data;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.product.vo
 * @className: AttrRespVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/6/11 21:17
 * @version: 1.0
 */
@Data
public class AttrRespVo extends AttrVo {
    /*
     *"catelogName": "手机/数码/手机", //所属分类名字
     * "groupName": "主体", //所属分组名字
     * */
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
