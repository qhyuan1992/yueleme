package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by weiersyuan on 2016/7/24.
 * 要缓存
 */
public class FollowedUser extends DataSupport{

    private String user_name;

    private String image_url;

    private String group_name;

    private int sex;

    private int age;

    private String tel;

    private String sign;

    public static List<FollowedUser> fromJson2List(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Type type = new TypeToken<List<DateItem>>(){}.getType();
        List<FollowedUser> followedUsers = gson.fromJson(jsonString, type);
        return followedUsers;
    }

    public static FollowedUser fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        FollowedUser followedUser = gson.fromJson(jsonString, FollowedUser.class);
        return followedUser;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
