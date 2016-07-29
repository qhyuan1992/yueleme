package com.mini.yueleme.data;

import org.litepal.crud.DataSupport;

/**
 * Created by weiersyuan on 2016/7/24.
 * 主页上显示的每条约单
 */
public class DateTarget extends DataSupport{
    private int boy;
    private int girl;

    public int getBoy() {
        return boy;
    }

    public void setBoy(int boy) {
        this.boy = boy;
    }

    public int getGirl() {
        return girl;
    }

    public void setGirl(int girl) {
        this.girl = girl;
    }
}
