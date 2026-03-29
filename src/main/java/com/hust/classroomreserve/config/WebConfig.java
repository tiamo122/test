package com.hust.classroomreserve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于全局的Web应用配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 添加跨域资源共享(CORS)映射配置
     * 
     * 该方法配置了CORS策略，以允许来自任何域的请求访问/superadmin/下的资源
     * 它支持GET, POST, PUT, DELETE, OPTIONS等HTTP方法，并允许携带任何头信息的请求
     * 此外，还允许请求携带用户凭证，如Cookies
     *
     * @param registry CORS注册表，用于添加CORS映射
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 使用模式匹配
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}