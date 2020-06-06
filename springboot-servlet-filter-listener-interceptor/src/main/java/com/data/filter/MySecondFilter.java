package com.data.filter;

import javax.servlet.*;
import java.io.IOException;


/**
 * 不加注解，交给自动配置类@bean
 */
public class MySecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入" + this.getClass().getName());
        filterChain.doFilter(request, response);//过滤链
        System.out.println("离开" + this.getClass().getName());
    }

    @Override
    public void destroy() {

    }
}
//后面的工作由