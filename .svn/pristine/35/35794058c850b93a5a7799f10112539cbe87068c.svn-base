package com.xxl.job.admin.service;

import com.xxl.job.admin.core.enums.JobTriggerTypeEnum;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.model.XxlJobTriggerDependency;

import java.util.List;

/**
 * @author yanpf
 * @date 2017/12/1
 * @description 保存任务的依赖信息以及状态信息。避免线程安全
 */
public interface XxlJobLogDepService {

    List<XxlJobTriggerDependency> getChildrenByDependencyJobLogId(int parentJobLogId);

    /**
     * 保存当前任务和子任务的依赖关系
     *
     * @param childJobLogId
     * @param jobLog
     */
    boolean saveRelationShap(int childJobLogId, XxlJobLog jobLog, JobTriggerTypeEnum triggerTypeEnum);
}
