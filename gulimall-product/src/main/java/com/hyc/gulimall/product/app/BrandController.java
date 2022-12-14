package com.hyc.gulimall.product.controller;

import com.hyc.common.utils.PageUtils;
import com.hyc.common.utils.R;
import com.hyc.common.valid.AddGroup;
import com.hyc.common.valid.UpdateGroup;
import com.hyc.common.valid.UpdateStatusGroup;
import com.hyc.gulimall.product.entity.BrandEntity;
import com.hyc.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌
 *
 * @author lenghuanyuan
 * @email 3132774018@qq.com
 * @date 2022-05-22 16:21:49
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);
        System.out.println(page.getList());
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand /*BindingResult result*/) {
        //if (result.hasErrors()) {
        //    HashMap<String, String> map = new HashMap<>();
        //    //1.获取校验错误结果
        //    result.getFieldErrors().forEach((item) -> {
        //        //    获取到的错误提示
        //        String message = item.getDefaultMessage();
        //        //获取错误属性的名字
        //        String field = item.getField();
        //        map.put(field, message);
        //    });
        //    return R.error(400, "提交数据不合法").put("data", map);
        //}
        brandService.save(brand);

        return R.ok();
    }

    /**
     *  修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    //@RequiresPermissions("product:brand:update")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand) {
        System.out.println(brand);
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));
        return R.ok();
    }

}
