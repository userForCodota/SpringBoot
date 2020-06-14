package com.data.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


/**
 * 注意别导错包了
 */
//@WebFilter(filterName = "MyFirstFilter", urlPatterns = {"*.do", "*.jsp"})
@WebFilter(filterName = "MyFirstFilter", urlPatterns = "/first")
public class MyFirstFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("初始化过滤器" + this.getClass().getName() + ".............");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入" + this.getClass().getName());
        filterChain.doFilter(request, response);//过滤链
        System.out.println("离开" + this.getClass().getName());
    }

    @Override
    public void destroy() {
        System.out.println("销毁过滤器" + this.getClass().getName() + "...............");
    }

}
