package com.mini.yueleme.webserver;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/** 
* 创建热点 
* 
*/  
public class WifiApAdmin {  
   private WifiManager mWifiManager = null;
   public static String AP_SSID;
   public WifiApAdmin(Context context) {
       mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
   }
   
   /**
    * 打开热点
    * @param ssid
    * @param
    */
   public void startWifiAp(String ssid, WifiConfigurationFactory.PasswordType passwordType, String password) {
	   if (mWifiManager.isWifiEnabled()) {  
           mWifiManager.setWifiEnabled(false);  
       } 
       Method method1 = null;
       try {  
           method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",  
                   WifiConfiguration.class, boolean.class);
           WifiConfiguration netConfig = WifiConfigurationFactory.createWifiConfiguration(ssid, passwordType, password);
           method1.invoke(mWifiManager, netConfig, true);  
           AP_SSID = ssid;
       } catch (IllegalArgumentException e) {
            e.printStackTrace();  
        } catch (IllegalAccessException e) {
            e.printStackTrace();  
        } catch (InvocationTargetException e) {
            e.printStackTrace();  
        } catch (SecurityException e) {
            e.printStackTrace();  
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  
        }  
    }  
   
   /**
    * 关闭当前热点
    */
   public void closeWifiAp() {  
       closeWifiAp(mWifiManager);  
   }  
   
   /**
    * 关闭热点
    * @param wifiManager
    */
    private void closeWifiAp(WifiManager wifiManager) {
        if (isWifiApEnabled(wifiManager)) {  
            try {  
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);  
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);  
            } catch (NoSuchMethodException e) {
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {
                e.printStackTrace();  
            } catch (IllegalAccessException e) {
                e.printStackTrace();  
            } catch (InvocationTargetException e) {
                e.printStackTrace();  
            }  
        }  
    }  
    
    public boolean isWifiApEnabled(){
    	return isWifiApEnabled(mWifiManager);
    }

    /**
     * wifi热点是否开启
     * @param wifiManager
     * @return
     */
    private boolean isWifiApEnabled(WifiManager wifiManager) {
        try {  
            Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return false;  
    }

}
