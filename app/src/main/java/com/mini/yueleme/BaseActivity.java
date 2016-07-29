package com.mini.yueleme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	public YLMApplication application;
	protected TextView title;
	protected ImageView ivLeft;
	protected ImageView ivRight;
	protected TextView tvRight;
	protected Context context;
	protected ImageView midIv;

	public void onCreate(Bundle savedInstanceState, int layoutResID){
		super.onCreate(savedInstanceState);
		setContentView(layoutResID);
		application = (YLMApplication) getApplication();
		init();
		context = this;
	}
	
	private void init() {
		ivLeft = (ImageView) findViewById(R.id.iv_left);
		title = (TextView) findViewById(R.id.tv_title);
		midIv = (ImageView) findViewById(R.id.iv_middle);
		ivRight = (ImageView) findViewById(R.id.iv_right);
		tvRight = (TextView) findViewById(R.id.tv_right);

		ivLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		setTitle(ivLeft, title, midIv, ivRight, tvRight);
	}
	
	protected abstract void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight);
}
