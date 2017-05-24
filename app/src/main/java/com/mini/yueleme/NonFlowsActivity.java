package com.mini.yueleme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mini.yueleme.webserver.IPUtil;
import com.mini.yueleme.webserver.WebService;
import com.mini.yueleme.webserver.WifiApAdmin;
import com.mini.yueleme.webserver.WifiConfigurationFactory;
import com.mini.yueleme.webserver.YLMWifiManager;

public class NonFlowsActivity extends Activity {

	private YLMWifiManager mWifiManager;
	private TextView tvMyHotpointName;
	private TextView tvNet;
	private ImageView imageView;
	private YLMApplication myApplication;

	protected static final int AP_OPEN_SUCCESS = 0;
	protected static final int AP_OPEN_FAIL = -1;

	private WifiApAdmin mWifiApAdmin;
	private String hot_name = "ylm_wifi";

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// hideLoadingBar();
			try {
				switch (msg.what) {
				case AP_OPEN_SUCCESS:
					String ip = IPUtil.getLocalIpAddress(NonFlowsActivity.this);
					initServer(ip);
					break;
				case AP_OPEN_FAIL:
					Toast.makeText(NonFlowsActivity.this,
							"AP 开启失败，请退出重试，或检查权限", Toast.LENGTH_SHORT).show();
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nonflowshare);
		myApplication = (YLMApplication) getApplication();
		// displayLoadingBar();
		tvNet = (TextView) findViewById(R.id.tv_net);
		tvMyHotpointName = (TextView) findViewById(R.id.tv_my_hotpoint_name);
		imageView = (ImageView) findViewById(R.id.iv_qrcode);

		mWifiManager = new YLMWifiManager(this);
		StopServiceR.isQuit = false;
		mWifiApAdmin = new WifiApAdmin(this);
		mWifiManager.closeWifi();
		startAP();
	}

	private void initServer(String ip) {
		tvNet.setText("http://" + ip + ":8888");
		imageView.setImageResource(R.drawable.share_qc_icon);
		if (mWifiApAdmin.isWifiApEnabled()) {
			tvMyHotpointName.setText(WifiApAdmin.AP_SSID);
		} else {
			YLMWifiManager cnmWifiManager = new YLMWifiManager(this);
			WifiInfo wifiInfo = cnmWifiManager.getConnectionInfo();
			tvMyHotpointName.setText(wifiInfo.getSSID());
		}

		Intent intent = new Intent(this.getApplicationContext(),
				WebService.class);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeAP();
		StopServiceR.isQuit = true;
		new Handler().postDelayed(
				new StopServiceR(this.getApplicationContext()), 180000);// 3min
	}

	public static class StopServiceR implements Runnable {
		private Context context;
		public static boolean isQuit;

		private StopServiceR(Context c) {
			super();
			this.context = c;
		}

		@Override
		public void run() {
			if (isQuit) {
				Intent intent = new Intent(context, WebService.class);
				context.stopService(intent);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startAP() {
		boolean isApOpen = mWifiApAdmin.isWifiApEnabled();
		if (isApOpen) {
			return;
		}
		// sendBroadcast(new Intent(ApBroadcast.ACTION_AP_OPENING));
		mWifiApAdmin.startWifiAp(hot_name,
				WifiConfigurationFactory.PasswordType.NO_PASSWD_TYPE, null);
		myApplication.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					int count = 0;
					while (!mWifiApAdmin.isWifiApEnabled() && count < 20) {
						Thread.sleep(500);
						count++;
					}
					if (count == 20) {
						mWifiApAdmin
								.startWifiAp(
										hot_name,
										WifiConfigurationFactory.PasswordType.NO_PASSWD_TYPE,
										null);
					}
					while (!mWifiApAdmin.isWifiApEnabled() && count > 0) {
						Thread.sleep(500);
						count--;
					}
					if (count == 0) {
						handler.sendEmptyMessage(AP_OPEN_FAIL);
					} else {
						handler.sendEmptyMessage(AP_OPEN_SUCCESS);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void closeAP() {
		boolean isApOpen = mWifiApAdmin.isWifiApEnabled();
		if (!isApOpen) {
			return;
		}
		// myApplication.sendBroadcast(new
		// Intent(ApBroadcast.ACTION_AP_CLOSING));// 发送热点关闭消息
		mWifiApAdmin.closeWifiAp();
		myApplication.executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (mWifiApAdmin.isWifiApEnabled()) {
						Thread.sleep(200);
					}
					mWifiManager.openWifi();
					while (!(mWifiManager.checkState() == WifiManager.WIFI_STATE_ENABLED)) {
						Thread.sleep(200);
					}
					// myApplication.sendBroadcast(new Intent(ApBroadcast.ACTION_AP_CLOSED));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		closeAP();
		super.onBackPressed();
	}
}
