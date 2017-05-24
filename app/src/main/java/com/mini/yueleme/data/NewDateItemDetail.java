package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * 约单详情
 * Created by weiersyuan on 2016/7/28.
 */
public class NewDateItemDetail extends NewDateItem{

    /**
     * 在基本约单信息的基础之上增加一个评论字段
     */
    private List<RemarkItem> remark;

    public static NewDateItemDetail fromJson(String jsonString){
        Gson gson = new GsonBuilder().create();
        NewDateItemDetail dateItemDetail = gson.fromJson(jsonString, NewDateItemDetail.class);
        return dateItemDetail;
    }

    public List<RemarkItem> getRemarks() {
        return remark;
    }

    public void setRemarks(List<RemarkItem> remarks) {
        this.remark = remarks;
    }

}
