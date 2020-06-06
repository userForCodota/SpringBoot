# SpringBoot Web开发

使用SpringBoot开发Web引用，虽然SpringBoot帮我们做了大量的自动配置，我们仍然要解决的问题是：
1. **静态资源导入的问题**

2. 首页的问题

3. 如何使用jsp的问题、模板引擎

4. 装配扩展SpringMVC

5. 拦截器、过滤器、监听器

6. 国际化

..........



## 静态资源问题

**分析思路**

找到对应的配置类`WebMvcAutoConfiguration`，其他有一个方法`addResourceHandlers()`，这个方法就是SpringMVC处理静态资源的，查看方法内容发现有3个if方法：

**第1个if**：如果自己自定义了比如`spring.mvc.static-path-pattern:/chengfeng/**`，那么系统默认的静态资源路径将会失效（基本不会这么干）；

**第2个if**：webjar的形式将资源导入，作了映射；

**第3个if**：追溯到`ResourceProperties.CLASSPATH_RESOURCE_LOCATIONS`可以看出，`/**`下的请求会去以下位置找资源，也就是说资源可以放在以下目录：

{ "classpath:/META-INF/resources/","classpath:/resources/", "classpath:/static/", "classpath:/public/" }

比如现在请求一个静态资源`localhost:8080/test.js`，根据源码，它会去上面四个路径找`test.js`这个资源，假设四个位置都有这个资源呢？我们可以测试一下他们的优先级：

![静态资源目录的位置和优先级]()
