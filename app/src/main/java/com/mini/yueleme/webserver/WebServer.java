package com.mini.yueleme.webserver;

import android.content.Context;
import android.content.Intent;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread {

	static final String SUFFIX_APK = ".apk";
	private int port;
	private String webRoot;
	private boolean isLoop = false;
	private ServerSocket serverSocket = null;
	private Context c;

	public WebServer(Context c, int port, final String webRoot) {
		super();
		this.port = port;
		this.c = c;
		this.webRoot = webRoot;
	}

	@Override
	public void run() {
		try {
			// 创建服务器套接字
			serverSocket = new ServerSocket(port);
			// 创建HTTP协议处理
			BasicHttpProcessor httpproc = new BasicHttpProcessor();
			// 增加HTTP协议拦截
			httpproc.addInterceptor(new ResponseDate());
			httpproc.addInterceptor(new ResponseServer());
			httpproc.addInterceptor(new ResponseContent());
			httpproc.addInterceptor(new ResponseConnControl());
			// 创建HTTP服务
			HttpService httpService = new HttpService(httpproc,
					new DefaultConnectionReuseStrategy(),
					new DefaultHttpResponseFactory());
			// 创建HTTP参数
			HttpParams params = new BasicHttpParams();
			params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
					.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
							8 * 1024)
					.setBooleanParameter(
							CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
					.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
					.setParameter(CoreProtocolPNames.ORIGIN_SERVER,
							"WebServer/1.1");
			// 设置HTTP参数
			httpService.setParams(params);
			// 创建HTTP请求执行器注册表
			HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
			// 增加HTTP请求执行
			reqistry.register("*" + SUFFIX_APK, new HttpApkHandler(webRoot));
			reqistry.register("*", new HttpFileHandler(c,webRoot));
			// 设置HTTP请求执行
			httpService.setHandlerResolver(reqistry);
			/* 循环接收各客户端 */
			isLoop = true;
			while (isLoop && !Thread.interrupted()) {
				// 接收客户端套接字
				Socket socket = serverSocket.accept();
				// 绑定至服务器端HTTP连接
				DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
				conn.bind(socket, params);
				// 派�?至WorkerThread处理请求
				Thread t = new WorkerThread(httpService, conn);
				t.setDaemon(true); // 设为守护线程
				t.start();
			}
		} catch (Exception e) {
			isLoop = false;
			e.printStackTrace();
		} finally {
			try {
				stopService();
				if (serverSocket != null) {
					serverSocket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private  void stopService(){
		Intent intent = new Intent(c,WebService.class);
		c.stopService(intent);
	}

	public void close() {
		isLoop = false;
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
