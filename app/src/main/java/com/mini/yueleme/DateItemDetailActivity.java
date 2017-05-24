package com.mini.yueleme;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mini.yueleme.adapter.CommentAdapter;
import com.mini.yueleme.adapter.DateItemAdapter;
import com.mini.yueleme.data.NewDateItemDetail;
import com.mini.yueleme.data.RemarkItem;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by weiersyuan on 2016/7/27.
 */
public class DateItemDetailActivity  extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "DateItemDetailActivity";

    private CommentAdapter adapter;

    private SVProgressHUD joinDialog;
    private NewDateItemDetail detailItem;

    private String dateId;
    private CircleImageView detailUserImage;
    private TextView detailSubject;
    private TextView detailDatePosition;
    private TextView detailDateTime;
    private TextView detailUserName;
    private TextView detailRemark;
    private TextView detailAgree;
    private TextView detailDiscription;
    private TextView detailPayMethod;
    private TextView detailDepartment;
    private TextView detailSexNeed;
    private LinearLayout ll_date_detail;
    private SVProgressHUD syncDialog;
    private LinearLayout ll_bottom;
    private RecyclerView lvRemarks;
    private ImageView publishJoin;
    private TextView tv_added;
    private TextView tvCreateTime;
    private View layoutRemark;
    private View layoutJoin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_dateitem_detail);
        syncDialog = new SVProgressHUD(context);
        syncDialog.showWithStatus("正在加载...");
        Bundle bundle = getIntent().getExtras();
        initView();
        joinDialog = new SVProgressHUD(context);
        if (bundle != null) {
            dateId = bundle.getString("date_id");
            // 网络请求
            getDateItemDetail(dateId);
        }
    }

    private void initView() {
        tvCreateTime = (TextView)findViewById(R.id.tv_datedetail_create_time);
        lvRemarks = (RecyclerView) findViewById(R.id.rv_remark);

        lvRemarks.setLayoutManager(new LinearLayoutManager(this));
        ll_date_detail = (LinearLayout)findViewById(R.id.ll_detail);
        ll_bottom = (LinearLayout)findViewById(R.id.ll_bottom);
        ll_bottom.setOnClickListener(this);
        publishJoin = (ImageView) findViewById(R.id.iv_attend);
        publishJoin.setOnClickListener(this);

        detailUserImage = (CircleImageView) findViewById(R.id.iv_datedetail_userimage);
        detailUserName = (TextView) findViewById(R.id.tv_datedetail_username);
        detailSubject = (TextView) findViewById(R.id.tv_datedetail_subject);
        detailDatePosition = (TextView) findViewById(R.id.tv_datedetail_location);
        detailDateTime = (TextView) findViewById(R.id.tv_datedetail_datetime);
        detailDiscription = (TextView) findViewById(R.id.tv_date_detail_date_descripsion);
        detailPayMethod = (TextView) findViewById(R.id.tvdatedetail__pay_method);
        detailDepartment = (TextView) findViewById(R.id.tv_datedetail_department);
        detailSexNeed = (TextView) findViewById(R.id.tv_datedetail_sexneed);
        detailAgree = (TextView) findViewById(R.id.tv_datedetail_agree);
        detailRemark = (TextView) findViewById(R.id.tv_datedetail_remark_num);
    }

    private void getDateItemDetail(String dateId) {
        JsonObjectRequest getDateItemsPostRequest = new JsonObjectRequest(Constant.GET_DATE_ITEM_DETAIL + "?date_id=" + dateId + "&user_id=" + application.userID, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        updateDateItemsDetail(jsonObject);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                application.showToastMsg("服务器异常...");
            }
        }
        );
        application.requestQueue.add(getDateItemsPostRequest);
    }

    // 通过网络获取新的条目更新界面
    private void updateDateItemsDetail(JSONObject jsonObject) {
        try {
            if (jsonObject.getInt(Constant.ERROR_CODE) == 0) {
                // 转化为Detail类
                detailItem = NewDateItemDetail.fromJson(jsonObject.getJSONObject(Constant.DATA).toString());
                updateUI(detailItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 设置详情页的内容
    private void updateUI(NewDateItemDetail detail) {
        layoutRemark = findViewById(R.id.layout_comment);
        layoutJoin = findViewById(R.id.layout_attend);
        layoutJoin.setOnClickListener(this);
        layoutRemark.setOnClickListener(this);

        tv_added = (TextView)findViewById(R.id.tv_attend);
        ll_date_detail.setVisibility(View.VISIBLE);
        ll_bottom.setVisibility(View.VISIBLE);
        syncDialog.dismiss();
        ImageLoader.getInstance().displayImage(detail.getImage_url(), detailUserImage);
        detailUserName.setText(detail.getUser_name());
        detailSubject.setText(detail.getSubject());
        detailDatePosition.setText(detail.getDate_location());
        detailDateTime.setText(TimeUtil.long2DateString(detail.getDate_time()));
        detailDiscription.setText(detail.getDescription());
        detailPayMethod.setText(DateItemAdapter.fromPay(detail.getPay()));
        detailDepartment.setText(detail.getGroup_name());
        detailSexNeed.setText(DateItemAdapter.formSexNeed(detail.getDate_target().getBoy(), detail.getDate_target().getGirl()));
        detailAgree.setText(detail.getDate_join_num() + "");
        detailRemark.setText(detail.getRemark_num() + "");
        tvCreateTime.setText("发表于" + TimeUtil.long2DateString(detail.getCreate_time()*1000));

        if (detail.getRemarks() == null) {
            Log.d(TAG, "remark is null");
        } else {
            adapter = new CommentAdapter(detail.getRemarks());
            lvRemarks.setAdapter(adapter);
        }
        if (detail.getUser_status() ==1) {
            layoutJoin.setClickable(false);
            tv_added.setTextColor(Color.GRAY);
            publishJoin.setImageResource(R.drawable.iv_join_disable);
        }
    }

    @Override
    protected void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight) {
        ivLeft.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("约单详情");
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.selector_share);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_attend:
                if (!v.isEnabled()) {
                    application.showToastMsg("无法参与此约会");
                    return;
                }
                if (detailItem.getdate_person_num() == detailItem.getDate_join_num()) {
                    application.showToastMsg("报名人数已满,无法参与");
                    return;
                }
                if (application.userID == detailItem.getUser_id()) {
                    application.showToastMsg("不能参与自己发起的约会哦");
                    return;
                }
                // 加入
                JsonObjectRequest getDateItemsPostRequest = new JsonObjectRequest(Constant.REQUEST_JOIN +
                        "?date_id=" + dateId + "&user_id=" + application.userID, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.getInt("code") == 0) {
                                        joinDialog.showSuccessWithStatus("报名成功");
                                    }
                                } catch (JSONException e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        application.showToastMsg("服务器异常...");
                    }
                }
                );
                application.requestQueue.add(getDateItemsPostRequest);
                break;

            case R.id.layout_comment:
                Log.d(TAG, "评论");
                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this).setTitle("评论").
                        setIcon(android.R.drawable.ic_dialog_info).
                        setView(editText).setPositiveButton("评论", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String comment = editText.getText().toString();
                        JSONObject infoParams = new JSONObject();
                        try {
                            infoParams.put("date_id", dateId);
                            infoParams.put("user_id", application.userID);
                            infoParams.put("time", System.currentTimeMillis() / 1000);
                            infoParams.put("content", comment);
                            infoParams.put("user_name", application.userName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JsonObjectRequest infoJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.PUBLISH_COMMENT, infoParams,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        int code = -1;
                                        try {
                                            code = jsonObject.getInt("code");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if(0 == code) {
                                            syncDialog.showSuccessWithStatus("评论成功");
                                            // 更新本地存储的用户信息
                                            detailItem.getRemarks().add(new RemarkItem(comment, application.userName));
                                            CommentAdapter adapter = new CommentAdapter(detailItem.getRemarks());
                                            lvRemarks.setAdapter(adapter);

                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                syncDialog.showSuccessWithStatus("提交失败");
                            }
                        });
                        application.requestQueue.add(infoJsonObjectRequest);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
                break;
            case R.id.iv_right:
                share2QQFriend();
                break;
        }
    }

    // 分享给QQ好友
    public void share2QQFriend() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "快下载约了么");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "快下载约了么快下载约了么快下载约了么");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://qq.com");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,"约了么");
        application.tencent.shareToQQ(this, params, qqShareListener);
    }

    // 分享到QQZone
    public void share2QQZone() {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "快下载约了么");
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "快下载约了么快下载约了么快下载约了么");
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://qq.com");
        application.tencent.shareToQzone(this, params, qqShareListener);
    }

    // 分享结果的回调
    IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            application.showToastMsg("分享已取消");
        }
        @Override
        public void onComplete(Object response) {
            application.showToastMsg("分享完成");
        }
        @Override
        public void onError(UiError e) {
        }
    };
}
