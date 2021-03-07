package com.github.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 做登录检查
 * 配置工作：
 *  1.配置好拦截器要拦截哪些请求
 *  2.把拦截器注册到容器中，配置类实现WebConfigurer并重写addInterceptors
 *  3.在registry中addInterceptor(new xxx)
 * @author HAN
 * @version 1.0
 * @create 03-08-2:32
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截的请求路径是{}", request.getRequestURI());
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            return true;
        }
        session.setAttribute("msg", "请先登录");
        response.sendRedirect("/");
        return false;
    }
}
