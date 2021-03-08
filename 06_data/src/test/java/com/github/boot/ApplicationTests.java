package com.github.boot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
class ApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() {
        String sql = "select * from book";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        maps.forEach(data -> data.forEach((k, v) -> {
            System.out.println(k + ": " + v);
        }));
        log.info("dbPool type: {}", dataSource.getClass());
    }
}
