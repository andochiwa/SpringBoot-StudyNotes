package com.github.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-1:34
 */
@Controller
public class DbController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ResponseBody
    @GetMapping("/sql")
    public Integer sql() {
        Integer count = jdbcTemplate.queryForObject("select count(*) from book", int.class);
        return count;
    }

}
