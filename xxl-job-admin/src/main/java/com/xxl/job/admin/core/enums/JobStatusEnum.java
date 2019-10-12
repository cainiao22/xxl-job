package com.xxl.job.admin.core.enums;

/**
 * @author yanpf
 * @date 2017/11/28
 * @description
 */

public enum JobStatusEnum {
    PENDING(303, "任务唤起成功"),
    READY(301, "任务准备运行"),
    WAITING(302, "任务正在等待"),
    TRIGGER_SUCCESS(100, "任务触发成功"),
    RUNNING(300, "任务正在运行"),
    SUCCEED(200, "执行成功"),
    FAILED(500, "任务运行失败");

    private int code;
    private String desc;

    JobStatusEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
