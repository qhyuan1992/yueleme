package com.mini.yueleme.data;

/**
 * Created by weiersyuan on 2016/7/23.
 */
public class QQUserInfo {
    private String userImageUrl;
    private String openID;
    private String nikeName;

    public String getNikeName() {
        return nikeName;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public String getOpenID() {
        return openID;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public void setOpenID(String gender) {
        this.openID = gender;
    }

    @Override
    public String toString() {
        return "[userImageUrl:" + userImageUrl + " openID:" + openID + " nikeName:" + nikeName;
    }
}
