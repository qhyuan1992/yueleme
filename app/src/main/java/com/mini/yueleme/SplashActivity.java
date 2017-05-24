package com.mini.yueleme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mini.yueleme.adapter.SplashAdapter;
public class SplashActivity extends Activity {
	private int[] imageIds;
	private ViewPager vp;
	private List<ImageView> imageList = new ArrayList<ImageView>();
	private int oldDotPos = 0;
	private GestureDetector detector;
	private ImageView begin_exp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		detector = new GestureDetector(gestureListener);
		imageIds = new int[] { R.drawable.splash_1, R.drawable.splash_2};
		initImageList();
		begin_exp = (ImageView) findViewById(R.id.begin_exp);
		vp = (ViewPager) findViewById(R.id.vp);
		vp.setAdapter(new SplashAdapter(imageList));
		vp.setOnPageChangeListener(pageChangeListener);
		begin_exp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goMain();
			}

		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
							   float velocityY) {
//			if (vp.getCurrentItem() == 2) {
//				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
//						- e2.getY())
//						&& (e1.getX() - e2.getX() <= (-50) || e1.getX()
//								- e2.getX() >= 50)) {
//					if (e1.getX() - e2.getX() >= 50) {
//						//goMain();
//						return true;
//					}
//				}
//			}
			return false;
		}
	};
	private void goMain() {
		// 首次登录进入注册界面
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		private Animation animation;
		@Override
		public void onPageSelected(int position) {
			oldDotPos = position;
			if (1 == position) {
				animation = AnimationUtils.loadAnimation(SplashActivity.this,
						R.anim.welcome_animation);
//				welcomeView.startAnimation(welAnimation);
				begin_exp.setAnimation(animation);
				begin_exp.setVisibility(View.VISIBLE);
			}else {
				begin_exp.setAnimation(animation);
				begin_exp.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private void initImageList() {
		for (int i = 0; i < imageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imageIds[i]);
			imageList.add(imageView);
		}
	}
}
