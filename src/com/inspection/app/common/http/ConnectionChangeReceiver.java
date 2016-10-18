package com.inspection.app.common.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.inspection.app.R;
import com.inspection.app.widget.NewToast;

/**
 * 网络连接状态监听器
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {

	private static final String TAG =ConnectionChangeReceiver.class.getSimpleName();   
	
	@Override
	public void onReceive(Context context, Intent intent) {

		Log.e(TAG, "网络状态改变");     
	    boolean success = false;
	    String name = "";
		
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//判断是否正在使用WIFI网络  
		if(mobNetInfo.isConnected()){
			success = true;
			name = "GPRS";
		}else if(wifiNetInfo.isConnected()){
			success = true;
			name = "WiFi";
		}
		
		if(success){
			NewToast.makeText(context, R.drawable.sure_icon, name+"网络已连接", Toast.LENGTH_LONG).show();
		}else{
			NewToast.makeText(context, R.drawable.sure_icon, "网络连接失败，已离线", Toast.LENGTH_LONG).show();
		}
		
		
	}
}