package com.data.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //模拟登陆拦截
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            //无登录则拦截
            System.out.println(this.getClass().getSimpleName() + ":无权限，请先登录");
            request.setAttribute("msg", "无权限，请先登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;//不放行
        } else {
            return true;
        }
    }
}

//拦截器写好之后就需要解决如何交给SpringBootMVC接管了，这里利用com.data.config.MyMvcConfig类经过注入，具体的知识点请参考README-SpringBootWeb.md中的SpringMVC配置原理