package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.enums.ExecutorFailStrategyEnum;
import com.xxl.job.admin.core.enums.JobTriggerTypeEnum;
import com.xxl.job.admin.core.model.*;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.core.thread.JobFailMonitorHelper;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * xxl-job trigger
 * @author  xuxueli on 17/7/13.
 */
public class XxlJobTrigger {
    private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

    /**
     * 如果是子任务触发的，那么他的子任务的logId会传过来
     */
    public static final String CHILD_JOBLOG_ID = "childJobLogId";
    /**
     * 如果该任务是因为父任务反向触发而来，那么这个值就是他原来的那个logId
     */
    public static final String PRE_JOBLOG_ID = "preJobLogId";

    /**
     * 标识该任务是自动触发还是手动触发
     */
    public static final String JOB_TRIGGE_TYPE = "jobTriggerType";

    /**
     * 任务的调度时间
     */
    public static final String JOB_SCHEDULE_TIME = "jobSchedultTime";


    /**
     * trigger job
     *
     * @param jobId
     */
    public static void trigger(int jobId, int jobLogId, int childJobLogId, JobTriggerTypeEnum triggerTypeEnum, Date scheduleTime) {

        // load data
        XxlJobInfo jobInfo = XxlJobDynamicScheduler.xxlJobInfoDao.loadById(jobId);              // job info
        XxlJobGroup group = XxlJobDynamicScheduler.xxlJobGroupDao.load(jobInfo.getJobGroup());  // group info
        saveXxlJobTriggerHistory(jobInfo, jobLogId, ReturnT.READY);
        if (!checkAndSetUpDependency(jobInfo, jobLogId, childJobLogId, triggerTypeEnum)) {
            return;
        }
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);  // block strategy
        ExecutorFailStrategyEnum failStrategy = ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), ExecutorFailStrategyEnum.FAIL_ALARM);    // fail strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);    // route strategy
        ArrayList<String> addressList = (ArrayList<String>) group.getRegistryList();

        // broadcast
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum && CollectionUtils.isNotEmpty(addressList)) {
            for (int i = 0; i < addressList.size(); i++) {
                String address = addressList.get(i);

                // 1、save log-id
                XxlJobLog jobLog = new XxlJobLog();
                jobLog.setJobGroup(jobInfo.getJobGroup());
                jobLog.setJobId(jobInfo.getId());
                if (jobLogId == 0) {
                    XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
                } else { //是由父任务重新触发过来的
                    jobLog.setId(jobLogId);
                }
                logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

                //是由子任务触发的，需要保存子任务和父任务的依赖关系
                saveRelationShap(childJobLogId, jobLog, triggerTypeEnum);
                // 2、prepare trigger-info
                //jobLog.setExecutorAddress(executorAddress);
                jobLog.setGlueType(jobInfo.getGlueType());
                jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
                jobLog.setExecutorParam(jobInfo.getExecutorParam());
                jobLog.setTriggerTime(new Date());

                ReturnT<String> triggerResult = new ReturnT<String>(null);
                StringBuffer triggerMsgSb = new StringBuffer();
                triggerMsgSb.append("注册方式：").append((group.getAddressType() == 0) ? "自动注册" : "手动录入");
                triggerMsgSb.append("<br>阻塞处理策略：").append(blockStrategy.getTitle());
                triggerMsgSb.append("<br>失败处理策略：").append(failStrategy.getTitle());
                triggerMsgSb.append("<br>地址列表：").append(group.getRegistryList());
                triggerMsgSb.append("<br>路由策略：").append(executorRouteStrategyEnum.getTitle()).append("(" + i + "/" + addressList.size() + ")"); // update01

                // 3、trigger-valid
                if (triggerResult.getCode() == ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
                    triggerResult.setCode(ReturnT.FAIL_CODE);
                    triggerMsgSb.append("<br>----------------------<br>").append("调度失败：").append("执行器地址为空");
                }

                if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
                    // 4.1、trigger-param
                    TriggerParam triggerParam = new TriggerParam();
                    triggerParam.setJobId(jobInfo.getId());
                    triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
                    triggerParam.setExecutorParams(jobInfo.getExecutorParam());
                    triggerParam.setJobName(jobInfo.getJobDesc());
                    //调度时间
                    triggerParam.setScheduleTime(scheduleTime);
                    triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
                    triggerParam.setLogId(jobLog.getId());
                    triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
                    triggerParam.setGlueType(jobInfo.getGlueType());
                    triggerParam.setGlueSource(jobInfo.getGlueSource());
                    triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
                    triggerParam.setBroadcastIndex(i);
                    triggerParam.setBroadcastTotal(addressList.size()); // update02

                    // 4.2、trigger-run (route run / trigger remote executor)
                    triggerResult = runExecutor(triggerParam, address);     // update03
                    triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

                    // 4.3、trigger (fail retry)
                    if (triggerResult.getCode() != ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                        triggerResult = runExecutor(triggerParam, address);  // update04
                        triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>失败重试<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
                    }
                }

                // 5、save trigger-info
                jobLog.setExecutorAddress(triggerResult.getContent());
                jobLog.setTriggerCode(triggerResult.getCode());
                jobLog.setTriggerMsg(triggerMsgSb.toString());
                if(jobLog.getTriggerCode() == ReturnT.FAIL_CODE){
                    jobLog.setHandleCode(ReturnT.FAIL_CODE);
                    jobLog.setHandleMsg(jobLog.getTriggerMsg());
                }
                XxlJobDynamicScheduler.xxlJobLogDao.updateTriggerInfo(jobLog);

                // 6、monitor triger
                JobFailMonitorHelper.monitor(jobLog.getId());
                logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());

            }
            saveXxlJobTriggerHistory(jobInfo, jobLogId, ReturnT.TRIGGER_SUCCESS);
            return;
        }

        // 1、save log-id
        XxlJobLog jobLog = new XxlJobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        if (jobLogId == 0) {
            XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
        } else {//是由父任务重新触发过来的
            jobLog.setId(jobLogId);
        }
        //是由子任务触发的，需要保存子任务和父任务的依赖关系
        saveRelationShap(childJobLogId, jobLog, triggerTypeEnum);
        logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getId());

        // 2、prepare trigger-info
        //jobLog.setExecutorAddress(executorAddress);
        jobLog.setGlueType(jobInfo.getGlueType());
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setTriggerTime(new Date());

        ReturnT<String> triggerResult = new ReturnT<String>(null);
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append("注册方式：").append((group.getAddressType() == 0) ? "自动注册" : "手动录入");
        triggerMsgSb.append("<br>阻塞处理策略：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>失败处理策略：").append(failStrategy.getTitle());
        triggerMsgSb.append("<br>地址列表：").append(group.getRegistryList());
        triggerMsgSb.append("<br>路由策略：").append(executorRouteStrategyEnum.getTitle());

        // 3、trigger-valid
        if (triggerResult.getCode() == ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
            triggerResult.setCode(ReturnT.FAIL_CODE);
            triggerMsgSb.append("<br>----------------------<br>").append("调度失败：").append("执行器地址为空");
        }

        if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
            // 4.1、trigger-param
            TriggerParam triggerParam = new TriggerParam();
            triggerParam.setJobId(jobInfo.getId());
            triggerParam.setJobName(jobInfo.getJobDesc());
            triggerParam.setScheduleTime(scheduleTime);
            triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
            triggerParam.setExecutorParams(jobInfo.getExecutorParam());
            triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
            triggerParam.setLogId(jobLog.getId());
            triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
            triggerParam.setGlueType(jobInfo.getGlueType());
            triggerParam.setGlueSource(jobInfo.getGlueSource());
            triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
            triggerParam.setBroadcastIndex(0);
            triggerParam.setBroadcastTotal(1);

            // 4.2、trigger-run (route run / trigger remote executor)
            triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
            triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

            // 4.3、trigger (fail retry)
            if (triggerResult.getCode() != ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
                triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>失败重试<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
            }
        }

        // 5、save trigger-info
        jobLog.setExecutorAddress(triggerResult.getContent());
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        XxlJobDynamicScheduler.xxlJobLogDao.updateTriggerInfo(jobLog);

        // 6、monitor triger
        JobFailMonitorHelper.monitor(jobLog.getId());
        logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getId());
        if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
            saveXxlJobTriggerHistory(jobInfo, jobLog.getId(), ReturnT.TRIGGER_SUCCESS);
        }else {
            saveXxlJobTriggerHistory(jobInfo, jobLog.getId(), ReturnT.TRIGGER_FAIL);
        }
    }


    /**
     * run executor
     *
     * @param triggerParam
     * @param address
     * @return ReturnT.content: final address
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address) {
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = XxlJobDynamicScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE, "" + e);
        }

        StringBuffer runResultSB = new StringBuffer("触发调度：");
        runResultSB.append("<br>address：").append(address);
        runResultSB.append("<br>code：").append(runResult.getCode());
        runResultSB.append("<br>msg：").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        runResult.setContent(address);
        return runResult;
    }

    /**
     * 检查父任务的执行情况
     *
     * @param jobInfo
     * @param jobLogId 可能是由父任务触发回来的，此时的jobLogId不会为0
     * @Param childJobLogId 子任务的jobLogId,也就是说该任务是子任务触发的
     * @Param triggerTypeEnum 触发类型。
     * @return
     */
    private static boolean checkAndSetUpDependency(XxlJobInfo jobInfo, int jobLogId, int childJobLogId, JobTriggerTypeEnum triggerTypeEnum) {
        String parentJobKeyStr = jobInfo.getParentJobKey();
        if (StringUtils.isBlank(parentJobKeyStr)) {
            return true;
        }
        String[] parentJobKeys = parentJobKeyStr.split(",");

        List<XxlJobLog> jobsToTrigger = new ArrayList<>();

        List<XxlJobLog> jobsRunning = new ArrayList<>();
        List<XxlJobLog> jobsFailed = new ArrayList<>();
        for (String parentJobKey : parentJobKeys) {
            int parentGroup = Integer.valueOf(parentJobKey.split("_")[0]);
            int parentJobId = Integer.valueOf(parentJobKey.split("_")[1]);
            XxlJobLog parentJobLog = XxlJobDynamicScheduler.xxlJobLogDao.findLatestJobStatus(parentGroup, parentJobId);
            //判断log状态是没有还是在运行
            if (parentJobLog == null) {
                parentJobLog = new XxlJobLog();
                parentJobLog.setJobId(parentJobId);
                parentJobLog.setJobGroup(parentGroup);
                jobsToTrigger.add(parentJobLog);
            } else if (parentJobLog.getTriggerCode() == 200 && parentJobLog.getHandleCode() == 0) {
                //有父任务在运行
                jobsRunning.add(parentJobLog);
            } else if(parentJobLog.getTriggerCode() != 200 || parentJobLog.getHandleCode() != 200){
                //父任务运行失败
                jobsFailed.add(parentJobLog);
            }
        }

        if (!jobsFailed.isEmpty()) {

            //失败
            XxlJobLog jobLog = new XxlJobLog();
            jobLog.setJobGroup(jobInfo.getJobGroup());
            jobLog.setJobId(jobInfo.getId());
            jobLog.setTriggerTime(new Date());
            StringBuffer sb = new StringBuffer("[");
            for (XxlJobLog xxlJobLog : jobsFailed) {
                sb.append(xxlJobLog.getJobGroup() + "_" + xxlJobLog.getJobId())
                        .append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("]");
            jobLog.setTriggerMsg("触发任务失败,父任务存在异常:" + sb.toString());
            if(jobLogId != 0){
                jobLog.setId(jobLogId);
                XxlJobDynamicScheduler.xxlJobLogDao.updateTriggerInfo(jobLog);
            }else {
                XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
            }
            //父任务失败，保存流水日志退出
            saveXxlJobTriggerHistory(jobInfo, jobLog.getId(), new ReturnT(ReturnT.TRIGGER_FAIL_CODE, jobLog.getTriggerMsg()));
            return false;
        }
        if (!jobsToTrigger.isEmpty() || !jobsRunning.isEmpty()) {
            //是由父任务重新触发回来的，但是有一些还没执行完毕，仍然等待
            if(jobLogId != 0){
                return false;
            }
            List<XxlJobInfo> realJobsToTrigger = simplifyDependency(jobsToTrigger);
            XxlJobLog jobLog = new XxlJobLog();
            jobLog.setJobGroup(jobInfo.getJobGroup());
            jobLog.setJobId(jobInfo.getId());
            JobDataMap jobDataMap = new JobDataMap();
            XxlJobDynamicScheduler.xxlJobLogDao.save(jobLog);
            //子任务触发的该任务，要保存子任务和该任务的依赖关系
            if(childJobLogId != 0){
                saveRelationShap(childJobLogId, jobLog, triggerTypeEnum);
            }
            jobDataMap.put(CHILD_JOBLOG_ID, String.valueOf(jobLog.getId()));
            jobDataMap.put(JOB_TRIGGE_TYPE, triggerTypeEnum.name());
            if(triggerTypeEnum == JobTriggerTypeEnum.MANUAL_TRIGGER) {
                for (XxlJobInfo parentJobInfo : realJobsToTrigger) {
                    try {
                        XxlJobDynamicScheduler.triggerJob(String.valueOf(parentJobInfo.getId()), String.valueOf(parentJobInfo.getJobGroup()), jobDataMap);
                    } catch (SchedulerException e) {
                        logger.error("触发依赖任务失败:{}", e);
                        jobLog.setTriggerMsg("触发依赖任务失败,jobKey:" + parentJobInfo.getJobGroup() + "_" + parentJobInfo.getId());
                        return false;
                    }
                }
            }else {
                //仅仅保存该依赖关系
                for (XxlJobInfo parentJobInfo : realJobsToTrigger) {
                    XxlJobLog parentJobLog = new XxlJobLog();
                    parentJobLog.setJobGroup(parentJobInfo.getJobGroup());
                    parentJobLog.setJobId(parentJobInfo.getId());
                    saveRelationShap(jobLog.getId(), parentJobLog, triggerTypeEnum);
                }
            }
            int count = 0;
            for (XxlJobLog parentJobLog : jobsRunning) {
               boolean flag = saveRelationShap(jobLog.getId(), parentJobLog, triggerTypeEnum);
               count += flag ? 1 : 0;
            }
            //如果只有运行中任务，并且这时候运行中的任务已经完成了，没必要再等待了，直接运行
            if(count == jobsRunning.size() && jobsToTrigger.isEmpty()){
                return true;
            }
            saveXxlJobTriggerHistory(jobInfo, jobLog.getId(), ReturnT.WAITTING);
            return false;
        }

        return true;

    }

    /**
     * 去除同层级之间的依赖
     * @param parents
     * @return
     */
    private static List<XxlJobInfo> simplifyDependency(List<XxlJobLog> parents){
        List<XxlJobInfo> result = new ArrayList<>();
        Queue<XxlJobInfo> queue = new LinkedList<>();
        Map<String, XxlJobInfo> map = new HashMap<>();
        for (XxlJobLog xxlJobLog : parents) {
            XxlJobInfo xxlJobInfo = new XxlJobInfo();
            xxlJobInfo.setJobGroup(xxlJobLog.getJobGroup());
            xxlJobInfo.setId(xxlJobLog.getJobId());
            queue.add(xxlJobInfo);
            map.put(xxlJobLog.getJobGroup() + "_" + xxlJobLog.getJobId(), xxlJobInfo);
        }
        while(!queue.isEmpty()){
            XxlJobInfo xxlJobInfo = XxlJobDynamicScheduler.xxlJobInfoDao.loadById(queue.poll().getId());
            if(xxlJobInfo != null && StringUtils.isNotBlank(xxlJobInfo.getParentJobKey())){
                String[] split = xxlJobInfo.getParentJobKey().split(",");
                for (String s : split) {
                   if(map.containsKey(s)){
                       map.remove(s);
                       if(map.isEmpty()){
                           return Collections.emptyList();
                       }
                   }
                    int parentGroup = Integer.valueOf(s.split("_")[0]);
                    int parentJobId = Integer.valueOf(s.split("_")[1]);
                    XxlJobInfo newParent = new XxlJobInfo();
                    newParent.setJobGroup(parentGroup);
                    newParent.setId(parentJobId);
                    queue.add(newParent);
                }
            }
        }
        for (XxlJobInfo xxlJobInfo : map.values()) {
            result.add(xxlJobInfo);
        }
        return result;
    }

    private static boolean saveRelationShap(int childJobLogId, XxlJobLog jobLog, JobTriggerTypeEnum triggerTypeEnum){
        return XxlJobDynamicScheduler.xxlJobLogDepService.saveRelationShap(childJobLogId, jobLog, triggerTypeEnum);
    }

    private static void saveXxlJobTriggerHistory(XxlJobInfo jobInfo, int jobLogId, ReturnT returnT){
        XxlJobTriggerHistory jobTriggerHistory = new XxlJobTriggerHistory();
        jobTriggerHistory.setJobLogId(jobLogId);
        jobTriggerHistory.setHandleCode(returnT.getCode());
        jobTriggerHistory.setHandleMsg(returnT.getMsg());
        jobTriggerHistory.setJobId(jobInfo.getId());
        jobTriggerHistory.setGroupId(jobInfo.getJobGroup());
        jobTriggerHistory.setCreateTime(new Date());
        XxlJobDynamicScheduler.xxlJobTriggerHistoryDao.insert(jobTriggerHistory);

    }

}
