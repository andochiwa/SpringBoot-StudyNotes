package com.github.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.admin.bean.User;
import com.github.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-5:54
 */
@Controller
@Transactional
public class TableController {

    @Autowired
    UserService userService;

    @GetMapping("/basic_table")
    public String basic_table() {
//        int i = 10 / 0;
        return "table/basic_table";
    }

    @GetMapping("/dynamic_table")
    public String dynamic_table(Model model, @RequestParam(value = "pn", defaultValue = "1") Integer pn) {
        // 表格内容的遍历
//        List<User> users = Arrays.asList(new User("zhangsan", "123456"),
//                new User("list", "112233"),
//                new User("hello", "132465"),
//                new User("abcd", "18888"));
//        model.addAttribute("users", users);
//        if (users.size() > 3) {
//            throw new UserTooManyException();
//        }

        List<User> users = userService.list();
//        model.addAttribute("users", users);

        // 分页查询数据
        Page<User> page = new Page<>(pn, 2);
        Page<User> userPage = userService.page(page, null);
        model.addAttribute("page", userPage);

        return "table/dynamic_table";
    }

    @GetMapping ("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.removeById(id);

        return "redirect:/dynamic_table";
    }

    @GetMapping("/responsive_table")
    public String responsive_table() {
        return "table/responsive_table";
    }

    @GetMapping("editable_table")
    public String editable_table() {
        return "table/editable_table";
    }

}
