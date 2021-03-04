package com.github.boot.controller;

import com.github.boot.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HAN
 * @version 1.0
 * @create 03-05-0:00
 */
@RestController
public class MyController {

    @Autowired
    Person person;

    @RequestMapping("/person")
    public Person person() {
        return person;
    }

}
