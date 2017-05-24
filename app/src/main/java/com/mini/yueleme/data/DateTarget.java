package com.mini.yueleme.data;

import org.litepal.crud.DataSupport;

/**
 * 每个约单项对应的对男女性别数量的要求
 * Created by weiersyuan on 2016/7/24.
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
