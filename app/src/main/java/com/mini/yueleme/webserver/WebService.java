package com.mini.yueleme.webserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebService extends Service {
	public static final String WEBROOT = "/";
	private WebServer webServer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		webServer = new WebServer(this.getApplicationContext(),8888, WEBROOT);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(webServer!=null&&webServer.isAlive()){
		}else{
			webServer.setDaemon(true);
			webServer.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		webServer.close();
		super.onDestroy();
	}

}
