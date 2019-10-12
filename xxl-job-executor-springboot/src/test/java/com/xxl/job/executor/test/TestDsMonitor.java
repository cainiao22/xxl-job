package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import com.xxl.job.executor.Application;
import com.xxl.job.executor.mapper.ds.DsMonitorRulesMapper;
import com.xxl.job.executor.model.DsMonitorRules;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author yanpf
 * @date 2017/12/27
 * @description
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestDsMonitor {

    @Autowired
    DsMonitorRulesMapper dsMonitorRulesMapper;

    @Test
    public void testSelect(){
        DsMonitorRules res = dsMonitorRulesMapper.selectByPrimaryKey(1L);
        System.out.println(JSON.toJSONString(res));
    }

    @Test
    public void testAx(){
        getAverage(Arrays.asList(1,2,3,1,2,3,4,2,3,9, -4, -1, -3, -5));
    }

    /**
     * 三倍标准差法 获取平均值
     * @param list
     * @return
     */
    private double getAverage(List<Integer> list){
        int length = list.size();
        double sum = 0;

        for (Integer item : list) {
            sum += item;
        }

        double average = sum / length;

        //获取标准差
        double e = 0;
        for (Integer item : list) {
            e += Math.pow(item - average, 2);
        }
        e = Math.sqrt(e/length);
        double max = average + e * 3;
        double min = average - e * 3;
        for (Integer item : list) {
            if (item < min || item > max) {
                sum -= item;
                length--;
                System.out.print(item + "\t");
            }
        }
        System.out.println();

        return sum / length;
    }
}
