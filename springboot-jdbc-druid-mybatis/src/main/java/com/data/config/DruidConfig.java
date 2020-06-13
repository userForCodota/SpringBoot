package com.data.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource getDruidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();//阿里巴巴给我们写好的类
        return druidDataSource;
    }

    //扩展:Druid框架里面，阿里巴巴还写了一个后台监控，我们把这个也注入进去。通过
//    @Bean
    public ServletRegistrationBean getStatViewServlet() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet());//固定写法，页面本站是Servlet
        //这个后台监控的属性简单设置一下
        HashMap<String, String> initParameters = new HashMap<>();//传参用的map
        initParameters.put("loginUsername", "admin");
        initParameters.put("loginPassword", "123456");
        initParameters.put("allow", "localhost");
        bean.setInitParameters(initParameters);//初始化
        return bean;
    }
}
