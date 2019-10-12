package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.JobInfoParam;
import com.xxl.job.executor.Application;
import com.xxl.job.executor.model.SyncDataParam;
import com.xxl.job.executor.service.jobhandler.DWSqlJobHandler;
import com.xxl.job.executor.service.jobhandler.DataIntegerationJobHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author yanpf
 * @date 2018/1/22
 * @description
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestDataIntegerationJobHandler {

    @Autowired
    DataIntegerationJobHandler handler;

    @Autowired
    DWSqlJobHandler dwSqlJobHandler;

    @Test
    public void testExecute() throws Exception {
        SyncDataParam param = new SyncDataParam();
        param.setDbType("mysql");
        param.setJdbcUrl("jdbc:mysql://localhost:3306/ds");
        param.setUsername("root");
        param.setPassword("root123");
        param.setTableName("ds_jobnotify_copy");
        param.setSql("select * from ds_jobnotify where createtime<'${当天-1}'");
        param.setDelTargetTable(1);
        long begin = System.currentTimeMillis();
        JobInfoParam jobInfoParam = new JobInfoParam();
        jobInfoParam.setScheduleTime(new Date());
        for(int i=0; i<1; i++) {
            handler.execute(jobInfoParam, "{\"password\":\"wh2HR~]t\",\"db_id\":\"e0696e3eaf2211e7aaa444a842284092\",\"dtOffset\":\"-1\",\"jdbcUrl\":\"jdbc:mysql://10.37.5.124:3307/qding_housekeeper\",\"delTargetTable\":\"0\",\"dbType\":\"mysql\",\"target_tableId\":\"831c5f92fe894bc69f4beebc53a33256\",\"sql\":\"SELECT *  from hk_matter_apply where FROM_UNIXTIME(createtime/1000, '%Y-%m-%d')=${当天-1}\",\"username\":\"qding_read\",\"tableName\":\"ODS.ods_hk_matter_apply\"}");
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - begin));
    }

    @Test
    public void testDWSql() throws Exception {
        String json = "{\"dtOffset\":\"-1\",\"sql\":\"\\nDELETE from mid.mid_uer_active_detail  where dt=${当日-1} and behavior_type in ('package', 'repair');\\n\\ninsert into mid.mid_uer_active_detail \\n\\nSELECT ${当日-1} as dt, projectid as project_id, projectname as project_name,userid as member_id, 'repair' as behavior_type, COUNT(1) as num from ods.ods_hk_matter_apply where id in (SELECT applyid from ods.ods_hk_matter_reply where to_char(to_timestamp(createtime/1000), 'yyyy-MM-dd')=${当日-1} and status='Completed')  GROUP BY userid,projectid,projectname\\n\\nunion all\\n\\nSELECT ${当日-1} as dt,project_id, project_name, mid as member_id, 'package' as behavior_type, count(1) as num from ods.ods_courier where to_char(to_timestamp(create_at/1000), 'yyyy-MM-dd')=${当日-1} GROUP BY project_id, project_name, mid;\"}";
        json = "{\"dtOffset\":\"-1\",\"sql\":\"delete from mid.mid_room_active_detail where dt=${当天-1};\\ninsert into mid.mid_room_active_detail\\nselect a.dt,a.project_id,a.project_name,b.region_id,b.region_name,c.room_id,b.stage,sum(num) num ,b.online_date from \\n(select project_id ,stage,online_date from dim.dim_project where online_date is not null ) b\\nLEFT JOIN \\n(select * from mid.mid_uer_active_detail where dt=${当天-1})a\\non a.project_id=b.project_id\\nleft join \\n(select * from mid.mid_member_room_bind_snapshot where dt=${当天-1})c\\non a.project_id=c.project_id and a.member_id=c.member_id\\nwhere c.room_id is not null\\nGROUP BY a.dt,a.project_id,a.project_name,b.region_id,b.region_name,c.room_id,b.online_date,b.stage\\n\\n\"}";
        JobInfoParam jobInfoParam = new JobInfoParam();
        jobInfoParam.setScheduleTime(new Date());
        dwSqlJobHandler.execute(jobInfoParam, json);
    }
}


class A {
    private A next;
}