package com.hyc.gulimall.ware.VO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.ware.VO
 * @className: PurchaseSDoneVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/8/30 18:57
 * @version: 1.0
 */
@Data
public class PurchaseSDoneVo {
    @NotNull
    //采购单id
    private Long id;

    private List<PurchaseItemDoneVo> items;
}
