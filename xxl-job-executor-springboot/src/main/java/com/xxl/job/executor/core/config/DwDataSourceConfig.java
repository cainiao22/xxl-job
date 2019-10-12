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
@MapperScan(basePackages = "com.xxl.job.executor.mapper.dw", sqlSessionFactoryRef = "dwSqlSessionFactory")
public class DwDataSourceConfig {

    @Value("${dw.db.driver}")
    private String dbDriver;
    @Value("${dw.db.url}")
    private String dbUrl;
    @Value("${dw.db.username}")
    private String dbUser;
    @Value("${dw.db.password}")
    private String dbPassword;

    @Bean(name = "dwDataSource")
    public DataSource dwDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean(name = "dwTransactionManager")
    public DataSourceTransactionManager rdsTransactionManager() {
        return new DataSourceTransactionManager(dwDataSource());
    }

    @Bean(name = "dwSqlSessionFactory")
    public SqlSessionFactory rdsSqlSessionFactory(@Qualifier("dwDataSource") DataSource rdsDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(rdsDataSource);
        return sessionFactory.getObject();
    }
}
