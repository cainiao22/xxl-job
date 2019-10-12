package com.xxl.job.core.grammar.engines;

import com.xxl.job.core.grammar.ISyntaxProcessor;
import com.xxl.job.core.util.CommonParamUtils;
import com.xxl.job.core.util.SqlUtil;

import java.text.ParseException;
import java.util.Map;

/**
 * @author yanpf
 * @date 2018/3/1 15:11
 * @description
 */
public class SqlProcessor implements ISyntaxProcessor {
    @Override
    public String process(String source, Map<String, String> params) {
        String datetime = params.get("s_datetime");
        String result = null;
        try {
            result = SqlUtil.parse(source, CommonParamUtils.dateTimeformat.parse(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
