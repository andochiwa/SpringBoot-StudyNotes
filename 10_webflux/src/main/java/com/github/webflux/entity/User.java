package com.github.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAN
 * @version 1.0
 * @since 08-26-19:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;

    private String email;
}
