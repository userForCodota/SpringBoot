package com.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;


/**
 * 按照官网文档，如果想扩展SpringMVC，官网建议我们这么做：
 * 一个类实现WebMvcConfigurer接口，就能扩展SpringMVC的配置，所以此类是一个自动配置类，通过重写对应的方法实现对应的组件
 * 且不能加上@EnableWebMvc这个注解
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Bean
    public ViewResolver MyViewResolver() {
        return new MyViewResolver();
    }

    //假设我们需要把MyViewResolver这个自定义的视图解释器交给SpringBoot
    //这里把类写成匿名内部类方便我们看
    public static class MyViewResolver implements ViewResolver {
        @Override
        public View resolveViewName(String viewName, Locale locale) throws Exception {
            return null;
        }
    }
}
