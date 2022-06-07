package com.hyc.gulimall.member.fegin;

import com.hyc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.member.fegin
 * @className: CouponFeignService
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/5/13 20:52
 * @version: 1.0
 */
@FeignClient("gulimall-coupon")
@Service
public interface CouponFeignService {
    /*测试方法*/
    @RequestMapping("coupon/coupon/member/list")
    public R membercoupons();
}
