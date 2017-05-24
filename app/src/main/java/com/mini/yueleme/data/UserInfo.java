package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.litepal.crud.DataSupport;

/**
 * 用户信息，缓存到本地
 * Created by weiersyuan on 2016/7/24.
 * 要缓存
 */
public class UserInfo  {
    private String user_name;

    private String image_url;

    private String group;

    private int sex;

    private int age;

    private String tel;

    private String sign;

    private int single;

    public static UserInfo fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        UserInfo userInfo = gson.fromJson(jsonString, UserInfo.class);
        return userInfo;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }
}
