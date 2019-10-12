package com.xxl.job.core.biz.model;

import java.io.Serializable;

/**
 * common return
 * @author xuxueli 2015-12-4 16:32:31
 * @param <T>
 */
public class ReturnT<T> implements Serializable {
	public static final long serialVersionUID = 42L;

	public static final int SUCCESS_CODE = 200;

	public static final int RUNNING_CODE = 300;
	public static final int READY_CODE = 301;
	public static final int WAITTING_CODE = 302;

	public static final int TRIGGER_SUCCESS_CODE = 100;
	public static final int TRIGGER_FAIL_CODE = 505;
	public static final int FAIL_CODE = 500;

	public static final ReturnT<String> SUCCESS = new ReturnT<String>("执行成功");
	public static final ReturnT<String> READY = new ReturnT<String>(READY_CODE, "任务准备运行");
	public static final ReturnT<String> WAITTING = new ReturnT<String>(WAITTING_CODE, "任务正在等待");
	public static final ReturnT<String> TRIGGER_SUCCESS = new ReturnT<String>(TRIGGER_SUCCESS_CODE, "任务触发成功");
	public static final ReturnT<String> RUNNING = new ReturnT<String>(RUNNING_CODE, "任务正在运行");

	public static final ReturnT<String> TRIGGER_FAIL = new ReturnT<String>(TRIGGER_FAIL_CODE, "任务触发失败");
	public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, "任务运行失败");

	private int code;
	private String msg;
	private T content;

	public ReturnT(){}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public ReturnT(T content) {
		this.code = SUCCESS_CODE;
		this.content = content;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}

}
