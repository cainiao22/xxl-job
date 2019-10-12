package com.xxl.job.executor.model;

/**
 * @author yanpf
 * @date 2017/12/28
 * @description
 */
public class PageModel {

    private Integer pageNum = 1;
    private Integer pageSize = 15;

    public Integer getPageOffset(){
        return (pageNum - 1) * pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
