package com.github.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HAN
 * @version 1.0
 * @create 03-05-1:00
 */
@RestController
public class HelloController {

    @RequestMapping("/3.jpg")
    public String hello() {
        return "hello";
    }

}
