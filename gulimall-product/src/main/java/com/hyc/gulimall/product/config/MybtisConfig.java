package com.hyc.gulimall.product.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimall.product.config
 * @className: MybtisConfig
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/6/7 15:46
 * @version: 1.0
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.hyc.gulimall.product.dao")
public class MybtisConfig {
    // 最新版
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
