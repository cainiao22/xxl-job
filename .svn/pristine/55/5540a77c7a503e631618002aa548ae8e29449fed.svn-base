package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.GlueScript;
import org.junit.Test;

import java.util.Date;

/**
 * @author yanpf
 * @date 2017/12/8
 * @description
 */
public class TestShellHandler {

    @Test
    public void test(){
        GlueScript glueScript = new GlueScript();
        glueScript.setGlueSource("echo '@s_date@ @param@' $@");
        glueScript.setCommandParams(new String[]{"a", "o", "e"});
        glueScript.getGlueParams().put("param", "wo shi param");
        glueScript.setGlueType("shell");
        System.out.println(JSON.toJSONString(glueScript));
    }

    @Test
    public void test2(){
        Date date = new Date(1495504683344L);
        System.out.println(date.toString());
    }
}
