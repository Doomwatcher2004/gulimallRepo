package com.hyc.gulimall.ware.feign;

import com.hyc.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface PreductFeignService {
    /**
     * @author 冷环渊 Doomwatcher
     * @context:
     * 请求的两种写法：
     *  1、product/skuinfo/info/{skuId}
     *  2、api/product/skuinfo/info/{skuId}
     *
     * 1) 如果需要让所有请求包括微服务内的请求都过网关
     *  1、@FeignClient("gulimall-gateway") 给网关服务器发送请求
     *  2、api/product/skuinfo/info/{skuId}
     * 2) 如果不需要过网关 直接用访问微服务机器
     * @date: 2022/8/31 3:44
     * @param skuId
     * @return: com.hyc.common.utils.R
     */
    @RequestMapping("product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId);
}
