package com.mini.yueleme;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mini.yueleme.fragment.DatingItemFragment;
import com.mini.yueleme.fragment.HomeFragment;
import com.mini.yueleme.fragment.MessageFragment;
import com.mini.yueleme.fragment.MineFragment;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

public class MainActivity extends BaseActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener{

	private FragmentManager fragmentManager;
	private HomeFragment homeFragment;
	private MessageFragment messageFragment;
	private DatingItemFragment datingItemFragment;
	private MineFragment mineFragment;
	private ImageView publishIv;
	private PopupWindow popWnd;
	private RadioGroup main_tab;

	private Button randomPublish;
	private Button normalPublish;

	private YLMApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_main);
		application = (YLMApplication) getApplication();
		fragmentManager = getFragmentManager();
		initView();
		fragmentManager.beginTransaction().replace(R.id.maincontainer, homeFragment).commit();
		changeHeaderStatus(0);
		initPublish();
	}

	private void initView() {
		main_tab = (RadioGroup) findViewById(R.id.main_tab);
		publishIv = (ImageView) findViewById(R.id.iv_publish);

		homeFragment = new HomeFragment();
		messageFragment = new MessageFragment();
		datingItemFragment = new DatingItemFragment();
		mineFragment = new MineFragment();

		publishIv.setOnClickListener(this);
		publishIv.setOnClickListener(this);
		main_tab.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		XGPushClickedResult click = XGPushManager.onActivityStarted(this);
		if (click != null) {
			fragmentManager.beginTransaction().replace(R.id.maincontainer, messageFragment).commit();
			changeHeaderStatus(1);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		XGPushManager.onActivityStoped(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_publish:
				showPublish();
				break;
			case R.id.btn_normalPublishDate:
				popWnd.dismiss();
				publishNormalDate();
				break;
			case R.id.btn_randomPublishDate:
				popWnd.dismiss();
				publishRandonDate();
				break;
			case R.id.iv_right:
				ivRight.setVisibility(View.GONE);
				tvRight.setVisibility(View.VISIBLE);
				mineFragment.startEditMode();
				break;
			case R.id.tv_right:
				ivRight.setVisibility(View.VISIBLE);
				tvRight.setVisibility(View.GONE);
				mineFragment.saveData();
				break;
		}
	}

	private void publishRandonDate() {
		startActivity(new Intent(context, RandonDateActivity.class));
		overridePendingTransition(R.anim.activity_open, 0);
	}

	private void publishNormalDate() {
		startActivity(new Intent(context, NormalDateActivity.class));
		overridePendingTransition(R.anim.activity_open, 0);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.rb_home:
				fragmentManager.beginTransaction().replace(R.id.maincontainer, homeFragment).commit();
				changeHeaderStatus(0);
				break;
			case R.id.rb_message:
				fragmentManager.beginTransaction().replace(R.id.maincontainer, messageFragment).commit();
				changeHeaderStatus(1);
				break;
			case R.id.rb_datelist:
				fragmentManager.beginTransaction().replace(R.id.maincontainer, datingItemFragment).commit();
				changeHeaderStatus(2);
				break;
			case R.id.rb_mine:
				fragmentManager.beginTransaction().replace(R.id.maincontainer, mineFragment).commit();
				changeHeaderStatus(3);
				break;
		}
	}

	private void changeHeaderStatus(int page) {
		switch (page) {
			case 0:
				ivLeft.setVisibility(View.GONE);
				midIv.setVisibility(View.VISIBLE);
				title.setVisibility(View.GONE);
				ivRight.setVisibility(View.GONE);
				tvRight.setVisibility(View.GONE);
				break;
			case 1:
				ivLeft.setVisibility(View.GONE);
				midIv.setVisibility(View.GONE);
				title.setVisibility(View.VISIBLE);
				ivRight.setVisibility(View.GONE);
				tvRight.setVisibility(View.GONE);
				title.setText("消息中心");
				break;
			case 2:
				ivLeft.setVisibility(View.GONE);
				midIv.setVisibility(View.GONE);
				title.setVisibility(View.VISIBLE);
				ivRight.setVisibility(View.GONE);
				tvRight.setVisibility(View.GONE);
				title.setText("约单");
				break;
			case 3:
				ivLeft.setVisibility(View.GONE);
				midIv.setVisibility(View.GONE);
				title.setVisibility(View.VISIBLE);
				ivRight.setVisibility(View.VISIBLE);
				tvRight.setVisibility(View.GONE);
				title.setText("个人中心");
				ivRight.setOnClickListener(this);
				tvRight.setOnClickListener(this);
				break;
		}
	}

	private void showPublish() {
		popWnd.showAtLocation(findViewById(R.id.maincontainer), Gravity.BOTTOM, 0, 100);
		backgroundAlpha(0.5f);
	}

	private void initPublish() {
		View popupView = getLayoutInflater().inflate(R.layout.publish_window, null);
		normalPublish = (Button) popupView.findViewById(R.id.btn_normalPublishDate);
		randomPublish = (Button) popupView.findViewById(R.id.btn_randomPublishDate);
		normalPublish.setOnClickListener(this);
		randomPublish.setOnClickListener(this);
		popWnd = new PopupWindow(popupView, RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT, true);
		popWnd.setTouchable(true);
		popWnd.setOutsideTouchable(true);
		popWnd.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		popWnd.setAnimationStyle(R.style.anim_menu_bottombar);
		popWnd.getContentView().setFocusableInTouchMode(true);
		// mPopupWindow.getContentView().setFocusable(true);
		popWnd.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
		popWnd.getContentView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					if (popWnd != null && popWnd.isShowing()) {
						popWnd.dismiss();
					}
					return true;
				}
				return false;
			}
		});
	}

	private void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	@Override
	protected void setTitle(ImageView ivLeft, TextView title, ImageView midIv, ImageView ivRight, TextView tvRight) {
		ivLeft.setVisibility(View.GONE);
		title.setVisibility(View.GONE);
		midIv.setVisibility(View.VISIBLE);
		ivRight.setVisibility(View.GONE);
		tvRight.setVisibility(View.GONE);
	}
}