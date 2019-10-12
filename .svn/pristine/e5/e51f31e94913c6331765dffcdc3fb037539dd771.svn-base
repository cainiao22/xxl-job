package com.xxl.job.admin.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * @author yanpf
 * @date 2018/3/14 9:23
 * @description
 */

@ControllerAdvice("com.xxl.job.admin.controller")
public class JsonpAdvice extends AbstractJsonpResponseBodyAdvice {

    public JsonpAdvice(){
        super("jsonp", "callback");
    }
}
