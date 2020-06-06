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

![静态资源目录的位置和优先级](https://github.com/MajorTooooom/SpringBoot/blob/master/images/%E9%9D%99%E6%80%81%E8%B5%84%E6%BA%90%E7%9B%AE%E5%BD%95%E7%9A%84%E4%BD%8D%E7%BD%AE%E5%92%8C%E4%BC%98%E5%85%88%E7%BA%A7.png)

 ## 首页和图标定制
 
 ### 首页如何放置
 **思路**
 
没有了`web.xml`后是由`WebMvcAutoConfiguration`来接管关于web的配置的，所以理论上也是去`WebMvcAutoConfiguration`找关于首页配置的内容，查看源码发现`welcomePageHandlerMapping()`方法说明了
在`/**`目录下去找**index.html**，所以我们只要把首页放在对应的位置即可，一般是放在`public`目录下。

此时直接访问**localhost:8080**或者**localhost:8080/index.html**都可以。注意：

如果`index.html`是放在了templates目录下，则只能通过controller去跳转；且需要添加`spring-boot-starter-thymeleaf`依赖；

### 图标定制

高版本的SpringBoot取消掉了；

## thymeleaf模板引擎

理解jsp和Vue就能理解thymeleaf；个人觉得有必要再学；感觉目前老项目还是jsp居多，新项目是Vue+ElementUI居多；

## SpringMVC配置原理

查阅官方文档的说明，如果想扩展MVC的配置，那么我们只需要配置一个配置类并且类型为**WebMvcConfigurer**，它是一个接口我们实现它即可；这里我们以模块`SpringMVC配置原理`下的`springboot-web\src\main\java\com\data\config\MyMvcConfig.java`为例子进行测试：

```
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    //重写的内容就是我们想要自定义的内容
}
```

    

