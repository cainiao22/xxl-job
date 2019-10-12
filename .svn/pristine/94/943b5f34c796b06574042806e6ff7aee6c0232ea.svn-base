package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.enums.JobStatusEnum;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.model.XxlJobTriggerDependency;
import com.xxl.job.admin.core.model.XxlJobTriggerHistory;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.core.trigger.XxlJobTrigger;
import com.xxl.job.admin.dao.*;
import com.xxl.job.admin.service.XxlJobLogDepService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public XxlJobLogDao xxlJobLogDao;
    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobLogDepService xxlJobLogDepService;
    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobRegistryDao xxlJobRegistryDao;
    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobTriggerDependencyDao xxlJobTriggerDependencyDao;

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobTriggerHistoryDao xxlJobTriggerHistoryDao;

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam: callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.info("JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode()==ReturnT.SUCCESS_CODE?"success":"fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        XxlJobLog log = xxlJobLogDao.load(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }
        saveJobHistoryLog(log, handleCallbackParam.getExecuteResult());
        //正在运行的话保存流水日志后退出
        if(handleCallbackParam.getExecuteResult().getCode() == ReturnT.RUNNING_CODE){
            return ReturnT.SUCCESS;
        }
        List<XxlJobTriggerDependency> jobTriggerDependencyList = xxlJobTriggerDependencyDao.getByJobLogId(log.getId());
        if(jobTriggerDependencyList != null){
            for (XxlJobTriggerDependency jobTriggerDependency : jobTriggerDependencyList) {
                jobTriggerDependency.setJobStatus(handleCallbackParam.getExecuteResult().getCode() == ReturnT.SUCCESS_CODE ? JobStatusEnum.SUCCEED.name() : JobStatusEnum.FAILED.name());
                xxlJobTriggerDependencyDao.updateByPrimaryKey(jobTriggerDependency);
            }
        }
        // trigger success, to trigger child job, and avoid repeat trigger child job
        String childTriggerMsg = null;
        //确保手动触发的可以回调回来，自动触发的可以容忍在下一次回调
        List<XxlJobTriggerDependency> childTriggerJobs = xxlJobLogDepService.getChildrenByDependencyJobLogId(log.getId());
        List<XxlJobTriggerDependency> autoChildTriggerJobs = xxlJobTriggerDependencyDao.getChildrenByDependencyJobId(log.getJobId(), log.getJobGroup());
        if(childTriggerJobs == null){
            childTriggerJobs = new ArrayList<>();
        }
        if(autoChildTriggerJobs == null){
            autoChildTriggerJobs = new ArrayList<>();
        }
        childTriggerJobs.addAll(autoChildTriggerJobs);
        if (ReturnT.SUCCESS_CODE==handleCallbackParam.getExecuteResult().getCode() && ReturnT.SUCCESS_CODE!=log.getHandleCode()) {
            if (CollectionUtils.isNotEmpty(childTriggerJobs)) {
                for (int i = 0; i < childTriggerJobs.size(); i++) {
                    XxlJobTriggerDependency childJob = childTriggerJobs.get(i);
                    if (childJob.getJobLogId() != null) {
                        try {
                            JobDataMap jobDataMap = new JobDataMap();
                            jobDataMap.put(XxlJobTrigger.PRE_JOBLOG_ID, String.valueOf(childJob.getJobLogId()));
                            boolean ret = XxlJobDynamicScheduler.triggerJob(String.valueOf(childJob.getJobId()), String.valueOf(childJob.getGroupId()), jobDataMap);
                            // add msg
                            childTriggerMsg += MessageFormat.format("<br> {0}/{1} 触发子任务成功, 子任务Key: {2}, status: {3}, 子任务描述: {4}",
                                    (i+1), childJob.getGroupId(), childJob.getJobId(), ret, childJob.getJobLogId());
                        } catch (SchedulerException e) {
                            logger.error("{}", e);
                        }
                        childJob.setJobStatus(JobStatusEnum.PENDING.name());
                        xxlJobTriggerDependencyDao.updateByPrimaryKey(childJob);
                    } else {
                        childTriggerMsg += MessageFormat.format("<br> {0}/{1} 触发子任务失败, 子任务xxlJobInfo不存在, 子任务logKey: {2}",
                                (i+1), childJob.getJobId(), childJob.getJobLogId());
                    }
                }

            }
            logger.debug(childTriggerMsg);
        }else {
            //任务执行失败了
            if(CollectionUtils.isNotEmpty(childTriggerJobs)) {
                for (XxlJobTriggerDependency childTriggerJob : childTriggerJobs) {
                    childTriggerJob.setJobStatus(JobStatusEnum.FAILED.name());
                    childTriggerJob.setParentJobLogId(handleCallbackParam.getLogId());
                    xxlJobTriggerDependencyDao.updateByPrimaryKey(childTriggerJob);
                }
            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg()!=null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (childTriggerMsg !=null) {
            handleMsg.append("<br>子任务触发备注：").append(childTriggerMsg);
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        xxlJobLogDao.updateHandleInfo(log);

        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        int ret = xxlJobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret < 1) {
            xxlJobRegistryDao.registrySave(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        }
        return ReturnT.SUCCESS;
    }

    private void saveJobHistoryLog(XxlJobLog log, ReturnT<String> excuteResult){
        XxlJobTriggerHistory xxlJobTriggerHistory = new XxlJobTriggerHistory();
        xxlJobTriggerHistory.setJobId(log.getJobId());
        xxlJobTriggerHistory.setGroupId(log.getJobGroup());
        xxlJobTriggerHistory.setJobLogId(log.getId());
        xxlJobTriggerHistory.setHandleMsg(excuteResult.getMsg());
        xxlJobTriggerHistory.setHandleCode(excuteResult.getCode());
        xxlJobTriggerHistory.setCreateTime(new Date());
        xxlJobTriggerHistoryDao.insert(xxlJobTriggerHistory);
    }

}
