package com.github.boot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAN
 * @version 1.0
 * @create 03-11-4:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;
    private String gender;
    private Integer age;

}
