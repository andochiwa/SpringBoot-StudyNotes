package com.github.admin.config;

import com.github.admin.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author HAN
 * @version 1.0
 * @create 03-08-2:36
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**") // 拦截路径 这种情况会把静态资源也给拦截了
                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**", "/js/**"); // 放行路径

    }
}
