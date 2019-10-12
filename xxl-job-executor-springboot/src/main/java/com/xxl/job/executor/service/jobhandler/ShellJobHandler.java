package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.GlueScript;
import com.xxl.job.core.biz.model.JobInfoParam;
import com.xxl.job.core.biz.model.ReturnT;

import com.xxl.job.core.grammar.SyntaxProcessorFactory;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.CommonParamUtils;
import com.xxl.job.core.util.ScriptUtil;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */

@JobHander(value="ShellJobHandler")
@Service
public class ShellJobHandler extends IJobHandler {

    private static final DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ReturnT<String> execute(JobInfoParam jobInfoParam, String... params) throws Exception {
        // cmd + script-file-name
        String cmd = "bash";
        String scriptFileName = XxlJobFileAppender.logPath.concat("gluesource/").concat(String.valueOf(jobInfoParam.getJobId())).concat("_").concat(".sh");

        GlueScript glueScript = JSON.parseObject(params[0], GlueScript.class);
        Map<String, String> commonParams = CommonParamUtils.getCommonParams(jobInfoParam);
        glueScript.getGlueParams().putAll(commonParams);
        String gluesource = SyntaxProcessorFactory.getProcessor(glueScript.getGlueType()).process(glueScript.getGlueSource(), glueScript.getGlueParams());

        // make script file
        ScriptUtil.markScriptFile(scriptFileName, gluesource);

        // log file
        String logFileName = XxlJobFileAppender.logPath.concat(XxlJobFileAppender.contextHolder.get());

        // invoke
        XxlJobLogger.log("----------- script file:"+ scriptFileName +" -----------");
        int exitValue = ScriptUtil.execToFile(cmd, scriptFileName, logFileName, glueScript.getCommandParams());
        ReturnT<String> result = (exitValue==0)?ReturnT.SUCCESS:new ReturnT<String>(ReturnT.FAIL_CODE, "script exit value("+exitValue+") is failed");
        return result;
    }
}
