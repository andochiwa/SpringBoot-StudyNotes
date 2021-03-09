package com.github.junit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author HAN
 * @version 1.0
 * @create 03-10-2:05
 */
@SpringBootTest
@DisplayName("测试断言")
public class AssertTest {

    /**
     * import static org.junit.jupiter.api.Assertions.assertEquals;
     * 如果前面断言失败，则往后的代码都不会执行
     */
    @DisplayName("简单断言")
    @Test
    void testSimpleAssertions() {
        int cal = 2 + 3;
        assertEquals(5, cal, "计算失败");
        Object o1 = new Object();
        Object o2 = new Object();
        assertSame(o1, o2);
    }

    @DisplayName("数组断言")
    @Test
    void testArray() {
        assertArrayEquals(new int[]{1, 2}, new int[]{1, 2});
    }

    @DisplayName("组合断言")
    @Test
    void all() {
        assertAll("test",
                () -> assertTrue(true),
                () -> assertEquals(1, 2));
    }

    @DisplayName("异常断言")
    @Test
    void testException() {
        // 业务逻辑必须出现异常，否则断言失败
        assertThrows(ArithmeticException.class,
                () -> {int i = 10 / 0;},
                "业务逻辑居然正常？");
    }

    @SneakyThrows
    @DisplayName("超时断言")
    @Test
    void testTimeout() {
        assertTimeout(Duration.ofMillis(500), () -> Thread.sleep(600));
    }

    @DisplayName("快速失败")
    @Test
    void testFail() {
        int i = 2;
        if (i == 2) {
            fail("测试失败");
        }
    }


}
