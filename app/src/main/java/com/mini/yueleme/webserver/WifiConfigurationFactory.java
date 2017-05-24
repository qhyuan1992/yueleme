package com.mini.yueleme.webserver;

import android.net.wifi.WifiConfiguration;

public class WifiConfigurationFactory {
	
public static	enum PasswordType{
		NO_PASSWD_TYPE,
		WEP_TYPE,
		WPA_TYPE
	}
	
	public static WifiConfiguration createWifiConfiguration(String SSID, PasswordType passwordType, String password){
		  WifiConfiguration config = new WifiConfiguration();
	        config.allowedAuthAlgorithms.clear();  
	        config.allowedGroupCiphers.clear();  
	        config.allowedKeyManagement.clear();  
	        config.allowedPairwiseCiphers.clear();  
	        config.allowedProtocols.clear();  
	        config.SSID = "\"" + SSID + "\"";  
	  
	        /*
	         * 当命名的wifi名称相同时，删除以前的？？？？？？？？？？？？？？？？？？？
	         */
//	        WifiConfiguration tempConfig = this.IsExsits(SSID);  
//	        if (tempConfig != null) {  
//	            mWifiManager.removeNetwork(tempConfig.networkId);  
//	        }  
	          
	        // 分为三种情况:1.没有密码2.用wep加密3.用wpa加密   
	        if (passwordType == PasswordType.NO_PASSWD_TYPE) {// WIFICIPHER_NOPASS   
	            config.wepKeys[0] = "\""+"\"";  
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	            config.wepTxKeyIndex = 0;  
	              
	        } else if (passwordType == PasswordType.WEP_TYPE) {  //  WIFICIPHER_WEP    
	            config.hiddenSSID = true;  
	            config.wepKeys[0] = "\"" + password + "\"";  
	            config.allowedAuthAlgorithms  
	                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
	            config.allowedGroupCiphers  
	                    .set(WifiConfiguration.GroupCipher.WEP104);
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
	            config.wepTxKeyIndex = 0;  
	        } else if (passwordType == PasswordType.WPA_TYPE) {   // WIFICIPHER_WPA   
	            config.preSharedKey = "\"" + password + "\"";  
	            config.hiddenSSID = true;  
	            config.allowedAuthAlgorithms  
	                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	            config.allowedPairwiseCiphers  
	                    .set(WifiConfiguration.PairwiseCipher.TKIP);
	            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
	            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	            config.allowedPairwiseCiphers  
	                    .set(WifiConfiguration.PairwiseCipher.CCMP);
	            config.status = WifiConfiguration.Status.ENABLED;
	        }
	        return config;  
	}

}
