package com.xxl.job.dao.impl;

import com.xxl.job.admin.dao.XxlJobTriggerDependencyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by yanpf on 2017/11/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationcontext-*.xml")
public class XxlJobTriggerDependencyDaoTest extends ThreadGroup {

    @Resource
    XxlJobTriggerDependencyDao xxlJobTriggerDependencyDao;

    public XxlJobTriggerDependencyDaoTest() {
        super("ccccc");
    }

    @Test
    public void testgetChildrenByDependencyJobLogId(){
        xxlJobTriggerDependencyDao.getChildrenByDependencyJobLogId(1);
    }

    @Test
    public void testInterrupt() throws InterruptedException {
        Thread a = new Thread(){
            @Override
            public void run() {
                try {
                    this.sleep(10000);
                } catch (InterruptedException e) {
                   throw new NullPointerException(e.getMessage());
                }
            }
        };
        a.setName("thread_a");
        a.setUncaughtExceptionHandler(this);
        a.start();
        a.interrupt();
        Thread.sleep(10000);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t.getName());
        e.printStackTrace();
    }
}
