package com.xxl.job.core.util;

import com.xxl.job.core.biz.model.JobInfoParam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanpf
 * @date 2018/3/1 14:38
 * @description
 */
public class CommonParamUtils {

    public static final DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, String> getCommonParams(JobInfoParam jobInfoParam){
        Map<String, String> replaceParams = new HashMap<>();
        replaceParams.put("s_datetime", dateTimeformat.format(jobInfoParam.getScheduleTime()));
        replaceParams.put("s_date", dateformat.format(jobInfoParam.getScheduleTime()));

        return replaceParams;
    }
}
