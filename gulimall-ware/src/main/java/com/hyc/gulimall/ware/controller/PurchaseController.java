package com.hyc.gulimall.ware.controller;

import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.R;
import com.hyc.gulimall.ware.VO.MergeVo;
import com.hyc.gulimall.ware.VO.PurchaseSDoneVo;
import com.hyc.gulimall.ware.entity.PurchaseEntity;
import com.hyc.gulimall.ware.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 采购信息
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-10 22:49:34
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 和并采购需求 组成采购单
     * @date: 2022/8/29 21:15
     * @param mergeVo
     * @return: com.hyc.common.utils.R
     */
    @PostMapping("/merge")
    public R Merge(@RequestBody MergeVo mergeVo) {
        boolean b = purchaseService.mergePurchase(mergeVo);
        if (b) {
            return R.ok();
        } else {
            return R.error("合并采购单失败，可能是采购项已经采购或者重复");
        }
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 采购单是否被领取 并且改变采购需求的状态
     * @date: 2022/8/29 23:53
     * @param ids
     * @return: com.hyc.common.utils.R
     */
    @PostMapping(value = "/received")
    public R received(@RequestBody List<Long> ids) {
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * @author 冷环渊 Doomwatcher
     * @context: 采购单完成采购单 并且修改采购单和采购需求的状态
     * @date: 2022/8/31 3:18
     * @param purchaseSDoneVo
     * @return: com.hyc.common.utils.R
     */
    @PostMapping(value = "/done")
    public R done(@RequestBody PurchaseSDoneVo purchaseSDoneVo) {
        purchaseService.done(purchaseSDoneVo);
        return R.ok();
    }

    /**
     *  显示还没有开始购买和新建的采购单
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:purchase:list")
    public R unreceivelist(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
