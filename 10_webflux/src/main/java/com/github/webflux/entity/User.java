package com.github.webflux.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author HAN
 * @version 1.0
 * @since 08-26-19:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    private String name;
}
