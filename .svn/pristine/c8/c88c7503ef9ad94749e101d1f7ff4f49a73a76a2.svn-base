package com.xxl.job.executor.test;

import com.xxl.job.executor.Application;
import com.xxl.job.executor.mapper.dw.MetaTableMonitorMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * @author yanpf
 * @date 2017/12/27
 * @description
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestDwMonitor {

    @Autowired
    MetaTableMonitorMapper metaTableMonitor;

    @Test
    public void testGetResult(){
        double result = metaTableMonitor.getResult("SELECT count(*) from out.abnormal");
        System.out.println(result);
    }

    @Test
    public void testGetFields() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Field[] fields = calendar.getClass().getFields();
        for(Field field : fields){
            System.out.println(field.getName());
            System.out.println(field.getInt(Calendar.class));
        }
    }
}
