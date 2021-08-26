package com.github.webflux.controller;

import com.github.webflux.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author HAN
 * @version 1.0
 * @since 08-26-19:22
 */
@RestController
public class UserController {

    @GetMapping("/mono")
    public Mono<User> getUserToMono() {
        return Mono.just(new User("hello", "hello@gmail.com"));
    }

}
