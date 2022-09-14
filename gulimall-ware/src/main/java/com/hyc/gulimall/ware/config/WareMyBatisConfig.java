package com.hyc.gulimall.ware.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.ware.config
 * @className: WareMyBatisConfig
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/8/31 3:28
 * @version: 1.0
 */
@Configuration
public class WareMyBatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(true);
        // paginationInterceptor.setOverflow(true);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
}
