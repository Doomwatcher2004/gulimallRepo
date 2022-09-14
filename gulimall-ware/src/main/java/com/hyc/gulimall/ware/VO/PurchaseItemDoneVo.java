package com.hyc.gulimall.ware.VO;

import lombok.Data;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.ware.VO
 * @className: PurchaseItemDoneVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/8/30 18:58
 * @version: 1.0
 */
@Data
public class PurchaseItemDoneVo {
    //itemId:4,status:3,reason:""
    //采购需求的id
    private Long itemId;
    //状态
    private Integer status;
    //采购备注/失败原因
    private String reason;

}


