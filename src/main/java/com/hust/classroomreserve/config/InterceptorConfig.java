package com.hust.classroomreserve.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器的类
 * 该类实现了WebMvcConfigurer接口，用于自定义Spring MVC的配置
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器配置
     * 该方法用于向InterceptorRegistry中添加拦截器，以实现对特定请求的预处理和后处理
     * 
     * @param registry InterceptorRegistry实例，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
                new JwtInterceptor())
                .addPathPatterns("/**")
                //文件导出
                //登录
                .excludePathPatterns("/**/login")
                .excludePathPatterns("/login");


    }

}