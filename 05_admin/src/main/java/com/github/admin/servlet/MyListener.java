package com.github.admin.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author HAN
 * @version 1.0
 * @create 03-08-7:03
 */
//@WebListener
@Slf4j
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("MyListener init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("MyListener destroy");
    }
}
