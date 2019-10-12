package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.mysql.jdbc.Driver;
import com.xxl.job.core.biz.model.JobInfoParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.grammar.SyntaxProcessorFactory;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.CommonParamUtils;
import com.xxl.job.executor.model.SyncDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import util.DbUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanpf
 * @date 2018/1/22
 * @description
 */

@Service
@JobHander("DataIntegerationJobHandler")
@ConfigurationProperties
public class DataIntegerationJobHandler extends IJobHandler {

    private static final Map<String, String> jdbcDrivers = new HashMap<String, String>(){
        {
            put("mysql", Driver.class.getName());
            put("sqlserver", SQLServerDriver.class.getName());
            put("postgresql", org.postgresql.Driver.class.getName());
        }
    };

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${dw.db.driver}")
    private String dbDriver;
    @Value("${dw.db.url}")
    private String dbUrl;
    @Value("${dw.db.username}")
    private String dbUser;
    @Value("${dw.db.password}")
    private String dbPassword;


    @Override
    public ReturnT<String> execute(JobInfoParam jobInfoParam, String... params) throws Exception {
        XxlJobLogger.log("入参:" + params[0] + "  jobInfoParam:" + JSON.toJSONString(jobInfoParam));
        SyncDataParam syncDataParam = JSON.parseObject(params[0], SyncDataParam.class);
        XxlJobLogger.log("转换后的bean:" + JSON.toJSONString(syncDataParam));
        DbUtil dbUtil = new DbUtil(jdbcDrivers.get(syncDataParam.getDbType()), syncDataParam.getJdbcUrl() + "",
                    syncDataParam.getUsername(), syncDataParam.getPassword());
        DbUtil destDbUtil = new DbUtil(dbDriver, dbUrl, dbUser, dbPassword);

        Connection sourceConn = null;
        PreparedStatement readStatement = null;
        ResultSet resultSet = null;

        Connection writeConn = null;
        PreparedStatement writeStatement = null;
        try {
            sourceConn = dbUtil.getConnection();
            writeConn = destDbUtil.getConnection();

            Map<String, String> replaceParams = CommonParamUtils.getCommonParams(jobInfoParam);
            String querySql = SyntaxProcessorFactory.getProcessor("SQL").process(syncDataParam.getSql(), replaceParams);
            XxlJobLogger.log("处理之后的sql生成如下:" + querySql);
            readStatement = sourceConn.prepareStatement(querySql,
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            readStatement.setFetchSize(800);
            resultSet = readStatement.executeQuery();
            int columnCounts = resultSet.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCounts];
            //读取每一列的别名
            for (int i = 1; i <= columnCounts; i++) {
                columnNames[i - 1] = resultSet.getMetaData().getColumnLabel(i);
            }
            //生成插入的sql语句
            String insertSql = getInsertSql(syncDataParam.getTableName(), columnNames);
            writeConn.setAutoCommit(false);
            writeStatement = writeConn.prepareStatement(insertSql);
            if(syncDataParam.getDelTargetTable() != null && syncDataParam.getDelTargetTable() != 0) {
                String delSql = "truncate table " + syncDataParam.getTableName();
                XxlJobLogger.log("需要清空原有表数据：" + delSql);
                Statement delStatement = null;
                try{
                    delStatement = writeConn.createStatement();
                    delStatement.execute(delSql);
                    writeConn.commit();
                }catch (Exception e){
                    XxlJobLogger.log("需要清空原有表数据出错。任务退出");
                    throw e;
                }finally {
                    DbUtil.close(null, delStatement, null);
                }
                XxlJobLogger.log("原有表数据清理完毕");
            }
            int length = 0;
            long beginTime = System.currentTimeMillis();
            XxlJobLogger.log("开始插入新数据,开始时间:" + beginTime);
            while (resultSet.next()) {
                length++;
                for (int i = 1; i <= columnCounts; i++) {
                    int columnType = resultSet.getMetaData().getColumnType(i);
                    if(columnType == Types.VARCHAR || columnType == Types.LONGVARCHAR || columnType == Types.LONGNVARCHAR
                            || columnType == Types.CHAR || columnType == Types.NCHAR || columnType == Types.NCHAR
                            || columnType == Types.NVARCHAR) {
                        String valueStr = resultSet.getString(i);
                        if(valueStr != null){
                            valueStr = valueStr.replaceAll("\0", "");
                        }
                        writeStatement.setString(i, valueStr);
                    }else{
                        writeStatement.setObject(i, resultSet.getObject(i), columnType);
                    }
                }
                writeStatement.addBatch();
                //如果超过300条，提交一次
                if (length % 300 == 0) {
                    writeStatement.executeBatch();
                    writeConn.commit();
                }
            }
            writeStatement.executeBatch();
            writeConn.commit();
            XxlJobLogger.log("数据插入结束，耗时:" + (System.currentTimeMillis() - beginTime) + "毫秒，共插入" + length + "条数据");
            writeConn.setAutoCommit(true);
        }catch (BatchUpdateException e){
            throw e.getNextException();
        }
        catch (Exception e){
            throw e;
        }finally {
            try{
                DbUtil.close(resultSet, readStatement, sourceConn);
            }catch (Exception e){
                throw e;
            }finally {
                DbUtil.close(null, writeStatement, writeConn);
            }
        }
        return ReturnT.SUCCESS;
    }

    private String getInsertSql(String tableName, String[] columns){
        String createSql = "insert into %s (%s) values (%s)";
        StringBuffer columnNames = new StringBuffer();
        StringBuffer fillings = new StringBuffer();
        for(String column : columns){
            columnNames.append(column).append(",");
            fillings.append("?").append(",");
        }
        columnNames.setLength(columnNames.length() - 1);
        fillings.setLength(fillings.length() - 1);

        String result = String.format(createSql, tableName, columnNames.toString(), fillings.toString());
        logger.info("生成的sql如下:{}", result);

        return result;
    }
}
