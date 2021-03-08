package com.github.boot.dao;

import com.github.boot.bean.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author HAN
 * @version 1.0
 * @create 03-09-3:54
 */
@Mapper
public interface CityMapper {

//    @Insert("INSERT INTO city (name, state, country) VALUES(#{name}, #{state}, #{country})")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(City city);

    @Select("SELECT id, name, state, country FROM city WHERE id = #{id}")
    City findById(long id);

}
