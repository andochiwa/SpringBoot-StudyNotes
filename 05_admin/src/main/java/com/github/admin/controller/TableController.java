package com.github.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-5:54
 */
@Controller
public class TableController {

    @GetMapping("/basic_table")
    public String basic_table() {
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table() {
        return "table/dynamic_table";
    }

    @GetMapping("/responsive_table")
    public String responsive_table() {
        return "table/responsive_table";
    }

    @GetMapping("editable_table")
    public String editable_table() {
        return "editable_table";
    }

}
