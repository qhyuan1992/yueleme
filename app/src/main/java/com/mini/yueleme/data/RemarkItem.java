package com.mini.yueleme.data;

/**
 * 评论对应的类
 * Created by weiersyuan on 2016/7/24.
 */
public class RemarkItem {
    public RemarkItem(String content, String user_name) {
        this.content = content;
        this.user_name = user_name;
    }

    private String date_id;

    private String user_id;

    private String user_name;

    private long time;

    private String content;

    public String getContent() {
        return content;
    }

    public String getDate_id() {
        return date_id;
    }

    public long getTime() {
        return time;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate_id(String date_id) {
        this.date_id = date_id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
