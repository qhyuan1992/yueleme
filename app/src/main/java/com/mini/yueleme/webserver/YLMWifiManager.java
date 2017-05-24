package com.mini.yueleme.webserver;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import java.util.List;

public class YLMWifiManager {

	private static final String TAG = "CNMWifiManager";
	// 定义一个WifiManager对象
	private WifiManager mWifiManager;
	// 定义一个WifiInfo对象
	private WifiInfo mWifiInfo;
	// 扫描出的网络连接列表
	private List<ScanResult> mWifiList;
	// 网络连接列表
	private List<WifiConfiguration> mWifiConfigurations;
	private WifiLock mWifiLock;

	public YLMWifiManager(Context context) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = getConnectionInfo();
	}
	/**获取当前连接的哪一个wifi*/
	public WifiInfo getConnectionInfo(){
		mWifiInfo = mWifiManager.getConnectionInfo();
		return mWifiInfo;
	}

	/**
	 * 开启wifi
	 */
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 关闭wifi
	 */
	public void closeWifi() {
		if (mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(false);
		}
	}

	/**
	 * 检查当前wifi状态
	 * 
	 * @return
	 */
	public int checkState() {
		return mWifiManager.getWifiState();
	}

	/**
	 * 锁定wifiLock
	 */
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	/**
	 * 解锁wifiLock
	 */
	public void releaseWifiLock() {
		// 判断是否锁定
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}

	/**
	 * 创建一个wifiLock
	 * 
	 * @param tag
	 */
	public void createWifiLock(String tag) {
		mWifiLock = mWifiManager.createWifiLock(tag);
	}

	/**
	 * 得到配置好的网络
	 * 
	 * @return
	 */
	public List<WifiConfiguration> getConfiguration() {
		return mWifiConfigurations;
	}

	/**
	 * 获取加密方式
	 * @param result
	 * @return
	 */
	public static WifiConfigurationFactory.PasswordType getSecurity(ScanResult result) {
		if (result.capabilities.contains("WEP")) {
			return WifiConfigurationFactory.PasswordType.WEP_TYPE;
		} else if (result.capabilities.contains("WPA")) {
			return WifiConfigurationFactory.PasswordType.WPA_TYPE;
		}else{
			return WifiConfigurationFactory.PasswordType.NO_PASSWD_TYPE;
		}
	}

	/**
	 * 指定配置好的网络进行连接
	 * 
	 * @param index
	 */
	public void connectionConfiguration(int index) {
		if (index > mWifiConfigurations.size()) {
			return;
		}
		// 连接配置好指定ID的网络
		mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId,
				true);
	}
	
	/**
	 * 连接
	 */
	public boolean connect(String SSID, WifiConfigurationFactory.PasswordType passwordType, String password){
		WifiConfiguration wifiConfig = WifiConfigurationFactory.createWifiConfiguration(SSID, passwordType, password);
		if (wifiConfig == null) {
			return false;
		}
		WifiConfiguration tempConfig = this.IsExsits(SSID);
		if (tempConfig != null) {
			mWifiManager.removeNetwork(tempConfig.networkId);
		}
		int netID = mWifiManager.addNetwork(wifiConfig);
		boolean bRet = mWifiManager.enableNetwork(netID, true);
		mWifiManager.saveConfiguration();
		return bRet;
	}
	
	// 查看以前是否也配置过这个网络
		private WifiConfiguration IsExsits(String SSID) {
			List<WifiConfiguration> existingConfigs = mWifiManager
					.getConfiguredNetworks();
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (("\"" + SSID + "\"").equals(existingConfig.SSID)) {
					return existingConfig;
				}
			}
			return null;
		}

	/**
	 * 扫描
	 */
	public void startScan() {
		mWifiManager.startScan();
		// 得到扫描结果
		mWifiList = mWifiManager.getScanResults();
		// 得到配置好的网络连接
		mWifiConfigurations = mWifiManager.getConfiguredNetworks();
	}

	// 得到网络列表
	public List<ScanResult> getWifiList() {
		return mWifiList;
	}

	// 查看扫描结果
	public StringBuffer lookUpScan() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mWifiList.size(); i++) {
			sb.append("Index_" + new Integer(i + 1).toString() + ":");
			// 将ScanResult信息转换成一个字符串包
			// 其中把包括：BSSID、SSID、capabilities、frequency、level
			sb.append((mWifiList.get(i)).toString()).append("\n");
		}
		return sb;
	}

	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	public int getIpAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 得到连接的ID
	public int getNetWordId() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
	}

	// 得到wifiInfo的所有信息
	public String getWifiInfo() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
	}

	// 添加一个网络并连接
	public void addNetWork(WifiConfiguration configuration) {
		int wcgId = mWifiManager.addNetwork(configuration);
		mWifiManager.enableNetwork(wcgId, true);
	}

	// 断开指定ID的网络
	public void disConnectionWifi(int netId) {
		mWifiManager.disableNetwork(netId);
		mWifiManager.disconnect();
	}
	
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

}
