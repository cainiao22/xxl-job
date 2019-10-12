package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.core.enums.JobStatusEnum;
import com.xxl.job.admin.core.enums.JobTriggerTypeEnum;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.model.XxlJobTriggerDependency;
import com.xxl.job.admin.dao.XxlJobLogDao;
import com.xxl.job.admin.dao.XxlJobTriggerDependencyDao;
import com.xxl.job.admin.service.XxlJobLogDepService;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author yanpf
 * @date 2017/12/1
 * @description jvm锁去更新有关联关系的依赖表，避免父任务执行完回调时候会漏掉刚刚这个新任务
 */
@Service
public class XxlJobLogDepServiceImpl implements XxlJobLogDepService,InitializingBean {

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobLogDao xxlJobLogDao;

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private XxlJobTriggerDependencyDao xxlJobTriggerDependencyDao;

    private static final int LOCK_NUMS = 300;

    private ReentrantReadWriteLock[] locks = new ReentrantReadWriteLock[LOCK_NUMS];

    @Override
    public void afterPropertiesSet() throws Exception {
        for(int i=0; i<locks.length; i++){
            locks[i] = new ReentrantReadWriteLock();
        }
    }

    @Override
    public List<XxlJobTriggerDependency> getChildrenByDependencyJobLogId(int parentJobLogId) {
        try{
            getReadLock(parentJobLogId).lock();
            return xxlJobTriggerDependencyDao.getChildrenByDependencyJobLogId(parentJobLogId);
        }finally {
            getReadLock(parentJobLogId).unlock();
        }
    }

    /**
     * 保存当前任务和子任务的依赖关系
     *
     * @param childJobLogId
     * @param jobLog
     */
    @Override
    public boolean saveRelationShap(int childJobLogId, XxlJobLog jobLog, JobTriggerTypeEnum triggerTypeEnum) {
        if (childJobLogId != 0) {
            //不知道父任务的logId.代表父任务没有运行，不要锁
            if(jobLog.getId() != 0) {
                getWriteLock(jobLog.getId()).lock();
            }
            try {
                XxlJobLog parentLog = xxlJobLogDao.load(jobLog.getId());
                if(parentLog != null && parentLog.getHandleCode() == ReturnT.SUCCESS_CODE){
                    return false;
                }
                XxlJobTriggerDependency relation = new XxlJobTriggerDependency();
                XxlJobLog childJobLog = xxlJobLogDao.load(childJobLogId);
                relation.setGroupId(childJobLog.getJobGroup());
                relation.setJobId(childJobLog.getJobId());
                relation.setParentJobLogId(jobLog.getId());
                relation.setJobLogId(childJobLogId);
                relation.setTriggerType(triggerTypeEnum.name());
                relation.setParentJobId(jobLog.getJobId());
                relation.setParentGroupId(jobLog.getJobGroup());
                relation.setJobStatus(JobStatusEnum.WAITING.name());
                xxlJobTriggerDependencyDao.insert(relation);
                return true;
            }finally {
                if(jobLog.getId() != 0) {
                    getWriteLock(jobLog.getId()).unlock();
                }
            }
        }
        return true;
    }

    private Lock getWriteLock(int lockId){
        return locks[lockId % LOCK_NUMS].writeLock();
    }

    private Lock getReadLock(int lockId){
        return locks[lockId % LOCK_NUMS].readLock();
    }


}
