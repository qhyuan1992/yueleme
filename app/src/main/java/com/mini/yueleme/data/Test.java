package com.mini.yueleme.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by weiersyuan on 2016/7/24.
 */
public class Test {

    private String image_url;

    private String unique_token;

    private String user_name;

    public static Test fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        Test test = gson.fromJson(jsonString, Test.class);
        return test;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUnique_token() {
        return unique_token;
    }

    public void setUnique_token(String unique_token) {
        this.unique_token = unique_token;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
