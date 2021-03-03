package com.github.boot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController 整合了@Controller和@ResponseBody
 * @author HAN
 * @version 1.0
 * @create 03-04-3:13
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String handle() {
        return "hello spring boot";
    }

}
