package com.hyc.gulimallsearch.cofnig;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @projectName: gulimall
 * @package: com.hyc.gulimallsearch.cofnig
 * @className: gulimallElasticSearchConfig
 * @author: 冷环渊 doomwatcher
 * @description: TODO
 * @date: 2022/9/4 21:51
 * @version: 1.0
 */
@Configuration
public class gulimallElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = null;
        builder = RestClient.builder(new HttpHost("110.40.214.10", 9200, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
