package com.mini.yueleme;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mini.yueleme.net.NetworkUtil;
import com.mini.yueleme.utils.PrefUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.tauth.Tencent;

import org.litepal.LitePalApplication;

public class YLMApplication extends LitePalApplication {

	public String userName;
	public String userImageUrl;
	public String userID;
	/**
	 * QQ登录接口的APPID
	 */
	public static String APP_ID = "1105558944";
	/**
	 * 本地保存数据
	 */
	public PrefUtil prefUtils;
	/**
	 * 全局线程池
	 */
	public ExecutorService executorService;
	/**
	 * QQ登录相关类
	 */
	public Tencent tencent;
	private Toast toast;
	/**
	 * 全局Volley请求队列
	 */
	public RequestQueue requestQueue;
	@Override
	public void onCreate() {
		super.onCreate();
		Context context = getApplicationContext();
		requestQueue = Volley.newRequestQueue(context);

		// QQ登录
		tencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
		executorService = Executors.newFixedThreadPool(5);
		prefUtils = new PrefUtil(this);

		// 信鸽推送
		XGPushManager.registerPush(context,prefUtils.getString("token"));

		// 初始化ImageLoader
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)//设置线程的优先级
				.denyCacheImageMultipleSizesInMemory()//当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//设置缓存文件的名字
				.discCacheFileCount(60)//缓存文件的最大个数
				.tasksProcessingOrder(QueueProcessingType.LIFO)// 设置图片下载和显示的工作队列排序
				.build();
		ImageLoader.getInstance().init(config);
	}

	// 显示Toast
	public void showToastMsg(String msg) {
		if (null == msg || "".equals(msg)) {
			return;
		}
		if (toast == null) {
			toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	public void showToastMsg(int msgID) {
		if (toast == null) {
			toast = Toast.makeText(this, this.getString(msgID), Toast.LENGTH_SHORT);
		} else {
			toast.setText(this.getString(msgID));
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	public boolean checkNetWork() {
		return NetworkUtil.isNetworkAvailable(this);
	}

}
