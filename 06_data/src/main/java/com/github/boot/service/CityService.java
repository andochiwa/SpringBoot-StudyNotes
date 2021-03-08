package com.github.boot.service;

import com.github.boot.bean.City;
import com.github.boot.dao.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-3:59
 */
@Service
public class CityService {

    @Autowired
    CityMapper cityMapper;

    public City findById(Long id) {
        return cityMapper.findById(id);
    }

    public void insert(City city) {
        cityMapper.insert(city);
    }

}
