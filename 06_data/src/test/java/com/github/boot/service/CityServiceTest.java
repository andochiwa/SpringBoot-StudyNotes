package com.github.boot.service;

import com.github.boot.bean.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-4:02
 */
@SpringBootTest
class CityServiceTest {

    @Autowired
    CityService cityService;

    @Test
    void findById() {
        System.out.println(cityService.findById(1L));
    }

    @Test
    void insert() {
        City city = new City();
        city.setName("aaqCAq");
        city.setState("ca");
        city.setCountry("ww");
        cityService.insert(city);
        System.out.println(city);
    }
}