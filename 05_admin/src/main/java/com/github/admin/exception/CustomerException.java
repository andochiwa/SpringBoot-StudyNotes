package com.github.admin.exception;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常
 *
 * @author HAN
 * @version 1.0
 * @create 03-08-6:31
 */
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE) // 为了测试，给其最高优先级
public class CustomerException implements HandlerExceptionResolver {


    @SneakyThrows
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception ex) {
        response.sendError(511, "自定义错误");
        return new ModelAndView();
    }
}
