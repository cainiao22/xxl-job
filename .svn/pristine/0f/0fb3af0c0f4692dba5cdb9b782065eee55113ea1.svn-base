package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.JobInfoParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.executor.enums.MonitorRuleTypeEnum;
import com.xxl.job.executor.mapper.ds.DsMonitorResultMapper;
import com.xxl.job.executor.mapper.ds.DsMonitorRulesMapper;
import com.xxl.job.executor.mapper.dw.MetaTableMonitorMapper;
import com.xxl.job.executor.model.DsMonitorResult;
import com.xxl.job.executor.model.DsMonitorRules;
import com.xxl.job.executor.model.PageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.HttpClientUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/27
 * @description
 */

@JobHander(value = "MetaTableMonitorHandler")
@Service
public class MetaTableMonitorHandler extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String ALARM_WECHAT="http://wukong.iqdnet.cn/wukongbg/admin/api/wx_msg_send_by_mobiles";

    @Autowired
    DsMonitorRulesMapper dsMonitorRulesMapper;

    @Autowired
    MetaTableMonitorMapper metaTableMonitorMapper;

    @Autowired
    DsMonitorResultMapper dsMonitorResultMapper;

    @Override
    public ReturnT<String> execute(JobInfoParam jobInfoParam, String... params) throws Exception {
        if(params.length != 1){
            return new ReturnT<>(ReturnT.FAIL_CODE, "参数非法：" + JSON.toJSONString(params));
        }

        Long monitorRuleId = Long.valueOf(params[0]);
        DsMonitorRules dsMonitorRules = dsMonitorRulesMapper.selectByPrimaryKey(monitorRuleId);
        if(dsMonitorRules == null){
            return new ReturnT<>(ReturnT.FAIL_CODE, "未找到对应监控规则：" + JSON.toJSONString(params));
        }
        double result;
        if(MonitorRuleTypeEnum.SQL.name().equals(dsMonitorRules.getRuleType())){
            result = metaTableMonitorMapper.getResult(dsMonitorRules.getParameters());
            //DsMonitorResult lastExecuteResult = dsMonitorResultMapper.getLastExecuteResult(dsMonitorRules);
            Double lastExecuteResult = getAverage(dsMonitorRules);
            if(lastExecuteResult != null){
                double offset = result - lastExecuteResult;
                if(offset < dsMonitorRules.getOffsetMin()
                        || offset > dsMonitorRules.getOffestMax()){
                    logger.info("业务数据异常。result:{}, average:{}", result, lastExecuteResult);
                    wechatAlarm(dsMonitorRules);
                }
            }
        }else{
            return new ReturnT<>(ReturnT.FAIL_CODE, "不支持的处理类型：" + JSON.toJSONString(params) + dsMonitorRules.getRuleType());
        }
        DsMonitorResult dsMonitorResult = new DsMonitorResult();
        dsMonitorResult.setMonitorRuleId(dsMonitorRules.getId());
        dsMonitorResult.setOffsetDay(dsMonitorRules.getOffsetDay());
        dsMonitorResult.setScheduleTime(jobInfoParam.getScheduleTime());
        dsMonitorResult.setResultValue(result);
        dsMonitorResultMapper.insertSelective(dsMonitorResult);
        return ReturnT.SUCCESS;
    }

    public void wechatAlarm(DsMonitorRules dsMonitorRules){
        String msg = "监控规则:%d,名称:%s, 负责人:%s";
        try {
            msg = String.format(msg, dsMonitorRules.getId(), dsMonitorRules.getMonitorName(), dsMonitorRules.getOwner());
            logger.info("发送微信报警:" + msg);
            Map<String, String> params = new HashMap<>();
            params.put("mobiles", "15801029684|13641064288|15110234502|15011233180|18600295448|15313159809");
            params.put("title", "业务表监控");
            params.put("content", msg);
            HttpClientUtils.doPost(ALARM_WECHAT, params);
        }catch (Exception e){
            logger.error("微信报警发送失败:{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 三倍标准差 去除异常数据
     * @param dsMonitorRules
     * @return
     */
    private Double getAverage(DsMonitorRules dsMonitorRules){
        DsMonitorResult param = new DsMonitorResult();
        param.setMonitorRuleId(dsMonitorRules.getId());
        param.setOffsetDay(dsMonitorRules.getOffsetDay());
        PageModel page = new PageModel();
        List<DsMonitorResult> resultList = dsMonitorResultMapper.query(param, page);
        List<Double> numbers = new ArrayList<>();
        if(resultList != null && resultList.size() > 0){
            for (DsMonitorResult dsMonitorResult : resultList) {
                numbers.add(dsMonitorResult.getResultValue());
            }
        }else{
            return null;
        }
        double sum = 0;
        int length = numbers.size();
        double average = sum / length;
        for (Double item : numbers) {
            sum += item;
        }

        //获取标准差
        double e = 0;
        for (Double item : numbers) {
            e += Math.pow(item - average, 2);
        }
        e = Math.sqrt(e/length);
        double max = average + e * 3;
        double min = average - e * 3;
        for (double item : numbers) {
            if (item < min || item > max) {
                sum -= item;
                length--;
            }
        }

        return sum / length;

    }

}
