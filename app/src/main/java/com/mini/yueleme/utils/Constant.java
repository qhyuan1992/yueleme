package com.mini.yueleme.utils;

/**
 * Created by weiersyuan on 2016/7/24.
 */
public class Constant {

    /**
     * 服务器返回错误码的key
     */
    public static final String ERROR_CODE = "code";
    public static final int ERROR_CODE_OK = 0;
    /**
     * 用户UID,user_name头像信息
     */
    public static final String UID_KEY = "uid";
    public static final String USER_NAME = "user_name";
    public static final String USER_IMAGE_URL = "image_url";
    /**
     * 用户TOKEN
     */
    public static final String USER_TOKEN = "token";
    public static final String DATA = "data";
    /**
     * 登录接口，post请求，json参数
     */
    public static final String LOGIN_URL = "http://123.206.78.161/cgi-bin/Login.cgi";
    /**
     * 获取最新最热接口，get请求，无参数
     */
    public static final String GET_NEW_DATE_ITEMS_URL = "http://123.206.78.161/cgi-bin/Latest.cgi";
    public static final String GET_HOT_DATE_ITEMS_URL = GET_NEW_DATE_ITEMS_URL;
    /**
     * 补全用户信息接口，post请求，json参数
     */
    public static final String INFO_SYNC = "http://123.206.78.161/cgi-bin/Complement.cgi";
    /**
     * 发布常规约单接口，post请求，json参数
     */
    public static final String PUBLISH_NORMAL_DATE = "http://123.206.78.161/cgi-bin/CreateDate.cgi";
    /**
     * 获取约单详情接口，get请求，参数：user_id和date_id
     */
    public static final String GET_DATE_ITEM_DETAIL = "http://123.206.78.161/cgi-bin/GetDateDetail.cgi";
    /**
     * 约单报名接口，get请求，参数：user_id和date_id
     */
    public static final String REQUEST_JOIN = "http://123.206.78.161/cgi-bin/ApplyDate.cgi";
    /**
     * 获取个人信息接口，get请求，参数：user_id
     */
    public static final String GET_JOIN_MESSAGE = "http://123.206.78.161/cgi-bin/GetJoinPersonInfo.cgi";
    /**
     * 获取用户参加的约会，get请求，参数user_id
     */
    public static final String GET_ME_JOINED = "http://123.206.78.161/cgi-bin/GetJoinDate.cgi?user_id=";

    /**
     * 获取用户发起的约会，get请求，参数user_id
     */
    public static String GET_ME_PUBLISHED = "http://123.206.78.161/cgi-bin/GetOrganizeDate.cgi?user_id=";

    /**
     * 发表评论，post请求，json参数
     */
    public static final String PUBLISH_COMMENT = "http://123.206.78.161/cgi-bin/Comment.cgi";
}
