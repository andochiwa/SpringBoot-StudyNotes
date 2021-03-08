package com.github.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-1:05
 */
@Deprecated
//@Configuration
public class DataSourceConfig {

    @SneakyThrows
    @ConfigurationProperties(prefix = "spring.datasource") // 把数据源信息和yml配置文件绑定
//    @Bean
    public DataSource dataSource() {
        //        dataSource.setFilters("stat,wall"); // 加入监控功能, 防火墙功能，这些都可以在配置文件中设置
        return new DruidDataSource();
    }

    /**
     * 配置Druid的内置监控功能
     */
//    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet() {
        StatViewServlet statViewServlet = new StatViewServlet();
        return new ServletRegistrationBean<>(statViewServlet, "/druid/*");
    }

    /**
     * WebStatFilter 用于采集web-jdbc关联监控的数据
     */
//    @Bean
    public FilterRegistrationBean<WebStatFilter> webStatFilter() {
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<>(webStatFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
