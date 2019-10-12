package com.xxl.job.executor.mapper.ds;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.xxl.job.executor.model.DsMonitorRules;

public class DsMonitorRulesSqlProvider {

    public String insertSelective(DsMonitorRules record) {
        BEGIN();
        INSERT_INTO("ds_monitor_rules");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getMonitorName() != null) {
            VALUES("monitor_name", "#{monitorName,jdbcType=CHAR}");
        }
        
        if (record.getRuleType() != null) {
            VALUES("rule_type", "#{ruleType,jdbcType=CHAR}");
        }
        
        if (record.getOffsetMin() != null) {
            VALUES("offset_min", "#{offsetMin,jdbcType=DOUBLE}");
        }
        
        if (record.getOffestMax() != null) {
            VALUES("offest_max", "#{offestMax,jdbcType=DOUBLE}");
        }
        
        if (record.getOffsetDay() != null) {
            VALUES("offset_day", "#{offsetDay,jdbcType=INTEGER}");
        }
        
        if (record.getOwner() != null) {
            VALUES("owner", "#{owner,jdbcType=CHAR}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getParameters() != null) {
            VALUES("parameters", "#{parameters,jdbcType=LONGVARCHAR}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(DsMonitorRules record) {
        BEGIN();
        UPDATE("ds_monitor_rules");
        
        if (record.getMonitorName() != null) {
            SET("monitor_name = #{monitorName,jdbcType=CHAR}");
        }
        
        if (record.getRuleType() != null) {
            SET("rule_type = #{ruleType,jdbcType=CHAR}");
        }
        
        if (record.getOffsetMin() != null) {
            SET("offset_min = #{offsetMin,jdbcType=DOUBLE}");
        }
        
        if (record.getOffestMax() != null) {
            SET("offest_max = #{offestMax,jdbcType=DOUBLE}");
        }
        
        if (record.getOffsetDay() != null) {
            SET("offset_day = #{offsetDay,jdbcType=INTEGER}");
        }
        
        if (record.getOwner() != null) {
            SET("owner = #{owner,jdbcType=CHAR}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getParameters() != null) {
            SET("parameters = #{parameters,jdbcType=LONGVARCHAR}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }
}