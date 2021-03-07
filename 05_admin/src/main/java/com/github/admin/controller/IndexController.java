package com.github.admin.controller;

import com.github.admin.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

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
    public String loginPage(HttpSession session) {
        return "login";
    }

    /**
     * 登陆成功，重定向到main
     * 防止表单重复提交
     */
    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model) {
        if (StringUtils.hasText(user.getUserName()) && "123456".equals(user.getPassword())) {
            // 把登陆成功用户保存起来
            session.setAttribute("loginUser", user);
            return "redirect:/main.html";
        } else {
            model.addAttribute("msg", "账号密码错误");
            return "login";
        }

    }

    /**
     * 去main页
     */
    @GetMapping("/main.html")
    public String mainPage(HttpSession session,Model model) {

//        // 是否登陆 使用拦截器或过滤器，这里为了简单
//        Object loginUser = session.getAttribute("loginUser");
//        if(loginUser != null) {
//            return "main";
//        } else {
//            model.addAttribute("msg", "请重新登陆");
//            return "login";
//        }

        return "main";

    }

}
