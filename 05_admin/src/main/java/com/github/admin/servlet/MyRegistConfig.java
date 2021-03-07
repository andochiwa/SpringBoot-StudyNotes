package com.github.admin.servlet;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 使用spring提供的原生组件注入方式
 * @author HAN
 * @version 1.0
 * @create 03-08-7:08
 */
@Configuration
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean<MyServlet> myServlet() {
        MyServlet servlet = new MyServlet();
        return new ServletRegistrationBean<>(servlet, "/my", "/my02");
    }

    @Bean
    public FilterRegistrationBean<MyFilter> myFilter() {
//        return new FilterRegistrationBean<>(new MyFilter(), myServlet());
        FilterRegistrationBean<MyFilter> filter = new FilterRegistrationBean<>(new MyFilter());
        filter.setUrlPatterns(Arrays.asList("/css/*", "/images/*"));
        return filter;
    }

    @Bean
    public ServletListenerRegistrationBean<MyListener> myListener() {
        return new ServletListenerRegistrationBean<>(new MyListener());
    }
}
