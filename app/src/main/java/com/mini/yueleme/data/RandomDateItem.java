package com.mini.yueleme.data;

/**
 * Created by bumblebee on 16/7/29.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by weiersyuan on 2016/7/26.
 */
public class RandomDateItem extends DataSupport {

    private String image_url;

    private String date_id;

    private String subject;

    private int date_person_num;

    private int date_join_num;

    private int date_time;

    private String date_location;

    private String user_id;

    private String user_name;

    private String group_name;/*group???*/

    private int sex;

    private int pay;

    private int single;

//    private int date_limit;

    private String date_target;

    /*private String description; *//*simple_description*/

    private int follow_num;

    private int remark_num;

//    private int date_status;

    public static List<RandomDateItem> fromJson2List(String jsonString){
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<DateItem>>() {}.getType();
        List<RandomDateItem> dateItems = gson.fromJson(jsonString, type);
        return dateItems;
    }

    public static RandomDateItem fromJson(String jsonString){
        Gson gson = new GsonBuilder().create();
        RandomDateItem randomDateItem = gson.fromJson(jsonString, RandomDateItem.class);
        return randomDateItem;
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

    public int getdate_person_num() {
        return date_person_num;
    }

    public void setdate_person_num(int date_person_num) {
        this.date_person_num = date_person_num;
    }

    public int getDate_join_num() {
        return date_join_num;
    }

    public void setDate_join_num(int date_join_nub) {
        this.date_join_num = date_join_nub;
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


    public String  getDate_target() {
        return date_target;
    }

    public void setDate_target(String  date_target) {
        this.date_target = date_target;
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

    public void setDate_person_num(int date_person_num) {
        this.date_person_num = date_person_num;
    }

    public void setSingle(int single) {
        this.single = single;
    }

    public int getDate_person_num() {

        return date_person_num;
    }

    public int getSingle() {
        return single;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

