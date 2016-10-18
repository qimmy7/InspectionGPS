package com.inspection.app.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.inspection.app.R;
import com.inspection.app.adapter.SyncDbAdapter;
import com.inspection.app.common.AndroidDeviceInfo;

/**
 * 欢迎界面
 * 
 * @author liuyx
 * @created 2014/10/30
 */
public class WelcomeActivity extends Activity implements AnimationListener {

	private Context context;
	
	private SharedPreferences deviceInfo;// 存储设备信息
	private String equnum = "";
	private String sn = "";
	private String imei = "";
	private String sim = "";
	private String os_version = "";
	
	public SyncDbAdapter dbAdepter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		context = this;
		dbAdepter = new SyncDbAdapter(context);
        dbAdepter.open();
        
		deviceInfo = this.getSharedPreferences("device",Context.MODE_WORLD_READABLE);
		getDeviceInfo();

		// 渐变动画
		Animation animation = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.fade);
		ImageView img = (ImageView) findViewById(R.id.image);
		animation.setAnimationListener(this);

		// 设置动画
		img.setAnimation(animation);
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		};
		timer.schedule(timerTask, 1000);
	}

	public void getDeviceInfo() {
		equnum = AndroidDeviceInfo.getAndroidDeviceInfo(context)
				.getDeviceName();
		if (equnum.equals("")) {
			equnum = "-";
		}
		sn = AndroidDeviceInfo.getAndroidDeviceInfo(context).getSn();
		if (sn.equals("")) {
			sn = "-";
		}
		imei = AndroidDeviceInfo.getAndroidDeviceInfo(context).getImei();
		if (imei.equals("")) {
			imei = "-";
		}
		//sim = AndroidDeviceInfo.getAndroidDeviceInfo(context).getLine1Number();
		if (sim.equals("")) {
			sim = "-";
		}
		os_version = AndroidDeviceInfo.getAndroidDeviceInfo(context)
				.getOs_version();
		if (os_version.equals("")) {
			os_version = "-";
		}
		
		//Toast.makeText(context,equnum + "--" + sn + "--" + imei + "--" + sim + "--"+ os_version, Toast.LENGTH_LONG).show();
		Editor editor = deviceInfo.edit();
		editor.putString("equnum", equnum);
		editor.putString("sn", sn);
		editor.putString("imei", imei);
		editor.putString("sim", sim);
		editor.putString("os_version", os_version);
		editor.commit();
	}

	public void onAnimationEnd(Animation animation) {

	}

	public void onAnimationRepeat(Animation animation) {

	}

	public void onAnimationStart(Animation animation) {

	}

}
