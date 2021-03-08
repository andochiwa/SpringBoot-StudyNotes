package com.github.admin.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-5:14
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void selectById() {
        System.out.println(userMapper.selectById(1));
    }

}