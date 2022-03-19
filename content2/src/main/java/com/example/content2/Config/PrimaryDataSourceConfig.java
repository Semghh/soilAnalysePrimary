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
@MapperScan(basePackages = "com.example.content2.Mapper.Primary",
            sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDataSourceConfig {

    private static final String mapper_location = "classpath:mapper/Primary/*.xml";

    @Value("${datasource.primary.username}")
    private String username;

    @Value("${datasource.primary.password}")
    private String password;

    @Value("${datasource.primary.driver-class-name}")
    private String driverClassName;

    @Value("${datasource.primary.url}")
    private String url;

    @Bean("primaryDataSource")
    @Primary
    public DataSource primaryDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    @Bean("primaryDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(primaryDataSource());
    }

        @Bean("primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("primaryDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver() //路径资源匹配解析器
                .getResources(mapper_location));
        return sqlSessionFactoryBean.getObject();
    }
}
