package com.yinsin.simter.modal.base;

public class Pager {

    private int pageNum;
    private int pageSize;
    private boolean isCount = true;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean isCount) {
        this.isCount = isCount;
    }

    public int getStartIndex() {
        return (pageNum - 1) * pageSize;
    }

    public int getEndIndex() {
        return pageNum * pageSize;
    }

}
