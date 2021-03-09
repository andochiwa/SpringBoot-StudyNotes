package com.github.junit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@DisplayName("JUnit5功能测试类")
class JunitApplicationTests {

    @DisplayName("测试DisplayName注解")
    @Test
    void contextLoads() {
        System.out.println("测试1开始");
    }

    @Disabled
    @DisplayName("测试方法2")
    @Test
    void test1() {
        System.out.println("测试2开始");
    }

    /**
     * 规定方法超时时间，超出时间就会抛出异常
     */
    @SneakyThrows
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    @Test
    @DisplayName("测试Timeout")
    void testTimeout() {
        Thread.sleep(501);
    }

    @BeforeEach
    void testBeforeEach() {
        System.out.println("测试就要开始了");
    }

    @AfterEach
    void testAfterEach() {
        System.out.println("测试结束了");
    }

    @BeforeAll
    static void testBeforeAll() {
        System.out.println("所有测试就要开始了");
    }

    @AfterAll
    static void testAfterAll() {
        System.out.println("所有测试结束了");
    }

}
