package com.funcxy.oj;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.mongo.JdkMongoSessionConverter;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

/**
 * HttpSessionConfig
 * Created by zccz14 on 2017/3/2.
 */
@EnableMongoHttpSession
public class HttpSessionConfig {
    @Bean
    public JdkMongoSessionConverter jdkMongoSessionConverter() {
        return new JdkMongoSessionConverter();
    }
}
