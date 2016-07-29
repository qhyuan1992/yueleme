package com.mini.yueleme;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mini.yueleme.R;

/**
 * Created by weiersyuan on 2016/7/22.
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_setting);
    }


    @Override
    protected void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight) {
        ivLeft.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText("设置");
    }
}
