package com.github.boot.controller;

import com.github.boot.bean.User;
import com.github.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-5:07
 * @see com.github.boot.handler.UserHandler 函数式
 */
//@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // id查询
    @GetMapping("/user/{id}")
    public Mono<User> getUserId(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    // 查询所有
    @GetMapping("/user")
    public Flux<User> getUsers() {
        return userService.getAllUser();
    }

    // 添加
    @PostMapping("/saveuser")
    public Mono<Void> saveUser(@RequestBody User user) {
        Mono<User> userMono = Mono.just(user);
        return userService.saveUserInfo(userMono);
    }

}
