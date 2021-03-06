package com.github.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-2:18
 */
@Controller
public class IndexController {

    /**
     * 来登录页
     */
    @GetMapping(value = {"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String main(String username, String password) {
        return "";
    }

}
