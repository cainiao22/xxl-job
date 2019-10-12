package com.xxl.job.executor.mapper.ds;

import com.xxl.job.executor.model.DsMonitorResult;
import com.xxl.job.executor.model.PageModel;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

public class DsMonitorResultSqlProvider {

    public String insertSelective(DsMonitorResult record) {
        BEGIN();
        INSERT_INTO("ds_monitor_result");
        
        if (record.getId() != null) {
            VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getMonitorRuleId() != null) {
            VALUES("monitor_rule_id", "#{monitorRuleId,jdbcType=BIGINT}");
        }
        
        if (record.getResultValue() != null) {
            VALUES("result_value", "#{resultValue,jdbcType=DOUBLE}");
        }
        
        if (record.getOffsetDay() != null) {
            VALUES("offset_day", "#{offsetDay,jdbcType=INTEGER}");
        }
        
        if (record.getScheduleTime() != null) {
            VALUES("schedule_time", "#{scheduleTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateTime() != null) {
            VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(DsMonitorResult record) {
        BEGIN();
        UPDATE("ds_monitor_result");
        
        if (record.getMonitorRuleId() != null) {
            SET("monitor_rule_id = #{monitorRuleId,jdbcType=BIGINT}");
        }
        
        if (record.getResultValue() != null) {
            SET("result_value = #{resultValue,jdbcType=DOUBLE}");
        }
        
        if (record.getOffsetDay() != null) {
            SET("offset_day = #{offsetDay,jdbcType=INTEGER}");
        }
        
        if (record.getScheduleTime() != null) {
            SET("schedule_time = #{scheduleTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getCreateTime() != null) {
            SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        WHERE("id = #{id,jdbcType=BIGINT}");
        
        return SQL();
    }

    public String query(DsMonitorResult record, PageModel page){
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ds_monitor_result ")
                .append("where 1=1");
        if (record.getId() != null) {
            sb.append(" and id=#{id}");
        }
        if (record.getMonitorRuleId() != null) {
            sb.append(" and monitor_rule_id=#{monitorRuleId,jdbcType=BIGINT}");
        }

        if (record.getResultValue() != null) {
            sb.append(" and result_value=#{resultValue,jdbcType=DOUBLE}");
        }

        if (record.getOffsetDay() != null) {
            sb.append(" and offset_day=#{offsetDay,jdbcType=INTEGER}");
        }

        if (record.getScheduleTime() != null) {
            sb.append(" and schedule_time=#{scheduleTime,jdbcType=TIMESTAMP}");
        }

        if (record.getCreateTime() != null) {
            sb.append(" and create_time=#{createTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdateTime() != null) {
            sb.append("and update_time=#{updateTime,jdbcType=TIMESTAMP}");
        }

        sb.append(" order by update_time desc ");
        if(page != null) {
            sb.append(" offset #{pageOffset} limit #{pageSize} ");
        }

        return sb.toString();
    }
}