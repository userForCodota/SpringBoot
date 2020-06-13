package com.data;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringbootJdbcDruidMybatisApplicationTests {
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

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void simpleJdbcTemplate() {
        //简化后,数据源的配置信息写在了配置文件，直接被JdbcTemplate使用
        String sql = "SELECT id,title,time FROM movies LIMIT 0,10";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println(JSON.toJSONString(list));
    }
}
