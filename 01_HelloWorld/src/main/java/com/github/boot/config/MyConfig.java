package com.github.boot.config;

import ch.qos.logback.core.db.DBHelper;
import com.github.boot.bean.Car;
import com.github.boot.bean.Pet;
import com.github.boot.bean.User;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 第二种创建bean对象的方法
 * @Import 给容器中自动创建类型的组件, 默认组件的名字为全类名
 * @ImportResource 导入xml配置文件方式
 * @EnableConfigurationProperties 开启Car的属性配置功能，把组件自动导入到容器中
 *
 * @author HAN
 * @version 1.0
 * @create 03-04-4:18
 */
@Import({User.class, DBHelper.class})
@Configuration
// @ImportResource("classpath:beans.xml")
@EnableConfigurationProperties(Car.class)
public class MyConfig {


    // 条件注解，有条件中的组件时才去注册组件
    @ConditionalOnBean(name = "com.github.boot.bean.User")
    @Bean
    public User user() {
        return new User("zhangsan", 18);
    }

    @Bean
    public Pet pet() {
        return new Pet("Tomcat");
    }

}
