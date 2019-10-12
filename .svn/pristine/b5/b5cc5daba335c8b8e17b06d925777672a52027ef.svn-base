package com.xxl.job.executor.mapper.ds;

import com.xxl.job.executor.model.DsMonitorRules;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface DsMonitorRulesMapper {
    @Delete({
        "delete from ds_monitor_rules",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into ds_monitor_rules (id, monitor_name, ",
        "rule_type, offset_min, ",
        "offest_max, offset_day, ",
        "owner, create_time, ",
        "update_time, parameters)",
        "values (#{id,jdbcType=BIGINT}, #{monitorName,jdbcType=CHAR}, ",
        "#{ruleType,jdbcType=CHAR}, #{offsetMin,jdbcType=DOUBLE}, ",
        "#{offestMax,jdbcType=DOUBLE}, #{offsetDay,jdbcType=INTEGER}, ",
        "#{owner,jdbcType=CHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{updateTime,jdbcType=TIMESTAMP}, #{parameters,jdbcType=LONGVARCHAR})"
    })
    int insert(DsMonitorRules record);

    @InsertProvider(type=DsMonitorRulesSqlProvider.class, method="insertSelective")
    int insertSelective(DsMonitorRules record);

    @Select({
        "select",
        "id, monitor_name, rule_type, offset_min, offest_max, offset_day, owner, create_time, ",
        "update_time, parameters",
        "from ds_monitor_rules",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="monitor_name", property="monitorName", jdbcType=JdbcType.CHAR),
        @Result(column="rule_type", property="ruleType", jdbcType=JdbcType.CHAR),
        @Result(column="offset_min", property="offsetMin", jdbcType=JdbcType.DOUBLE),
        @Result(column="offest_max", property="offestMax", jdbcType=JdbcType.DOUBLE),
        @Result(column="offset_day", property="offsetDay", jdbcType=JdbcType.INTEGER),
        @Result(column="owner", property="owner", jdbcType=JdbcType.CHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="parameters", property="parameters", jdbcType=JdbcType.LONGVARCHAR)
    })
    DsMonitorRules selectByPrimaryKey(Long id);

    @UpdateProvider(type=DsMonitorRulesSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(DsMonitorRules record);

    @Update({
        "update ds_monitor_rules",
        "set monitor_name = #{monitorName,jdbcType=CHAR},",
          "rule_type = #{ruleType,jdbcType=CHAR},",
          "offset_min = #{offsetMin,jdbcType=DOUBLE},",
          "offest_max = #{offestMax,jdbcType=DOUBLE},",
          "offset_day = #{offsetDay,jdbcType=INTEGER},",
          "owner = #{owner,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "parameters = #{parameters,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(DsMonitorRules record);

    @Update({
        "update ds_monitor_rules",
        "set monitor_name = #{monitorName,jdbcType=CHAR},",
          "rule_type = #{ruleType,jdbcType=CHAR},",
          "offset_min = #{offsetMin,jdbcType=DOUBLE},",
          "offest_max = #{offestMax,jdbcType=DOUBLE},",
          "offset_day = #{offsetDay,jdbcType=INTEGER},",
          "owner = #{owner,jdbcType=CHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DsMonitorRules record);
}