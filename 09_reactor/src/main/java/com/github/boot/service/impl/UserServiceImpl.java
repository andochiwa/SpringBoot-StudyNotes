package com.github.boot.service.impl;

import com.github.boot.bean.User;
import com.github.boot.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-4:58
 */
@Service
public class UserServiceImpl implements UserService {

    // 创建map存储数据
    private final Map<Integer, User> users = new HashMap<>();

    public UserServiceImpl() {
        this.users.put(1, new User("zhangsan", "男", 20));
        this.users.put(2, new User("lisi", "男", 15));
        this.users.put(3, new User("wangwu", "女", 23));

    }

    @Override
    public Mono<User> getUserById(Integer id) {
        return Mono.justOrEmpty(this.users.get(id));
    }

    @Override
    public Flux<User> getAllUser() {
        return Flux.fromIterable(this.users.values());
    }

    @Override
    public Mono<Void> saveUserInfo(Mono<User> userMono) {
        return userMono.doOnNext(person -> {
            // 往map中放置
            int id = this.users.size() + 1;
            this.users.put(id, person);
        }).thenEmpty(Mono.empty());
    }
}
