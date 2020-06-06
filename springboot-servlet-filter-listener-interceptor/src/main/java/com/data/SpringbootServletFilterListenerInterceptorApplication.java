package com.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan//在SpringBoot启动的时候去扫描加了@WebServlet、@WebFilter、@WebListener等注解的类，并将其实例化
public class SpringbootServletFilterListenerInterceptorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootServletFilterListenerInterceptorApplication.class, args);
    }
}
