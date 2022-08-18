package com.hyc.gulimall.product.feign;

import com.hyc.common.to.SkuReductionTo;
import com.hyc.common.to.SpuBoundTo;
import com.hyc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.product.feign
 * @className: CouponFeignService
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/8/8 19:09
 * @version: 1.0
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @PostMapping(value = "/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(SkuReductionTo skuReductionTo);
}
