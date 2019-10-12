package com.xxl.job.core.biz.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */
public class GlueScript implements Serializable {

    /**
     * 脚本类型，指定由那种处理引擎来处理脚本
     */
    private String glueType;

    /**
     * 脚本主体
     */
    private String glueSource;

    /**
     * 脚本处理中需要用到的参数
     */
    private Map<String, String> glueParams = new LinkedHashMap<>();

    /**
     * 脚本中需要处理的参数
     */
    private String[] commandParams;

    public String getGlueType() {
        return glueType;
    }

    public void setGlueType(String glueType) {
        this.glueType = glueType;
    }

    public String getGlueSource() {
        return glueSource;
    }

    public void setGlueSource(String glueSource) {
        this.glueSource = glueSource;
    }

    public Map<String, String> getGlueParams() {
        return glueParams;
    }

    public void setGlueParams(Map<String, String> glueParams) {
        this.glueParams = glueParams;
    }

    public String[] getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(String[] commandParams) {
        this.commandParams = commandParams;
    }
}
