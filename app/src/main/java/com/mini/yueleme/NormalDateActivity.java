package com.mini.yueleme;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by weiersyuan on 2016/7/26.
 */
public class NormalDateActivity extends  BaseActivity implements View.OnClickListener{

    private static final String TAG = "NormalDateActivity";
    private EditText subject;
    private EditText boyNum;
    private EditText girlNum;
    private TextView dateTime;
    private EditText datePosition;
    private EditText dateDetail;
    private RadioGroup payMethod;
    private ImageView publish;
    private SVProgressHUD  publishDialog;
    private TimePickerView pvTime;
    private long selectedDateTimeSecond = System.currentTimeMillis() / 1000;
    private Spinner spinnerDepartment;

    private String department = "sng";

    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_publish_normal_date);
        publishDialog = new SVProgressHUD(context);
        pvTime = new TimePickerView(this, TimePickerView.Type.ALL);
        initView();
    }

    private void initView() {
        spinnerDepartment = (Spinner)findViewById(R.id.spinner_department);
        subject = (EditText)findViewById(R.id.et_subject);
        boyNum = (EditText)findViewById(R.id.et_boy_num);
        girlNum = (EditText)findViewById(R.id.et_girl_num);
        payMethod = (RadioGroup)findViewById(R.id.rg_pay);
        dateTime = (TextView) findViewById(R.id.tv_date_time);
        datePosition = (EditText)findViewById(R.id.et_date_position);
        dateDetail = (EditText) findViewById(R.id.et_date_detail);
        publish = (ImageView) findViewById(R.id.tv_publish_normal_date);
        publish.setOnClickListener(this);
        dateTime.setOnClickListener(this);
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] departmentArr = application.getResources().getStringArray(R.array.department);
                department = departmentArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                department = "sng";
            }
        });
    }

    @Override
    protected void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight) {
        ivLeft.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("发起约单");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_close, 0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_publish_normal_date) {
            publishNormalDate();
        } else if (v.getId() == R.id.tv_date_time) {
            showTimePicker();
        }
    }

    private void showTimePicker() {
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                selectedDateTimeSecond = date.getTime() / 1000;
                dateTime.setText(TimeUtil.long2DateString(selectedDateTimeSecond));
                Log.i(TAG, selectedDateTimeSecond+ "");
            }
        });
        pvTime.show();
    }

    private void publishNormalDate() {
        if (!checkParams()) {
            return;
        }

        publishDialog.showWithStatus("正在发布");
        JSONObject publishNromalDateParams = generatDateJsonObject();

        JsonObjectRequest publishNromalDateJsonRequest = new JsonObjectRequest(Request.Method.POST, Constant.PUBLISH_NORMAL_DATE, publishNromalDateParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        publishDialog.showSuccessWithStatus("发布成功！");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                overridePendingTransition(R.anim.activity_close, 0);
                            }
                        }, 1000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                publishDialog.showErrorWithStatus("服务器异常，发布失败！");
            }
        }
        );
        application.requestQueue.add(publishNromalDateJsonRequest);
    }

    private JSONObject generatDateJsonObject() {
        JSONObject publishNromalDateParams = new JSONObject();
        try {
            publishNromalDateParams.put("user_name",application.userName);
            publishNromalDateParams.put("image_url",application.userImageUrl);
            publishNromalDateParams.put("user_id",application.userID);
            publishNromalDateParams.put("create_time",System.currentTimeMillis()/1000);
            publishNromalDateParams.put("group_name",department.trim());
            publishNromalDateParams.put("subject",subject.getText().toString().trim());
            int total = Integer.valueOf(boyNum.getText().toString().trim()) + Integer.valueOf(girlNum.getText().toString().trim());
            publishNromalDateParams.put("date_person_nub",total);
            publishNromalDateParams.put("date_time",selectedDateTimeSecond);
            publishNromalDateParams.put("date_location",datePosition.getText().toString().trim());
            publishNromalDateParams.put("pay",0);
            publishNromalDateParams.put("date_limit",1);
            publishNromalDateParams.put("detail_description",dateDetail.getText().toString());
            JSONObject publishNromalDateTargetParams = new JSONObject();
            publishNromalDateTargetParams.put("boy", Integer.valueOf(boyNum.getText().toString().trim()));
            publishNromalDateTargetParams.put("girl", Integer.valueOf(girlNum.getText().toString().trim()));
            publishNromalDateParams.put("date_target", publishNromalDateTargetParams);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "JSON解析错误");
        }
        Log.i(TAG, publishNromalDateParams.toString());
        return publishNromalDateParams;
    }

    private boolean checkParams() {
        if (subject.getText().toString().trim().equals("")) {
            application.showToastMsg("主题不能为空");
            return false;
        }
        if (datePosition.getText().toString().trim().equals("")) {
            application.showToastMsg("地点不能为空");
            return false;
        }
        if (dateDetail.getText().toString().trim().equals("")) {
            application.showToastMsg("请填写详细信息更容易被别人注意到哦");
            return false;
        }
        try {
            Integer.valueOf(boyNum.getText().toString().trim());
            Integer.valueOf(girlNum.getText().toString().trim());
        } catch (NumberFormatException e) {
            application.showToastMsg("人数参数格式无效");
            return false;
        }
        return true;
    }
}
