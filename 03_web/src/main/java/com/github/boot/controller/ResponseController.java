package com.github.boot.controller;

import com.github.boot.bean.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @author HAN
 * @version 1.0
 * @create 03-05-23:46
 */
@Controller
public class ResponseController {

    @ResponseBody
    @GetMapping("/test/person")
    public Person person() {
        Person person = new Person();
        person.setAge(18);
        person.setBirth(new Date());
        person.setUserName("zhangsan");
        return person;
    }

}
