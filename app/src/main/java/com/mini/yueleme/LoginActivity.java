package com.mini.yueleme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mini.yueleme.data.QQUserInfo;
import com.mini.yueleme.utils.Constant;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by weiersyuan on 2016/7/23.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private YLMApplication application;
    private UserInfo qqUserInfo;
    private CircleImageView imageView;

    private static String openID;

    private SVProgressHUD loginDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        application = (YLMApplication) getApplication();

        imageView = (CircleImageView) findViewById(R.id.qqLogo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!application.tencent.isSessionValid()) {
                    application.tencent.login(LoginActivity.this, "all", loginListener);
                }
            }
        });
    }

    private IUiListener loginListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            //根据拿到的openID和token获取更多信息
            initOpenidAndToken((JSONObject) response);
            qqUserInfo = new UserInfo(LoginActivity.this, application.tencent.getQQToken());
            qqUserInfo.getUserInfo(qqUserInfolistener);
        }
        @Override
        public void onError(UiError e) {

        }
        @Override
        public void onCancel() {

        }
    };

    private IUiListener qqUserInfolistener = new IUiListener() {
        @Override
        public void onError(UiError e) {
            Log.d(TAG, "获取用户信息失败");
        }

        @Override
        public void onComplete(final Object response) {
            JSONObject qqUserInfoJson = (JSONObject)response;
//            application.showToastMsg(qqUserInfoJson.toString());
            QQUserInfo qqUserInfo = new QQUserInfo();
            try {
                if (qqUserInfoJson.has("figureurl")) {
                    qqUserInfo.setUserImageUrl(qqUserInfoJson.getString("figureurl_qq_2"));
                }
                if (qqUserInfoJson.has("nickname")) {
                    qqUserInfo.setNikeName(qqUserInfoJson.getString("nickname"));
                }
                qqUserInfo.setOpenID(openID);
//                application.showToastMsg(qqUserInfo.toString());
                // TODO:在这里进行登录逻辑
                login(qqUserInfo);
            } catch (JSONException e) {
                Log.d(TAG, "JSON解析错误");
            }
        }
        @Override
        public void onCancel() {
        }
    };

    // 登录
    private void login(final QQUserInfo qqUserInfo) throws JSONException {
        loginDialog = new SVProgressHUD(LoginActivity.this);
        loginDialog.showWithStatus("正在登录...");
        // 登录成功后将将服务端返回的UID保存到本地，下次就可以直接进入主界面
        JSONObject loginParams = new JSONObject();
        loginParams.put("user_name", qqUserInfo.getNikeName());
        loginParams.put("image_url", qqUserInfo.getUserImageUrl());
        loginParams.put("unique_token",qqUserInfo.getOpenID());
        Log.i(TAG, loginParams.toString());
        JsonObjectRequest loginJsonPostRequest = new JsonObjectRequest(Request.Method.POST, Constant.LOGIN_URL, loginParams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                // 退出时调用
                //application.tencent.logout(LoginActivity.this);
                String uid = "";
                try {
                    if(jsonObject.getInt("code") == 0) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        uid = dataObject.getString("user_id");
                    }
                } catch (JSONException e) {
                    Log.i(TAG,TAG + "JSON解析失败");
                    e.printStackTrace();
                }
                application.userID = uid;
                application.userName = qqUserInfo.getNikeName();
                application.userImageUrl = qqUserInfo.getUserImageUrl();

                application.prefUtils.restoreString(Constant.USER_TOKEN,qqUserInfo.getOpenID());
                application.prefUtils.restoreString(Constant.UID_KEY, application.userID);
                application.prefUtils.restoreString(Constant.USER_NAME, application.userName);
                application.prefUtils.restoreString(Constant.USER_IMAGE_URL, application.userImageUrl);

                loginDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                },1000);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loginDialog.dismiss();
                application.showToastMsg("服务器异常,请稍后再试");
                application.tencent.logout(LoginActivity.this);
            }
        }
        );
        application.requestQueue.add(loginJsonPostRequest);
    }

    public void initOpenidAndToken(JSONObject jsonObject) {
        try {
            openID = jsonObject.getString(Constants.PARAM_OPEN_ID);
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                application.tencent.setAccessToken(token, expires);
                application.tencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            application.tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}