package com.github.boot;

import com.github.boot.bean.Pet;
import com.github.boot.bean.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 主程序类
 * @SpringBootApplication 告知这是一个springboot应用
 * @author HAN
 * @version 1.0
 * @create 03-04-3:12
 */
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args){
        ConfigurableApplicationContext run = SpringApplication.run(MainApplication.class, args);

        // 从容器中获取组件
//        User user = run.getBean(User.class);
//        Pet pet = run.getBean(Pet.class);
//        System.out.println(user);
//        System.out.println(pet);

        // 获取所有组件的名字
        String[] beanNamesForType = run.getBeanNamesForType(User.class);
        System.out.println("================================");
        for (String name : beanNamesForType) {
            System.out.println(name);
        }

        beanNamesForType = run.getBeanNamesForType(Pet.class);
        System.out.println("================================");
        for (String name : beanNamesForType) {
            System.out.println(name);
        }


    }

}
