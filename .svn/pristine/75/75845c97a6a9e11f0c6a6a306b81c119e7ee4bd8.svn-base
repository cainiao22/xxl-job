package com.xxl.job.admin.core.jobbean;

import com.xxl.job.admin.core.enums.JobTriggerTypeEnum;
import com.xxl.job.admin.core.trigger.XxlJobTrigger;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * http job bean
 * “@DisallowConcurrentExecution” diable concurrent, thread size can not be only one, better given more
 * @author xuxueli 2015-12-17 18:20:34
 */
//@DisallowConcurrentExecution
public class RemoteHttpJobBean extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(RemoteHttpJobBean.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		// load jobId
		JobKey jobKey = context.getTrigger().getJobKey();
		Integer jobId = Integer.valueOf(jobKey.getName());
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		int childJobLogId = jobDataMap.get(XxlJobTrigger.CHILD_JOBLOG_ID) == null ? 0 : jobDataMap.getIntValue(XxlJobTrigger.CHILD_JOBLOG_ID);
		int preJobLogId = jobDataMap.get(XxlJobTrigger.PRE_JOBLOG_ID) == null ? 0: jobDataMap.getIntValue(XxlJobTrigger.PRE_JOBLOG_ID);
		JobTriggerTypeEnum jobTriggerTypeEnum = jobDataMap.get(XxlJobTrigger.JOB_TRIGGE_TYPE) == null ? JobTriggerTypeEnum.AUTOMATIC_TRIGGER:JobTriggerTypeEnum.valueOf(jobDataMap.getString(XxlJobTrigger.JOB_TRIGGE_TYPE));
		long timestamp = jobDataMap.get(XxlJobTrigger.JOB_SCHEDULE_TIME) == null ? 0: jobDataMap.getLongValue(XxlJobTrigger.JOB_SCHEDULE_TIME);
		Date scheduleTime = new Date();
		if(timestamp != 0){
			scheduleTime = new Date(timestamp);
		}
		// trigger
		XxlJobTrigger.trigger(jobId, preJobLogId, childJobLogId, jobTriggerTypeEnum, scheduleTime);
	}

}