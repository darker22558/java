package com.geo.integrated.config;

import com.geo.integrated.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: whtli
 * @date: 2023/02/01
 * @description: 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断token是否合法来决定是否需要登录
        // 放行静态文件
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login", "/**/generateAuthCode" , "/**/register", "/management/**", "/front/**",
                        "/swagger-ui.html/**", "/swagger-resources/**", "/v2/", "/api", "/api-docs", "/api-docs/**")
                .excludePathPatterns("/**/*.html", "/**/*.js", "/**/*.css", "/**/*.woff", "/**/*.ttf");

    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
