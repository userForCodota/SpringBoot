# SpringBoot整合JDBC

关键依赖：

```xml
<!--jdbc依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<!--MySQL依赖-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

 关键配置：

```yaml
server:
  port: 4567
spring:
  datasource:
    username: root
    password: root
    url: jdbc:mysql://localhost:3306/academic_research?useUnicode=true&characterEncoding=uft-8
    driver-class-name: com.mysql.cj.jdbc.Driver
```

以上准备工作完成后，我们就可以开始测试了，**正常的JDBC操作**例如：

```java
@Autowired
DataSource dataSource;

@Test
void contextLoads() throws SQLException {
    //查看一下
    System.out.println(dataSource.getClass());
    //SQL
    String sql = "SELECT id,title,time FROM movies LIMIT 0,10";
    /*********************************【这些步骤都可以被JdbcTemplate简化】************************************/

    //获取数据源
    Connection conn = dataSource.getConnection();
    PreparedStatement pst = conn.prepareStatement(sql);
    //步骤
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println(resultSet.getInt("id"));
        System.out.println(resultSet.getString("title"));
        System.out.println(resultSet.getString("time"));
        //..............
    }
    conn.close();
    /********************************************************************************************************/
}
```

那么利用**JdbcTemplate**进行简化之后：

```java
@Autowired
JdbcTemplate jdbcTemplate;

@Test
void simpleJdbcTemplate() {
    //简化后,数据源的配置信息写在了配置文件，直接被JdbcTemplate使用
    String sql = "SELECT id,title,time FROM movies LIMIT 0,10";
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    System.out.println(JSON.toJSONString(list));
}
```

打印结果：

```
[{"id":1,"title":"《名字之歌》","time":"　2019-09-08(多伦多电影节) / 2019-12-25(美国)"},{"id":2,"title":"《我仍然相信》","time":"　2020-03-13(美国)"},{"id":3,"title":"《阿肯色》"},{"id":4,"title":"《惊天营救/极限营救》","time":"　2020-04-24(美国)"},{"id":5,"title":"《狩猎的时间》","time":"　2020-02-22(柏林电影节) / 2020-04-23(韩国)"},{"id":6,"title":"《7号房的礼物》","time":"　2019-10-11(土耳其)"},{"id":7,"title":"《看不见的女人》","time":"　2019-05-20(戛纳电影节)"},{"id":8,"title":"《我们永不言弃》"},{"id":9,"title":"《军人的妻子》","time":"　2019-09-06(多伦多电影节) / 2020-03-06(英国)"},{"id":10,"title":"《匹诺曹/木偶奇遇记》","time":"　2019-12-19(意大利)"}]
```



**优点**：只需要关注业务，繁琐的工作都内封装了。



# SpringBoot整合Druid数据源

上面实现了jdbc的整合，但是真实开发中是不会这么干的，还是常规的`MyBatis`+`优秀数据库连接池框架`结合，例如Druid，下面看看整合Druid数据库连接池。

```xml
<!--Druid数据库连接池-->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.21</version>
</dependency>
```

关键改动：

```yaml
spring:
  datasource:
	#省略其他...............
    #当用SpringBoot来管理 datasource时，只需要将数据源的类型指定为目标框架（比如Druid）即可
    type: com.alibaba.druid.pool.DruidDataSource
```

指定之后就是SpringBoot的常规三部曲：

1. **@Configuration加上@Bean的方式得到我们要用的主键**
2. **该组件是通过@ConfigurationProperties(prefix = "")将yml文件的属性读取**
3. **主键配置好了之后就能在其他地方调用了，比如JdbcTemplate**



# SpringBoot整合MyBatis

添加依赖（MyBatis写的不会SpringBoot官方的）：

```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.3</version>
</dependency>
```

在MyBatis的GitHub找到了他们的[文档](https://github.com/mybatis/spring-boot-starter/blob/master/mybatis-spring-boot-autoconfigure/src/site/markdown/index.md)。

## 定义mapper类的2种方式

1. 接口类加@Mapper表示其实MyBatis的mapper类（加上@Repository用于Spring管理）；
2. 主启动类加@MapperScan(包名)去扫；



有了mapper接口类之后，我们把对应的xml文件们都写在resources目录下的mapper文件夹，那么现在的问题就是怎么告诉Spring去找到这些xml文件，就需要在yml文件里面配置相关：

```yaml
mybatis:
  type-aliases-package: com.data.mapper
  mapper-locations: classpath:mybatis/mapper/*.xml
```

然后就能在xml文件里面写对应的sql语句了，这里就不展开了。

 

扩展：

不需要xml文件，直接在接口方法上面写sql语句：

```java
@Mapper
@Repository
public interface MovieMapper {
    //增删改查
    @Select("SELECT id,title,time FROM movies LIMIT 0,10")
    List<Movie> queryMovies();
}
```

在controller（或service）中被调用时效果也是一样的：

```java
@RestController
public class MovieController {
    @Autowired
    MovieMapper movieMapper;

    @RequestMapping("/all")
    public List<Movie> queryMovies() {
        List<Movie> movies = movieMapper.queryMovies();
        return movies;
    }
}
```

```java
[{"id":1,"title":"《名字之歌》","time":"　2019-09-08(多伦多电影节) / 2019-12-25(美国)"},{"id":2,"title":"《我仍然相信》","time":"　2020-03-13(美国)"},{"id":3,"title":"《阿肯色》","time":null},{"id":4,"title":"《惊天营救/极限营救》","time":"　2020-04-24(美国)"},{"id":5,"title":"《狩猎的时间》","time":"　2020-02-22(柏林电影节) / 2020-04-23(韩国)"},{"id":6,"title":"《7号房的礼物》","time":"　2019-10-11(土耳其)"},{"id":7,"title":"《看不见的女人》","time":"　2019-05-20(戛纳电影节)"},{"id":8,"title":"《我们永不言弃》","time":null},{"id":9,"title":"《军人的妻子》","time":"　2019-09-06(多伦多电影节) / 2020-03-06(英国)"},{"id":10,"title":"《匹诺曹/木偶奇遇记》","time":"　2019-12-19(意大利)"}]
```

