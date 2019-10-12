package com.xxl.job.core.grammar;

import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */
public interface ISyntaxProcessor {

    String process(String source, Map<String, String> params);
}
