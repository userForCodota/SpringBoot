# SpringBoot整合安全框架SpringSecurity

官网文档链接[LINK](https://spring.io/projects/spring-security)

## 学习思路



## 准备工作

导入依赖

```xml
<!--安全框架SpringSecurity-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

如果页面是thymeleaf框架，则需要整合包**thymeleaf-extras-springsecurity4**且SpringBoot的版本不能太高（降到2.0.9）：

```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity4</artifactId>
    <version>3.0.4.RELEASE</version>
</dependency>
```

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.9.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```

正式开发不一定是用thymeleaf，可能还是JSP（SpringSecurity的官网演示也是用JSP......................）；

编写配置类（并开启服务）

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    //这里的内容很重要，后面详细讲
}
```

*下面将学习安全框架的2大功能是如何实现的。*

## 用户认证（Authentication）和用户授权（Authorization）









