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

    /**
     * 1.浏览器发请求直接返回xml时 [application/xml]  JacksonXmlConverter处理
     * 2.如果是ajax请求，返回json [application/json] JacksonJsonConverter处理
     * 3.如果返回自定义协议数据    [application/xxx]  自定义xxxConverter 处理
     *
     * 步骤：
     * 1.添加自定义的MessageConverter进系统低层
     * 2.系统低层会统计出所有MessageConverter能操作哪些类型
     * 3.客户端内容协商[xxx -> xxx]
     */
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
