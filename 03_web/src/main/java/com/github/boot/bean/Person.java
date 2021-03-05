package com.github.boot.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author HAN
 * @version 1.0
 * @create 03-05-22:16
 */
@Data
public class Person {

    private String userName;
    private Integer age;
    private Date birth;
    private Pet pet;

}
