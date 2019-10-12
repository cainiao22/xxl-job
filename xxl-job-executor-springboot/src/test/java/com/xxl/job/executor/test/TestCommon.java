package com.xxl.job.executor.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanpf
 * @date 2017/12/7
 * @description
 */
public class TestCommon {

    @Test
    public void test(){
        String[] c = new String[]{"a", "b", "c"};
        String ss = JSON.toJSONString(c);
        JSONObject jsonObject = JSON.parseObject(ss);
        String[] params = new String[]{"a", "b", "c"};
        func(params);
    }

    private void func(String ... params){
        for (int i = 0; i < params.length; i++) {
            System.out.println(params[i]);
        }
    }

    private static String sql = "select  * from out.out_weixin_total_koudian_maoli where dt='${day}'";
    /*@Test
    public void testFreeMaker() throws Exception {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_0);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(sql, sql);
        conf.setTemplateLoader(stringLoader);
        Template template = conf.getTemplate(sql,"utf-8");
        Map root = new HashMap();
        root.put("day", 18);
        //root.put("studentName", "nimei");

        StringWriter writer = new StringWriter();
        try {
            template.process(root, writer);
            System.out.println(writer.toString());
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
