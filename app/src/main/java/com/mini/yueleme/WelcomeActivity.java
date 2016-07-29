package com.mini.yueleme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.mini.yueleme.utils.Constant;

public class WelcomeActivity extends Activity implements AnimationListener {
	private View welcomeView;
	private boolean isFirstIn;
	private YLMApplication application;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		application = (YLMApplication) getApplication();
		isFirstIn = application.prefUtils.getBooleanTrue("isFirstIn");
		welcomeView = findViewById(R.id.ll);
		Animation welAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_animation);
		welcomeView.startAnimation(welAnimation);
		welAnimation.setAnimationListener(this);
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		finish();
		if (isFirstIn) {
			goSplash();
			application.prefUtils.restoreBoolean("isFirstIn", false);
		} else {
			goMain();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	private void goMain() {
		// 首次登录进入注册界面
		Intent intent = new Intent();
		if (application.prefUtils.getString(Constant.UID_KEY).equals("")) {
			intent.setClass(this, LoginActivity.class);
		} else {
			intent.setClass(this, MainActivity.class);
			application.userID = application.prefUtils.getString(Constant.UID_KEY);
			application.userName = application.prefUtils.getString(Constant.USER_NAME);
			application.userImageUrl = application.prefUtils.getString(Constant.USER_IMAGE_URL);
		}
		startActivity(intent);
	}

	private void goSplash() {
		Intent intent = new Intent(this, SplashActivity.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {

	}
}
