package com.github.boot.service;

import com.github.boot.bean.Book;
import com.github.boot.dao.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-3:27
 */
@Service
public class BookService {

    @Autowired
    BookMapper bookMapper;

    public Book getBookById(Integer id) {
        return bookMapper.getBookById(id);
    }

}
