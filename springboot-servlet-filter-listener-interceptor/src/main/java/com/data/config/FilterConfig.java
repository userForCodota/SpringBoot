package com.data.config;

import com.data.filter.MySecondFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean getFilterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new MySecondFilter());
        //bean.addUrlPatterns(new String[]{"*.do", "*.jsp"});
        bean.addUrlPatterns("/second");
        return bean;
    }
}
