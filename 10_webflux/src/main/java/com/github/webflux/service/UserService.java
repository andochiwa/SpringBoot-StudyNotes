package com.github.webflux.service;

import com.github.webflux.dao.UserDao;
import com.github.webflux.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * @author HAN
 * @version 1.0
 * @since 08-27-21:27
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public Mono<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public Mono<Void> saveUser(User user) {
        return userDao.save(user)
                .then(Mono.empty());
    }
}
