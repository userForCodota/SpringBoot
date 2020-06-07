# SpringBoot
<space><space><space><space>springboot详解


先说结论：
```
Spring Boot启动的时候会通过@EnableAutoConfiguration注解找到META-INF/spring.factories配置文件中的所有自动配置类，（但是不一定生效）并对其进行加载，
而这些自动配置类都是以AutoConfiguration结尾来命名的，它实际上就是一个JavaConfig形式的Spring容器配置类，它能通过以Properties结尾
命名的类中取得在全局配置文件中配置的属性如：server.port，而XxxxProperties类是通过@ConfigurationProperties注解与全局配置文件中对
应的属性进行绑定的。
```


## 一、SpringBoot-auto-assembly自动装配原理

从pom文件可以看出，SpringBoot的依赖关系是：

```
1.spring-boot-dependencies
    2.spring-boot-starter-parent
        3.SpringBoot-project(也就是我们创建的项目)
```
理解：可以发现我们添加的依赖不需要写版本号，是因为是从父级项目里面继承过来了，比如我们添加了`spring-boot-starter-web`这个依赖，点进去发现它是帮我们加载了web开发所必须和常用的依赖
，比如：`spring-boot-starter-tomcat`、`spring-webmvc`等；

同时也可以看出SpringBoot将一个个`场景`封装成了一个个`starter`。
`spring-boot-dependencies`中定义了大量的版本号，如果需要重新定义，需要在我们的项目pom文件中，例如：
```
<properties>
    <lombok.version>1.18.17</lombok.version>
</properties>
```
----------------------------------------------------------------------

### 注解详解
首先看一下层级关系([元注解](https://blog.csdn.net/lkp1603645756/article/details/84072600)就不列出了)：
```
@SpringBootApplication
    @SpringBootConfiguration            ---------------
        @Configuration                  ---------------
            @Component                  ---------------
    @EnableAutoConfiguration            ---------------
        @AutoConfigurationPackage       ---------------
            @Import                     ---------------
        @Import                         ---------------
    @ComponentScan                      ---------------该注解默认会扫描该类所在的包下所有的配置类，相当于之前的 <context:component-scan>，所以为什么package都在启动类同级目录就是这个原因
```
逐个理解：

首先我们发现`@Import(XXXX.class)`出现在多出，其含义是:
```
@Import(XXXX.class)
意思是如果某个类我只想在特殊的时刻才加载到容器中，那么我只需要那时候使用@Import注解即可，加了之后，这个类下面所有的@bean都会被加进去；
```
参考[《@Import注解》](https://blog.csdn.net/pange1991/article/details/81356594)

**@SpringBootConfiguration**

@SpringBootConfiguration下面有@Configuration.@Component，其中@Configuration的作用是定义配置类，这个用我们之前的xml文件就很好理解——
```
假设现在要定义数据库连接池c3p0,以往的做法是在spring-dao.xml文件中定义一个<bean></bean>，那么现在这个事情就交给@Configuration来完成了，只需要创建一个类假设@Configuration注解，里面
定义一个方法，加上@bean并且返回相对应的对象（这里例如c3p0的com.mchange.v2.c3p0.ComboPooledDataSource对象）就能把这个bean加载到Spring容器中。(记得把依赖加进去)
```
图解：

![@Configuration的作用](https://github.com/MajorTooooom/SpringBoot/blob/master/springboot-api/src/main/resources/static/images/@Configuration%E7%9A%84%E4%BD%9C%E7%94%A8.png?raw=true)

**@EnableAutoConfiguration**

直译过来就是`开启自动装配`的意思。

`@EnableAutoConfiguration`下面有2个注解：
`@Import(AutoConfigurationImportSelector.class)`和`@AutoConfigurationPackage`，

### 首先理解`@Import(AutoConfigurationImportSelector.class)`:

可以看出导入了`AutoConfigurationImportSelector`这个类，它的作用是运行其`selectImports()`方法（而这个方法是在 *run()* 方法里面被调用的），运行`selectImports()`的最终目的是将获取到候
选的配置，也就是我们在pom.xml文件中配了哪些依赖，然后结合属性文件中的属性进行了配置。具体的关键调用关系如下：
```
selectImports()
    getAutoConfigurationEntry(annotationMetadata);
        getCandidateConfigurations(annotationMetadata, attributes);
            Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you are using a custom packaging, make sure that file is correct.");//java断言机制，如果不符合就抛异常
            SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),getBeanClassLoader());
                loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);//此处前面做了判断，只要META-INF/spring.factories非空则加载；
```

### 再回头看 `@AutoConfigurationPackage`：

`@AutoConfigurationPackage`实际上就做了一件事情，`@Import(AutoConfigurationPackages.Registrar.class)`

-------------------------------------------------------------------------------
### 找到配置类后如何赋值

**首先一图了解** (*拼图导致字体较小的情况可下载原图查看*)

![找到配置类后如何赋值思路](https://github.com/MajorTooooom/SpringBoot/blob/master/images/%E6%89%BE%E5%88%B0%E9%85%8D%E7%BD%AE%E7%B1%BB%E5%90%8E%E5%A6%82%E4%BD%95%E8%B5%8B%E5%80%BC.png)

**详细的示例图参考** (*拼图导致字体较小的情况可下载原图查看*)

![找到配置类后如何赋值-实例详解](https://github.com/MajorTooooom/SpringBoot/blob/master/images/%E6%89%BE%E5%88%B0%E9%85%8D%E7%BD%AE%E7%B1%BB%E5%90%8E%E5%A6%82%E4%BD%95%E8%B5%8B%E5%80%BC-%E5%AE%9E%E4%BE%8B%E8%AF%A6%E8%A7%A3.png)

**下面是思路拆分**

到目前为止，我们知道SpringBoot的思路是将之前的xml文件变成了一个个javaConfig类，但是他是如何赋值的呢？之前的xml做法是(引入并)读取对应的properties文件，比如`jdbc.xml`读取的是`jdbc.properties`里面的属性，

![xml是如何读取properties文件的.png](https://github.com/MajorTooooom/SpringBoot/blob/master/images/xml%E6%98%AF%E5%A6%82%E4%BD%95%E8%AF%BB%E5%8F%96properties%E6%96%87%E4%BB%B6%E7%9A%84.png)

现在换成了javaConfig类之后，按照逻辑应该也是去读yml文件或者properties文件的，那么我们就要知道yml（或者properties）文件的属性是如何被读取的了，这个知识点在[README-yml语法.md](https://github.com/MajorTooooom/SpringBoot/blob/master/README-yaml%E8%AF%AD%E6%B3%95.md)中有详细说明，
顺着这个思路我们猜测一个`XXXXXAutoConfiguration.java`必然对应一个`XXXproperties`类，它告诉了我们配置类需要哪些属性，为了验证，我们去找到`spring-boot-autoconfigure-2.3.0.RELEASE.jar/META-INF/spring.factories`下的某个`XXXXXAutoConfiguration.java`，看看什么情况，这里我们找到一个简单的`HttpEncodingAutoConfiguration`来查看：

```
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ServerProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(CharacterEncodingFilter.class)
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)
public class HttpEncodingAutoConfiguration {
            //  省略主题内容
}
```

这里可以留意到`@EnableConfigurationProperties(ServerProperties.class)`传了`ServerProperties.class`这个类，点进去发现`ServerProperties`的属性就是`HttpEncodingAutoConfiguration`需要用的属性，而`ServerProperties.java`是被`@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)`注解修饰的，这个就是我们熟悉的知识点了，
我们知道它的作用是将yml(/properties)文件中地方属性读取出来，根据名字对应绑定的，至此，我们逆向思维得出了SpringBoot设计大体的思路就是：

1. 现在我们要使用某个组件，比如就是`HttpEncodingAutoConfiguration`，我们希望将encoding都设置成UTF-8；
2. 之前是xml导入properties文件，现在`HttpEncodingAutoConfiguration`利用注解导入了`ServerProperties.java`文件；
3. `ServerProperties.java`文件由`@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)`注解修饰，使得`ServerProperties.java`能读取到yml配置的属性值，也是就我们自己写的值；
4. 至此，`ServerProperties.java`+`yml`=`properties`，传递给`javaConfig`（也就是之前的`xml`）;

另外我们还看到`XXXXAutoConfiguration`类被三个`@ConditionalOnXXXX.....`注解进行修饰，他们的作用是判断这个`XXXXAutoConfiguration`配置类在什么情况下进行生效，

```
@ConditionalOnWebApplication()  ------------------------是否在应用层面满足对应条件？不满足则此配置类不生效
@ConditionalOnClass()           ------------------------是否在类层面满足对应条件？  不满足则此配置类不生效
@ConditionalOnProperty()        ------------------------是否在属性层面满足对应条件？不满足则此配置类不生效

    >>>>>>>>>>>以上三个条件都满足才生效
```

在看回`HttpEncodingAutoConfiguration`就很好理解：
```
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)                          ---程序类型是web才生效
@ConditionalOnClass(CharacterEncodingFilter.class)                                                     ---是否存在CharacterEncodingFilter.java不存在则此配置类不生效
@ConditionalOnProperty(prefix = "server.servlet.encoding", value = "enabled", matchIfMissing = true)   ---配置文件(yml)中是否能读取到对应的属性和属性值？没有的话这个配置类也不生效
public class HttpEncodingAutoConfiguration {
    //  省略主题内容
}
```
([@ConditionalOnXXX的扩展可以看这里](https://cloud.tencent.com/developer/article/1490442))

**至此，我们对SpringBoot的自动装配有了加深的了解，了解原理不单是面试时候的作用，对我们日常工作开发更是事半功倍的作用**

>作为Spring Boot的精髓，自动配置原理的工作过程往往只有在“面试”的时候才能用得上，但是如果在工作中你能够深入的理解Spring Boot的自动配置原理，将无往不利。

-------------------------------------------------------------------------------

以上笔记参考了博客[《Spring Boot面试杀手锏————自动配置原理》](https://blog.csdn.net/u014745069/article/details/83820511)）,参考视频：[【狂神说Java】](https://www.bilibili.com/video/BV1PE411i7CV)，
结合了前辈们的经验总结出的个人的学习过程的理解。

-------------------------------------------------------------------------------


# 其他板块

| 板块名称  | 板块地址 | 板块说明    | 其他  |
|-------|:---:|-----------|-------|
| yaml语法详解  |[README-yml语法.md](https://github.com/MajorTooooom/SpringBoot/blob/master/README-yml%E8%AF%AD%E6%B3%95.md)  | yml存储属性的多种方式；<br/>yml文件的属性是如何被取出来的;<br/>properties文件的属性是如何被出去来的；<br/>如何配置多环境；<br/>JSR303校验了解；     |  |
|SpringBoot开发web项目的基本注意事项|[README-SpringBootWeb.md](doc-开发Web项目需要解决的问题.md)|1)静态资源问题;<br/>2)首页如何放置;<br/>3)SpringMVC配置原理;<br/>4)||

finally

![](https://raw.githubusercontent.com/MajorTooooom/markdownImages/master/img/20200607122948.gif)