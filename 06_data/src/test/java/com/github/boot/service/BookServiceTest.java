package com.github.boot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-3:29
 */
@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Test
    void getBookById() {
        System.out.println(bookService.getBookById(3));
    }
}