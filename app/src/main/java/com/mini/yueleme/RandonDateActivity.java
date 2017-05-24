package com.mini.yueleme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.mini.yueleme.data.RandomDateItem;
import com.mini.yueleme.utils.Constant;
import com.mini.yueleme.utils.TimeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by weiersyuan on 2016/7/27.
 */
public class RandonDateActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivPublish;
    private long selectedBeginDateTimeSecond = System.currentTimeMillis() / 1000;
    private long selectedEndDateTimeSecond = System.currentTimeMillis() / 1000;

    private String Tag = "Bumble Bee";
    private String BG;
    private TextView tvBeginTime;
    private TextView tvEndTime;
    private Spinner spinner;
    private RadioButton rbOneToOne, rbOneToMany, rbIPay, rbAA;
    private SVProgressHUD syncDialog;



    private int payMethod = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_publish_random_date);

        tvBeginTime = (TextView) findViewById(R.id.tv_date_begin_time);
        tvEndTime = (TextView)findViewById(R.id.tv_date_end_time);
        ivPublish = (ImageView) findViewById(R.id.tv_publish_random_date);
        rbOneToOne = (RadioButton)findViewById(R.id.rb_oneToOne);
        rbOneToMany = (RadioButton)findViewById(R.id.rb_oneToMany);
        rbIPay = (RadioButton)findViewById(R.id.rb_iPay);
        rbAA = (RadioButton)findViewById(R.id.rb_AA);
        spinner = (Spinner)findViewById(R.id.spinner_department);
        syncDialog = new SVProgressHUD(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BG = getResources().getStringArray(R.array.department)[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvBeginTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        ivPublish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_begin_time :
                showBeginTimePicker();
                break;
            case R.id.tv_date_end_time :
                showEndTimePicker();
                break;
            case R.id.tv_publish_random_date :
                queryRandomDate();
                break;
        }
    }

    private void queryRandomDate() {

        Log.d(Tag, "in queryRandomDate");

        if (!rbOneToOne.isChecked() && !rbOneToMany.isChecked()) {
            Toast.makeText(this, "请选择约会人数", Toast.LENGTH_SHORT).show();
        } else if (!rbIPay.isChecked() && !rbAA.isChecked()) {
            Toast.makeText(this, "请选择付款方式", Toast.LENGTH_SHORT).show();
        } else {
            int person = getAppointmentPerson();
            int payWay = getPayWay();

            JSONObject infoParams = new JSONObject();
            try {
                infoParams.put("user_id", application.userID);
                infoParams.put("begin_time", selectedBeginDateTimeSecond);
                infoParams.put("end_time", selectedEndDateTimeSecond);
                infoParams.put("pay", payWay);
                infoParams.put("group_name", BG);
                infoParams.put("match_limit", person);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest infoJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://123.206.78.161/cgi-bin/DateMatch.cgi", infoParams,
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
                                RandomDateItem item;
                                syncDialog.showSuccessWithStatus("提交成功");
                                Log.d(Tag, jsonObject.toString());
                                try {
                                    item = RandomDateItem.fromJson(jsonObject.get("data").toString());
                                    Log.d(Tag, item.getDate_id());
                                    Bundle bundle = new Bundle();
                                    bundle.putString("date_id", item.getDate_id());
                                    Intent intent = new Intent(RandonDateActivity.this, DateItemDetailActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } if (-1 == code) {
                                syncDialog.showSuccessWithStatus("无合适条目");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    application.showToastMsg("网络错误，上传信息失败");
                    syncDialog.showSuccessWithStatus("提交失败");
                }
            });
            application.requestQueue.add(infoJsonObjectRequest);


        }

    }

    private int getAppointmentPerson() {
        if (rbOneToOne.isChecked()) {
            return 0;
        }else {
            return 1;
        }
    }

    private int getPayWay() {
        if (rbAA.isChecked()) {
            return 1;
        }else {
            return 0;
        }
    }

    private void showBeginTimePicker() {
        TimePickerView pvTimeBegin = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTimeBegin.setTime(new Date());
        pvTimeBegin.setCyclic(false);
        pvTimeBegin.setCancelable(true);
        //时间选择后回调
        pvTimeBegin.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                selectedBeginDateTimeSecond = date.getTime() / 1000;
                tvBeginTime.setText(TimeUtil.long2DateString(selectedBeginDateTimeSecond));

            }
        });
        pvTimeBegin.show();
    }

    private void showEndTimePicker() {
        TimePickerView pvTimeEnd = new TimePickerView(this, TimePickerView.Type.ALL);
        pvTimeEnd.setTime(new Date());
        pvTimeEnd.setCyclic(false);
        pvTimeEnd.setCancelable(true);
        pvTimeEnd.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(java.util.Date date) {
                selectedEndDateTimeSecond = date.getTime() / 1000;
                tvEndTime.setText(TimeUtil.long2DateString(selectedEndDateTimeSecond));
            }
        });
        pvTimeEnd.show();
    }

    @Override
    protected void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight) {
        ivLeft.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("随机配友");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.activity_close, 0);
            }
        });
    }



}
