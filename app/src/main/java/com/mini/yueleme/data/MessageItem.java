package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 消息中心Item对应的类
 * Created by weiersyuan on 2016/7/29.
 */
public class MessageItem extends DataSupport{
    private String message;

    public static List<MessageItem> fromJson2List(String jsonString){
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<MessageItem>>() {}.getType();
        List<MessageItem> msgItems = gson.fromJson(jsonString, type);
        return msgItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
