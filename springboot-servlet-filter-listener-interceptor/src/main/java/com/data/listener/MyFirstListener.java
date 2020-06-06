package com.data.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyFirstListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //System.out.println(this.getClass().getSimpleName() + "正在监听contextDestroyed..................");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(this.getClass().getSimpleName() + "正在监听contextInitialized.................");
    }
}
