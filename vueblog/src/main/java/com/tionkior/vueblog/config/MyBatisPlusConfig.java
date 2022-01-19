package com.tionkior.vueblog.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : TionKior
 * @date : 2022/1/19 9:54
 */

@Configuration
@EnableTransactionManagement
public class MyBatisPlusConfig {

    /**
     * 加载分页助手
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusInterceptor ();
    }
}
