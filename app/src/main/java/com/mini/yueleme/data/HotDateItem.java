package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by weiersyuan on 2016/7/26.
 */
public class HotDateItem extends DataSupport{

    private long create_time;

    private String image_url;

    private String date_id;

    private String subject;

    private int date_person_num;

    private int date_join_num;

    private int date_time;

    private String date_location;

    private String user_id;

    private String user_name;

    private String group_name;

    private int sex;

    private int pay;

    private int date_limit;

    private DateTarget date_target;

    private String description;

    private int follow_num;

    private int remark_num;

    private int user_status;

    public static List<HotDateItem> fromJson2List(String jsonString){
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<HotDateItem>>() {}.getType();
        List<HotDateItem> dateItems = gson.fromJson(jsonString, type);
        return dateItems;
    }

    public static HotDateItem fromJson(String jsonString){
        Gson gson = new GsonBuilder().create();
        HotDateItem dateItem = gson.fromJson(jsonString, HotDateItem.class);
        return dateItem;
    }


    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDate_id() {
        return date_id;
    }

    public void setDate_id(String date_id) {
        this.date_id = date_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getDate_person_num() {
        return date_person_num;
    }

    public void setDate_person_num(int date_person_num) {
        this.date_person_num = date_person_num;
    }

    public int getDate_join_num() {
        return date_join_num;
    }

    public void setDate_join_num(int date_join_num) {
        this.date_join_num = date_join_num;
    }

    public int getDate_time() {
        return date_time;
    }

    public void setDate_time(int date_time) {
        this.date_time = date_time;
    }

    public String getDate_location() {
        return date_location;
    }

    public void setDate_location(String date_location) {
        this.date_location = date_location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getDate_limit() {
        return date_limit;
    }

    public void setDate_limit(int date_limit) {
        this.date_limit = date_limit;
    }

    public DateTarget getDate_target() {
        return date_target;
    }

    public void setDate_target(DateTarget date_target) {
        this.date_target = date_target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollow_num() {
        return follow_num;
    }

    public void setFollow_num(int follow_num) {
        this.follow_num = follow_num;
    }

    public int getRemark_num() {
        return remark_num;
    }

    public void setRemark_num(int remark_num) {
        this.remark_num = remark_num;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

}