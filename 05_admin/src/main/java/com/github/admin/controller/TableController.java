package com.github.admin.controller;

import com.github.admin.bean.User;
import com.github.admin.exception.UserTooManyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-5:54
 */
@Controller
public class TableController {

    @GetMapping("/basic_table")
    public String basic_table() {
        int i = 10 / 0;
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model) {
        // 表格内容的遍历
        List<User> users = Arrays.asList(new User("zhangsan", "123456"),
                new User("list", "112233"),
                new User("hello", "132465"),
                new User("abcd", "18888"));
        model.addAttribute("users", users);
        if (users.size() > 3) {
            throw new UserTooManyException();
        }
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
