## 二、yaml语法详解

[(返回主目录)](https://github.com/MajorTooooom/SpringBoot)

对比xml文件，yml也具备属性文件的功能，但远不如此简单，yaml的功能非常强大。在module：`springboot-yaml`内建立`application.properties`和`application.yml`两个文件进行对比。

### yml文件存普通的key-value

| 功能  | springboot-yaml | application.properties    | 说明  |
|-------|:---:|-----------|-------|
| key-value  | name: 多罗罗丶 | name=多罗罗丶     | 单行K-V，注意yml中是有空格的 |

### yml文件存对象

树状写法：

```
student:
    name: 多罗罗丶
    age: 29
```

行内写法：
```
student: {name:多罗罗丶,age: 29}
```
### yml文件存数组

关键符号是`“-”`，树状写法是：
```
pets:
   - cat
   - dog
   - pig
```
行内写法：
```
pets: [cat,dog,pig]
```

#### 如何将yml中的属性值赋给具体的对象

测试方法：在module`springboot-yml`中创建类`pojo.dog`并添加`@Component`注解，使其会被扫描到，再创建一个`Person`类，目的是将dog注入到person中。
按照老Spring的做法是直接在dog的属性上添加`@value()`注解，比如：
```
public class Dog {
    @Value("旺财")
    private String name;
    
    private int age;
}
```
那么在测试类中就能直接读取到：

```
    @Autowired
    Dog dog;

    @Test
    void contextLoads() {
        System.out.println(dog);
    }
```
打印结果：Dog(name=旺财, age=29)

如果换成yml的方式就简洁很多，yml文件中如下配置：
```
person:
  name: 多罗罗丶
  age: 29
  happy: false
  birth: 2020/06/04
  map: {k1: v1,k2: v2}
  list:
    - code
    - music
    - girl
  dog:
    name: 旺财
    age: 3
```
然后在Person类中添加注解`@ConfigurationProperties(prefix = "")`:
```
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private int age;
    private boolean happy;
    private Date birth;
    private Map<String, Object> map;
    private List<Object> list;
    private Dog dog;
}
```
那么就能得到我们想要的结果：
```
    @Autowired
    Person person;

    @Test
    void contextLoads() {
        System.out.println(person);
    }
```
打印结果：
```
Person(name=多罗罗丶, age=29, happy=false, birth=Thu Jun 04 00:00:00 CST 2020, map={k1=v1, k2=v2}, list=[code, music, girl], dog=Dog(name=旺财, age=3))
```

拓展：![properties导致乱码问题解决方法](https://github.com/MajorTooooom/SpringBoot/blob/master/springboot-api/src/main/resources/static/images/properties%E6%96%87%E4%BB%B6%E4%B9%B1%E7%A0%81%E9%97%AE%E9%A2%98%E8%A7%A3%E5%86%B3%E6%96%B9%E6%B3%95.png)

## 三、JSR303校验

[(返回主目录)](https://github.com/MajorTooooom/SpringBoot)

SpringBoot中可以用@ validated来校验数据,如果数据异常则会统一抛出异常,方便异常中心统一处理。
JSR303是一种规范。举例理解：
在上面已经学会使用yml的属性注入了，如果我们在类的属性指定了属性值类型，则可以对数据进行校验。比如：
```
yml文件中：person: {email: 多罗罗丶}
实体类需要添加@Validated注解开启校验，对应的属性添加对应的校验规则：
@Validated
public class Person {
    @Email()
    private String email;
    //省略其他属性
}
```
由于`person: {email: 多罗罗丶}`并不符合`@Email()`的格式，那么运行后将报错：`ConfigurationPropertiesBindException`无法绑定。
报错的提示信息可以指定的：
```
@Validated
public class Person {
    @Email(message = "自定义报错信息")
    private String email;
    //省略其他属性
}
```

## 四、多环境配置

### 4.1多路径存放文件实现多环境配置

yml文件可以放在什么地方？根据官方文档，有四个位置(优先级从上往下)：
`项目路径下\config\application.yml`
`项目路径下的application.yml`
`\src\main\resources\config\application.yml`
`\src\main\resources\application.yml`

![yml文件可以放的位置.png]()

小结：

**SpringBoot给我们默认配置了优先级最低的yml文件，实际上是方面后期外部添加的yml文件能覆盖其他的从而达到更高的优先级**

### 4.2不同的文件后缀方式实现多环境配置

以上就是同名yml文件（application.yml）的覆盖达到多环境的目的。那么如果是`\src\main\resources\application.yml`的同级下呢？此时我们可以创建过个文件：

`\src\main\resources\application.yml`     ——server.port=9001

`\src\main\resources\application-test.yml`——假设测试环境希望使用这个文件,server.port=9002

`\src\main\resources\application-dev.yml` ——开发环境希望使用这个文件,server.port=9003

那么问题就是：我们如何切换？此时只需要在我们的`\src\main\resources\application.yml`下配置多环境参数即可：
```
spring:
  profiles:
    active: dev
```

如果是properties文件则类推：`spring.profiles.active=dev`,通过启动主启动类查看tomcat端口就能轻松测试出。

### 4.3同一个yml文件实现多环境配置

yml远不止以上的方法，甚至可以在同一个文件中实现多环境配置，关键是用符号是`---`间隔开：
```
server:
  port: 9001
spring:
  profiles:
    active: dev #修改这个参数就能轻松切换环境

---
server:
  port: 9002
spring:
  profiles: dev

---
server:
  port: 9003
spring:
  profiles: test
  
```