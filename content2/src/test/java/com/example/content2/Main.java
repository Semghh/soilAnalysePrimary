package com.example.content2;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;

public class Main {

    @Test
    public void test1() {
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
        System.out.println(1231);
    }


    @Test
    public void test2(){
        String s = "98889*44*100";
        String[] split = s.split("\\D");
        System.out.println(Arrays.toString(split));

    }

    @Test
    public void test3(){
        String str = "123456abc";
        String[] split = str.split("\\d*");
        System.out.println(Arrays.toString(split));
    }
    @Test
    public void test4(){
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
    }
    @Test
    public void test5() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        /**
         *     driver-class-name: com.mysql.cj.jdbc.Driver
         *     url: jdbc:mysql://localhost:3306/content2?useSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
         *     username: root
         *     password: zxc,./123
         */
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/content2?useSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8");
        dataSource.setUsername("root");
        dataSource.setPassword("zxc,./123");
        DruidPooledConnection connection = dataSource.getConnection();
        long start = System.currentTimeMillis();
        for (long i = 0; i <1000000-7393; i++) {
            String sql = "insert into regiontest(`longitude`,`latitude`) values(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1,Math.random()*150);
            preparedStatement.setDouble(2,Math.random()*150);
            preparedStatement.executeUpdate();
        }
        long end  = System.currentTimeMillis();
        System.out.println(end-start);
    }

}
