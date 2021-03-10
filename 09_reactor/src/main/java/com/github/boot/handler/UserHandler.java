package com.github.boot.handler;

import com.github.boot.bean.User;
import com.github.boot.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-5:26
 */
public class UserHandler {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    // 根据id查询
    public Mono<ServerResponse> getUserById(ServerRequest request) {
        // 获取id值
        Integer userId = Integer.valueOf(request.pathVariable("id"));

        // 处理空值
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        Mono<User> userMono = userService.getUserById(userId);

        // 使用Reactor操作符flatMap把userMono变成流
        return userMono.
                flatMap(person -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(person)))
                .switchIfEmpty(notFound);

    }

    // 查询所有
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        Flux<User> users = userService.getAllUser();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
    }

    // 添加
    public Mono<ServerResponse> saveUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok()
                .build(userService.saveUserInfo(userMono));
    }
}
