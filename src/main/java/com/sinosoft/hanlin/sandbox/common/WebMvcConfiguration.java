package com.sinosoft.hanlin.sandbox.common;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").maxAge(3600).allowedMethods("*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConverter.setFastJsonConfig(config);
        MediaType[] types = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                new MediaType("application", "vnd.spring-boot.actuator.v1+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v2+json"),
                new MediaType("application", "vnd.spring-boot.actuator.v3+json")};
        fastJsonConverter.setSupportedMediaTypes(Arrays.asList(types));
        converters.add(0, fastJsonConverter);
    }

}
