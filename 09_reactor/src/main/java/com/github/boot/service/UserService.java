package com.github.boot.service;

import com.github.boot.bean.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-4:56
 */
public interface UserService {

    Mono<User> getUserById(Integer id);

    Flux<User> getAllUser();

    Mono<Void> saveUserInfo(Mono<User> user);

}
