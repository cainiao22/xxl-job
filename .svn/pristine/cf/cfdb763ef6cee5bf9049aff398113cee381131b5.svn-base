package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobTriggerDependency;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XxlJobTriggerDependencyDao {
    int deleteByPrimaryKey(Long id);

    int insert(XxlJobTriggerDependency record);

    int insertSelective(XxlJobTriggerDependency record);

    XxlJobTriggerDependency selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(XxlJobTriggerDependency record);

    int updateByPrimaryKey(XxlJobTriggerDependency record);

    List<XxlJobTriggerDependency> getChildrenByDependencyJobLogId(int jobLogId);

    List<XxlJobTriggerDependency> getChildrenByDependencyJobId(@Param("jobId") int jobId, @Param("jobGroup") int jobGroup);

    List<XxlJobTriggerDependency> getByJobLogId(int jobLogId);
}