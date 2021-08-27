package com.github.webflux.dao;

import com.github.webflux.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author HAN
 * @version 1.0
 * @since 08-27-21:27
 */
public interface UserDao extends ReactiveCrudRepository<User, Long> {

}
