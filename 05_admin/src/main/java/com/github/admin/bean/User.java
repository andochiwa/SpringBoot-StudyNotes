package com.github.admin.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAN
 * @version 1.0
 * @create 03-07-2:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userName;
    private String password;

}
