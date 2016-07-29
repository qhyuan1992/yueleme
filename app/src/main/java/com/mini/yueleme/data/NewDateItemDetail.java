package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by weiersyuan on 2016/7/28.
 */
public class NewDateItemDetail extends  NewDateItem{

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
