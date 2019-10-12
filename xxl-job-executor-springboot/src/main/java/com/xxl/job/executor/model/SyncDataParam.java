package com.xxl.job.executor.model;

import java.io.Serializable;

/**
 * @author yanpf
 * @date 2018/1/22
 * @description
 */
public class SyncDataParam implements Serializable {

    /**
     * 数据库类型，mysql sqlServer
     */
    private String dbType;

    /**
     * 数据库连接
     */
    private String jdbcUrl;

    private String username;

    private String password;
    /**
     * 目标表名，带着schema
     */
    private String tableName;

    /**
     * 删除目标表的元数据
     */
    private Integer delTargetTable;

    /**
     * 查询语句
     */
    private String sql;

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Integer getDelTargetTable() {
        return delTargetTable;
    }

    public void setDelTargetTable(Integer delTargetTable) {
        this.delTargetTable = delTargetTable;
    }
}
