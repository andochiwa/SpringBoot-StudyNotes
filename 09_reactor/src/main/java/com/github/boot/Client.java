package com.github.boot;

import com.github.boot.bean.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-6:08
 */
public class Client {

    public static void main(String[] args){
        WebClient webClient = WebClient.create("http://localhost:7064");

        // 根据id查询
        int id = 1;
        User user = webClient.get().uri("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .block();
        System.out.println(user);
    }

}
