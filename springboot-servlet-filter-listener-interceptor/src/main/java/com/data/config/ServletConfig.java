package com.data.config;

import com.data.servlet.MySecondServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {
    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new MySecondServlet());//注意传的参是我们需要的Servlet
        bean.addUrlMappings("/second");//设置对应的请求url
        return bean;
    }
}
