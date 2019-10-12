package com.xxl.job.executor.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author yanpf
 * @date 2017/12/27
 * @description
 */

@Configuration
@MapperScan(basePackages = "com.xxl.job.executor.mapper.ds", sqlSessionFactoryRef = "dsSqlSessionFactory")
public class DsDataSourceConfig {

    @Value("${ds.db.driver}")
    private String dbDriver;
    @Value("${ds.db.url}")
    private String dbUrl;
    @Value("${ds.db.username}")
    private String dbUser;
    @Value("${ds.db.password}")
    private String dbPassword;

    @Bean(name = "dsDataSource")
    @Primary
    public DataSource dsDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean(name = "dsTransactionManager")
    @Primary
    public DataSourceTransactionManager rdsTransactionManager() {
        return new DataSourceTransactionManager(dsDataSource());
    }

    @Bean(name = "dsSqlSessionFactory")
    @Primary
    public SqlSessionFactory rdsSqlSessionFactory(@Qualifier("dsDataSource") DataSource rdsDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(rdsDataSource);
        return sessionFactory.getObject();
    }
}
