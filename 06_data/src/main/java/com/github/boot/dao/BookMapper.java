package com.github.boot.dao;

import com.github.boot.bean.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-3:16
 */
@Mapper
public interface BookMapper {

    Book getBookById(Integer id);

}
