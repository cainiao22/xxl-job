package com.xxl.job.executor.test;

import com.xxl.job.executor.mapper.ds.DsMonitorResultSqlProvider;
import com.xxl.job.executor.model.DsMonitorResult;
import com.xxl.job.executor.model.PageModel;
import org.junit.Test;

/**
 * @author yanpf
 * @date 2017/12/28
 * @description
 */
public class TestMetaTableMonitor {

    DsMonitorResultSqlProvider provider = new DsMonitorResultSqlProvider();


    @Test
    public void testSql(){
        DsMonitorResult record = new DsMonitorResult();
        record.setId(1L);
        PageModel page = new PageModel();
        String sql = provider.query(record, page);
        System.out.println(sql);
    }

}
