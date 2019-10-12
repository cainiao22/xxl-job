package com.xxl.job.executor.mapper.ds;

import com.xxl.job.executor.model.DsMonitorResult;
import com.xxl.job.executor.model.DsMonitorRules;
import com.xxl.job.executor.model.PageModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface DsMonitorResultMapper {
    @Delete({
        "delete from ds_monitor_result",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ds_monitor_result (id, monitor_rule_id, ",
        "result_value, offset_day, ",
        "schedule_time, create_time, ",
        "update_time)",
        "values (#{id,jdbcType=BIGINT}, #{monitorRuleId,jdbcType=BIGINT}, ",
        "#{resultValue,jdbcType=DOUBLE}, #{offsetDay,jdbcType=INTEGER}, ",
        "#{scheduleTime,jdbcType=TIMESTAMP}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(DsMonitorResult record);

    @InsertProvider(type=DsMonitorResultSqlProvider.class, method="insertSelective")
    int insertSelective(DsMonitorResult record);

    @Select({
        "select",
        "id, monitor_rule_id, result_value, offset_day, schedule_time, create_time, update_time",
        "from ds_monitor_result",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="monitor_rule_id", property="monitorRuleId", jdbcType=JdbcType.BIGINT),
        @Result(column="result_value", property="resultValue", jdbcType=JdbcType.DOUBLE),
        @Result(column="offset_day", property="offsetDay", jdbcType=JdbcType.INTEGER),
        @Result(column="schedule_time", property="scheduleTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    DsMonitorResult selectByPrimaryKey(Long id);

    @UpdateProvider(type=DsMonitorResultSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DsMonitorResult record);

    @Update({
        "update ds_monitor_result",
        "set monitor_rule_id = #{monitorRuleId,jdbcType=BIGINT},",
          "result_value = #{resultValue,jdbcType=DOUBLE},",
          "offset_day = #{offsetDay,jdbcType=INTEGER},",
          "schedule_time = #{scheduleTime,jdbcType=TIMESTAMP},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DsMonitorResult record);

    @Select({
            "select",
            "id, monitor_rule_id, result_value, offset_day, schedule_time, create_time, ",
            "update_time",
            "from ds_monitor_result",
            "where monitor_rule_id = #{id,jdbcType=BIGINT}",
            "and offset_day=#{offsetDay}",
            "order by update_time desc limit 1"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="monitor_rule_id", property="monitorRuleId", jdbcType=JdbcType.BIGINT),
            @Result(column="result_value", property="resultValue", jdbcType=JdbcType.DOUBLE),
            @Result(column="offset_day", property="offsetDay", jdbcType=JdbcType.INTEGER),
            @Result(column="schedule_time", property="scheduleTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    DsMonitorResult getLastExecuteResult(DsMonitorRules dsMonitorRules);

    @SelectProvider(type = DsMonitorResultSqlProvider.class, method = "query")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="monitor_rule_id", property="monitorRuleId", jdbcType=JdbcType.BIGINT),
            @Result(column="result_value", property="resultValue", jdbcType=JdbcType.DOUBLE),
            @Result(column="offset_day", property="offsetDay", jdbcType=JdbcType.INTEGER),
            @Result(column="schedule_time", property="scheduleTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<DsMonitorResult> query(DsMonitorResult record, PageModel page);
}