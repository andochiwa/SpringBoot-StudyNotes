package com.github.boot.config;

import com.github.boot.converter.MyConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.ParameterContentNegotiationStrategy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author HAN
 * @version 1.0
 * @create 03-05-4:35
 */
@Configuration(proxyBeanMethods = false)
public class WebConfig implements WebMvcConfigurer {

    /**
     * 改变_method变为自定义
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        hiddenHttpMethodFilter.setMethodParam("_m");
        return hiddenHttpMethodFilter;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        // 设置不移出分号内容，矩阵变量生效
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    // WebMvcConfigurer定制化springMVC的功能
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * 把Converter自定义规则添加进去
             */
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(new MyConverter());
            }

            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {

            }

            // 自定义内容协商策略
            @Override
            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
                // Map<String, MediaType> mediaTypes
                HashMap<String, MediaType> mediaTypeHashMap = new HashMap<>();
                mediaTypeHashMap.put("json", MediaType.APPLICATION_JSON);
                mediaTypeHashMap.put("xml", MediaType.APPLICATION_XML);
                mediaTypeHashMap.put("me", MediaType.parseMediaType("application/x-me"));
                // 指定支持解析哪些参数对应的哪些媒体类型
                ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(mediaTypeHashMap);
                configurer.strategies(Collections.singletonList(strategy));
            }
        };
    }
}
