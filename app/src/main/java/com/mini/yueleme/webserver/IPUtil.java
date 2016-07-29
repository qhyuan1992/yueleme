package com.mini.yueleme.webserver;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPUtil {

	private static final String TAG = "IPUtil";

	/*
	 * 获取本地IP地址
	 */
	public static String getLocalIpAddress2() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
					.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				Log.i(TAG, inetAddress.toString());
				/*
				 * 这里做了一步IPv4的判定
				 */
				if (!inetAddress.isLoopbackAddress()
						&& InetAddressUtils.isIPv4Address(inetAddress
								.getHostAddress())) {
					 return inetAddress.getHostAddress().toString();
				}
			}
		}
		return null;
	}

	/*
	 * 获取本地IP地址
	 */
	public static String getLocalIpAddress(Context context){
		WifiApAdmin apAdmin = new WifiApAdmin(context);
		if(apAdmin.isWifiApEnabled()){
			return "192.168.43.1";
		}
		WifiManager wifii = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo d = wifii.getDhcpInfo();
		return IPUtil.intToIP(d.ipAddress);
	}

	/*
	 * 获取局域网内广播IP
	 */
	public static String getBroadcastIpAddress(String localIp) {
		if (localIp != null) {
			return localIp.substring(0, localIp.lastIndexOf(".") + 1) + "255";
		}
		return null;
	}

	/**
	 * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
	 * 通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
	 */
	public static long ipToLong(String ipaddress) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = ipaddress.indexOf(".");
		int position2 = ipaddress.indexOf(".", position1 + 1);
		int position3 = ipaddress.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[3] = Long.parseLong(ipaddress.substring(0, position1));
		ip[2] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
		ip[1] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
		ip[0] = Long.parseLong(ipaddress.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 将十进制整数形式转换成127.0.0.1形式的ip地址 将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
	 * 通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
	 * 通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。 通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
	 */
	public static String intToIP(int addr) {
		StringBuffer buf = new StringBuffer("");
		buf.append(addr & 0xff).append('.').append((addr >>>= 8) & 0xff)
				.append('.').append((addr >>>= 8) & 0xff).append('.')
				.append((addr >>>= 8) & 0xff);
		return buf.toString();
	}

}
