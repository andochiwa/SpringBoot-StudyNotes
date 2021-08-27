package com.github.webflux.controller;

import com.github.webflux.entity.User;
import com.github.webflux.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author HAN
 * @version 1.0
 * @since 08-26-19:22
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    // webflux
    @GetMapping("/mono")
    public Mono<User> getUserToMono() {
        log.info("webflux get start");
        Mono<User> userMono = Mono.fromSupplier(this::getUser);
        log.info("webflux get end");
        return userMono;
    }

    // spring mvc
    @GetMapping("/hello")
    public User getUserByMvc() {
        log.info("spring mvc get start");
        User user = getUser();
        log.info("spring mvc get end");
        return user;
    }

    @SneakyThrows
    private User getUser() {
        TimeUnit.SECONDS.sleep(5);
        return new User(5L, "hello");
    }

    @GetMapping("/r2dbc/{id}")
    public Mono<User> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

}
