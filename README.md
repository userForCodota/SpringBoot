# SpringBoot
<space><space><space><space>springboot详解

先说结论（参考了很详细的博客[《Spring Boot面试杀手锏————自动配置原理》](https://blog.csdn.net/u014745069/article/details/83820511)）：
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
# 其他板块

| 板块名称  | 板块地址 | 板块说明    | 其他  |
|-------|:---:|-----------|-------|
| yaml语法详解  |[path_yaml语法.md]()  | yml是如何存属性和取出属性的     |  |
| JSR303校验  |[path_JSR303校验.md]()  |      |  |
