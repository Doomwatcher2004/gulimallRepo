package com.hyc.gulimall.ware.VO;

import lombok.Data;

import java.util.List;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.ware.VO
 * @className: MergeVo
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 *  合并采购单的vo
 * @date: 2022/8/27 20:42
 * @version: 1.0
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}
