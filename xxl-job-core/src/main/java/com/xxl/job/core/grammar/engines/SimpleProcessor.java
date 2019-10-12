package com.xxl.job.core.grammar.engines;

import com.xxl.job.core.grammar.ISyntaxProcessor;

import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */
public class SimpleProcessor implements ISyntaxProcessor {
    @Override
    public String process(String source, Map<String, String> params) {
        String result = source;
        for (Map.Entry<String, String> entry : params.entrySet()){
            String oldChar = String.format("@%s@", entry.getKey());
            result = result.replaceAll(oldChar, entry.getValue());
        }
        return result;
    }
}
