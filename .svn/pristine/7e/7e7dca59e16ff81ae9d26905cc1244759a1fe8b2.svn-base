package com.xxl.job.core.grammar;


import com.xxl.job.core.grammar.engines.SimpleProcessor;
import com.xxl.job.core.grammar.engines.SqlProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */
public class SyntaxProcessorFactory {

   private static Map<String, ISyntaxProcessor> processorMap = new HashMap(){
        {
            put("shell", new SimpleProcessor());
            put("SQL", new SqlProcessor());
        }
    };

    public static ISyntaxProcessor getProcessor(String glueType){
        ISyntaxProcessor processor = processorMap.get(glueType);
        if(processor == null){
            processor = new SimpleProcessor();
        }
        return processor;
    }
}
