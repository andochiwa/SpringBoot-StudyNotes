package com.github.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        SpringApplication.run(MainApplication.class, args);
    }

}
