package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.grammar.SyntaxProcessorFactory;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.CommonParamUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.JobInfoParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.executor.exception.JobHandlerException;

import util.DbUtil;

import java.util.Map;

/**
 * 任务Handler的一个Demo（Bean模式）
 *
 * 开发步骤： 1、继承 “IJobHandler” ； 2、装配到Spring，例如加 “@Service” 注解； 3、加 “@JobHander”
 * 注解，注解value值为新增任务生成的JobKey的值;多个JobKey用逗号分割; 4、执行日志：需要通过 "XxlJobLogger.log"
 * 打印执行日志；
 *
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHander(value = "DWSqlJobHandler")
@ConfigurationProperties
@Service
public class DWSqlJobHandler extends IJobHandler {


	@Value("${dw.db.driver}")
	private String dbDriver;
	@Value("${dw.db.url}")
	private String dbUrl;
	@Value("${dw.db.username}")
	private String dbUser;
	@Value("${dw.db.password}")
	private String dbPassword;

	@Override
	public ReturnT<String> execute(JobInfoParam jobInfoParam, String... params) {

		DbUtil dwDbUtil = new DbUtil(dbDriver,
				dbUrl, dbUser, dbPassword);
		JSONObject paramsJson = null;
		try {
			 paramsJson = JSON.parseObject(params[0]);
		} catch (Exception e) {

			return ReturnT.FAIL;
		}
		try {
			Map<String, String> replaceParams = CommonParamUtils.getCommonParams(jobInfoParam);
			String querySql = SyntaxProcessorFactory.getProcessor("SQL").process(paramsJson.getString("sql"), replaceParams);
			XxlJobLogger.log("处理之后的sql生成如下:" + querySql);
			dwDbUtil.executeSql(querySql);
		} catch (JobHandlerException e) {
		    XxlJobLogger.log("执行sql发生异常: " + e.getMessage());
			ReturnT<String> res=new ReturnT<String>(ReturnT.FAIL.getCode(), e.getResMsg());

			return res;

		}
		return ReturnT.SUCCESS;
	}

}
