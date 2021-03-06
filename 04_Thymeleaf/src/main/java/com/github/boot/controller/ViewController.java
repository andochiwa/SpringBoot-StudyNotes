package com.github.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-1:26
 */
@Controller
public class ViewController {

    @GetMapping("/view")
    public String view(Model model) {
        // model中的数据会自动放入请求域中
        model.addAttribute("msg", "hello world");
        model.addAttribute("link", "http://www.google.com");
        return "success";
    }

}
