# springboot整合Web层技术之：servlet-filter-listener-interceptor

虽然SpringBoot已经封装了SpringMVC，但是我们仍然要使用Servlet、过滤器、监听器、拦截器等，他们是如何整合到SpringBoot中的呢？方法有很多。

## SpringBoot整合Servlet的2中方法

### 方式1：注解扫描方式注册Servlet

步骤1：创建自定义的Servlet并加上`@WebServlet`注解；

步骤2：主启动类加上`@ServletComponentScan`注解开启扫描，其作用是在SpringBoot启动的时候去扫描加了@WebServlet注解的类，并将其实例化。

```java
@WebServlet(name = "MyFirstServlet", urlPatterns = "/first")
public class MyFirstServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        System.out.println(this.getClass().getName());
    }
}
```

```java
@SpringBootApplication
@ServletComponentScan//在SpringBoot启动的时候去扫描加了@WebServlet注解的类，并将其实例化
public class SpringbootServletFilterListenerInterceptorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootServletFilterListenerInterceptorApplication.class, args);
    }
}
```

### 方式2：不依赖注解通过配置类加bean的方式完成注入

这种方式就比较符合SpringBoot的风格，需要什么bean通过`@Configuration`配置类中`@bean`修饰的方法返回对应的bean实现注册：

```java
@Configuration
public class ServletConfig {
    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new MySecondServlet());//注意传的参是我们需要的Servlet
        bean.addUrlMappings("/second");//设置对应的请求url
        return bean;
    }
}
```

```java
public class MySecondServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //super.doGet(req, resp);
        System.out.println(this.getClass().getName());
    }
    //剩下的工作在com.data.config.ServletConfig配置类中完成。
}
```

## SpringBoot整合Filter的2种方式

和Servlet的方式类似，也是有注解扫描和自动装配2中方式：

### 注解扫描方式

```java
@WebFilter(filterName = "MyFirstFilter", urlPatterns = "/first")
public class MyFirstFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入" + this.getClass().getName());
        filterChain.doFilter(request, response);//过滤链
        System.out.println("离开" + this.getClass().getName());
    }
}
//启动类中修改修改：
@SpringBootApplication
@ServletComponentScan//在SpringBoot启动的时候去扫描加了@WebServlet、@WebFilter等注解的类，并将其实例化
public class SpringbootServletFilterListenerInterceptorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootServletFilterListenerInterceptorApplication.class, args);
    }
}
```

### 自动装配方式

```java
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
```

## SpringBoot整合Listener的2种方式

同上。类似。注意事项，监听器有很多，这里以其中一个为例子。

方式1：

```java
@WebListener
public class MyFirstListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(this.getClass().getSimpleName() + "正在监听contextDestroyed..................");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(this.getClass().getSimpleName() + "正在监听contextInitialized.................");
    }
}
//主启动类还是加@ServletComponentScan即可。
```

方式2：

```java
@Configuration
public class ListenerConfig {
    @Bean
    public ServletListenerRegistrationBean getServletListenerRegistrationBean() {
        ServletListenerRegistrationBean bean = new ServletListenerRegistrationBean(new MySecondListener());//注意传参
        return bean;
    }
}
```

## SpringBoot整合拦截器interceptor

**2020-6-6 22:20:46测试未成功**

```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //模拟登陆拦截
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            //无登录则拦截
            request.setAttribute("msg", "无权限，请先登录");
            request.getRequestDispatcher("/index.html").forward(request, response);
            return false;//不放行
        } else {
            return true;
        }
    }
}
```

