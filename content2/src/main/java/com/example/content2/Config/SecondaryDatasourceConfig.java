package com.example.content2.Config;


import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.example.content2.Mapper.Secondary",
        sqlSessionFactoryRef = "secondarySqlSessionFactory")
public class SecondaryDatasourceConfig {


    private static final String mapper_location = "classpath:mapper/Secondary/*.xml";

    @Value("${datasource.secondary.username}")
    private String username;

    @Value("${datasource.secondary.password}")
    private String password;

    @Value("${datasource.secondary.driver-class-name}")
    private String driverClassName;

    @Value("${datasource.secondary.url}")
    private String url;

    @Bean("secondaryDataSource")
    public DataSource secondaryDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean("secondaryDataSourceTransactionManager")
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(secondaryDataSource());
    }

    @Bean("secondarySqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("secondaryDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(mapper_location));
        return sqlSessionFactoryBean.getObject();
    }

}
