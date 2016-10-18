package com.inspection.app.common;

import java.io.File;
import java.io.FileInputStream;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Android设备信息类
 * 
 * @author liuyx
 * @create 2013/10/15
 */
public class AndroidDeviceInfo {

	private String deviceMacAddress;// 获取设备的mac地址
	private String deviceName;// 设备名称及型号
	private String line1Number;// 电话号码
	private String imei;// imei
	private String sn;
	private String os_version;

	/**
	 * 根据context获取android设备信息
	 */
	public static AndroidDeviceInfo getAndroidDeviceInfo(Context cx) {
		AndroidDeviceInfo milkAndroidInfo = new AndroidDeviceInfo();
		TelephonyManager tm = (TelephonyManager) cx
				.getSystemService(Context.TELEPHONY_SERVICE);
		Build bd = new Build();
		milkAndroidInfo.setImei(tm.getDeviceId());
		milkAndroidInfo.setDeviceName(bd.MODEL);
		milkAndroidInfo.setLine1Number(tm.getLine1Number());
		milkAndroidInfo.setSn(bd.SERIAL);
		milkAndroidInfo.setOs_version(android.os.Build.VERSION.RELEASE);
		//milkAndroidInfo.setDeviceMacAddress(getLocalMacAddress(cx));

		return milkAndroidInfo;
	}

	
	/**
	 * 获取设备的mac地址
	 * 
	 * @return
	 */
	public static String getLocalMacAddress(Context cx) {
		String Mac = null;
		try {
			String path = "sys/class/net/wlan0/address";
			if ((new File(path)).exists()) {
				FileInputStream fis = new FileInputStream(path);
				byte[] buffer = new byte[8192];
				int byteCount = fis.read(buffer);
				if (byteCount > 0) {
					Mac = new String(buffer, 0, byteCount, "utf-8");
				}
			}
			Log.v("mac地址1------", "" + Mac);
			if (Mac == null || Mac.length() == 0) {
				path = "sys/class/net/eth0/address";
				FileInputStream fis_name = new FileInputStream(path);
				byte[] buffer_name = new byte[8192];
				int byteCount_name = fis_name.read(buffer_name);
				if (byteCount_name > 0) {
					Mac = new String(buffer_name, 0, byteCount_name, "utf-8");
				}
			}
			Log.v("mac地址2------", "" + Mac);
			if (Mac.length() == 0 || Mac == null) {
				return "";
			}
		} catch (Exception io) {
			Log.v("exception------", "" + io.toString());
		}
		Log.v("mac地址3------", "" + Mac);
		return Mac.trim();
	}

	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getLine1Number() {
		return line1Number;
	}

	public void setLine1Number(String line1Number) {
		this.line1Number = line1Number;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

}
