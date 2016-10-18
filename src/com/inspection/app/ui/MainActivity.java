package com.inspection.app.ui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.app.R;
import com.inspection.app.adapter.SyncDbAdapter;
import com.inspection.app.baidu.BaiduActivity;
import com.inspection.app.bean.BugPatrol;
import com.inspection.app.bean.Param;
import com.inspection.app.bean.PatrolBugPhoto;
import com.inspection.app.bean.PatrolPersonPath;
import com.inspection.app.bean.PatrolTaskPoint;
import com.inspection.app.common.DBGps;
import com.inspection.app.common.DistanceComm;
import com.inspection.app.common.GpsData;
import com.inspection.app.common.http.HttpRequest;
import com.inspection.app.common.http.URLs;
import com.inspection.app.vlook.FFmpegRecorderActivity;
import com.inspection.app.widget.CustomDoubleDialog;
import com.inspection.app.widget.CustomLockDoubleDialog;
import com.inspection.app.widget.CustomProgressDialogA;
import com.inspection.app.widget.CustomSingleDialog;
import com.inspection.app.widget.NewToast;
import com.inspection.app.widget.data.MyData;
import com.inspection.app.widget.data.RaceCommon;
import com.inspection.app.widget.data.XY;
import com.inspection.app.widget.raceView.RaceAxisXView;
import com.inspection.app.widget.raceView.RaceBarView;
import com.inspection.app.widget.view.AxisYView_NormalType;
import com.inspection.app.widget.view.TitleView;

/**
 * 主界面
 * 
 * @author liuyx
 * @created by 2014/10/13
 */
public class MainActivity extends Activity {

	// 信噪比图
	private LinearLayout axisYLayout = null;
	private LinearLayout axisXLayout = null;
	private LinearLayout axisXNumLayout = null;
	private LinearLayout threndLine_Layout = null;
	private LinearLayout title_layout = null;
	private TitleView titleView;
	private RaceBarView raceBar;
	private AxisYView_NormalType axisY_2;
	private RaceAxisXView axisX;
	private XY xy = new XY();
	private float oldX = 0;
	private float oldY = 0;

	// 底部弹出式菜单按钮
	private PopupWindow mPopupWindow;
	private View mPopView;
	private Button app_about;// 关于
	private Button app_cancle;// 取消
	private Button app_exit;// 退出
	private Button app_change;// 注销用户

	private SharedPreferences sp;// 声明一个SharedPreferences,存储用户信息
	private SharedPreferences deviceInfo;// 存储设备信息
	private Context context;
	private LocationManager lm;
	private Location loc;
	private Criteria ct;
	private String provider;
	private GpsStatus gpsstatus;
	List<Integer> snrList;// 信噪比数据List

	private LinearLayout basic_1, basic_2, basic_3, basic_4, basic_5, basic_6,
			basic_7, basic_8, basic_9;

	private TextView find_star;// 搜星颗数

	private TextView tv1_value;
	private TextView tv2_value;
	private TextView tv3_value;
	private TextView tv4_value;
	private TextView tv5_value;
	private TextView tv_taskId;//距离最近作业点距离的ID

	private TextView tvLatitude;
	private TextView tvLongitude;
	private TextView tvHigh;
	private TextView tvSpeed;
	private TextView tvGpsTime;
	private TextView tvcSaltnum;
	
	//提示
	private TextView tv_hint_key,tv_hint_value;

	private ImageButton battery_icon;
	private TextView battery_soc;
	private BroadcastReceiver batteryLevelRcvr;
	private IntentFilter batteryLevelFilter;

	// 顶部bar
	private RelativeLayout topRelativeLayout;
	private RelativeLayout bottom_relativeLayout;// 底部布局
	private LinearLayout inspection_linearLayout, locked_linearLayout;
	private Button get_start, get_photo, get_end;// 底部按钮

	private DBGps dbgps = new DBGps(this);

	private boolean isSnrLocation = false;// 是否定位
	private int timespace = 1000;
	private AutoThread athread = new AutoThread();

	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	public SyncDbAdapter dbAdepter;

	// 拍照
	Dialog dialog;
	private Button photo_locale_takephotos, photo_cancel;
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";

	private File tempFile;// 临时文件
	private String imgFile;// 文件路径
	private boolean flags = false;// 是否有图片待上传
	private Spinner patrol_bug_list;
	private ImageView uploadImageView;// 照片
	private Button uploadButton, saveButton, cancelButton;

	private LinearLayout battery_linearLayout, snr_linearLayout,
			basic_linearLayout, takephoto_linearlayout;

	int toDoCount = 0;// 待上传作业点任务数
	int toDoBugCount = 0;// 待上传缺陷报告数
	int countNum = 0;// 总待上传数据
	int taskNum = 0;// 巡检作业个数
	int min0 = 0;// 距最近桩的距离
	int min1 = 0;// 距最近作业点的距离
	String taskStrId = "0";
	int k = 1;
	int m = 1;
	int pileLine = 0;
	int taskStraightLineDis = 0;
	int taskId;
	int min_i = 0;
	double[] currentPoint = { 0, 0 };// 当前位置点
	double[] pilePoint1 = { 0, 0 };// 桩点1
	double[] pilePoint2 = { 0, 0 };// 桩点2
	double[] taskPoint = { 0, 0 };// 作业点
	
	private String subline_id;// 管线id
	private String user_id;// 当前登录人id
	private int voiceTime = 3000;// 声音提示时间间隔
	private int gpsUploadTime = 10000;// GPS上传频率参数(秒)_Android
	private int UPTime = 0;// 上传路径点时间间隔参数(毫秒)
	private int OverSpeed = 0;// 巡检仪超速提醒参数(公里/小时)
	private int StartRange = 0;// 接近巡检作业点范围起点参数(米)
	private int EndRange = 0;// 接近巡检作业点范围终点参数(米)
	private int UPDistance = 0;// 上传巡检作业点参数(米)
	private int PipeDeviation = 0;// 偏离管道参数(米)
	private int OutOfLineTime = 0;// 离线时间限制(分钟)
	private boolean flag = true;
	private boolean speedFlag = false;// 超速
	private boolean ComeNearFlag = false;// 接近巡检作业点
	private boolean CrossingFlag = false;// 超出巡检范围
	private boolean IsCal = true;// 是否计算得出当前位置和作业点的距离
	SoundPool spl;
	HashMap<Integer, Integer> spMap;
	boolean locked = true;// 锁定标识
	View layout1;// 信噪比View
	int initV = 0;
	boolean min1IsFlag = true;
	boolean gpsFlag = false;
	
	/**
	 * 当前系统时间
	 */
	Date currentDate = new Date();
	SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
	String dateF = form.format(currentDate);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_panel);
		context = this;
		// 打开本地数据库
		dbAdepter = new SyncDbAdapter(context);
		dbAdepter.open();
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		RaceCommon.screenWidth = mDisplayMetrics.widthPixels;
		RaceCommon.screenHeight = mDisplayMetrics.heightPixels / 5;

		// 设置分组柱状图参数
		RaceCommon.raceWidth = RaceCommon.screenWidth * 1 / 14;
		RaceCommon.barWidth = 30;
		RaceCommon.space = 3;

		// 获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		deviceInfo = this.getSharedPreferences("device",
				Context.MODE_WORLD_READABLE);
		subline_id = sp.getString("subline_id", "");
		user_id = sp.getString("user_id", "");// 获取当前登录用户ID

		if(initV==0){//初始化巡检
			initV = 1;
			Editor et = sp.edit();
			et.putString("start", "false");// 是否巡检标识
			et.commit();
		}
		
		// 顶部
		topRelativeLayout = (RelativeLayout) findViewById(R.id.main_topbar);
		TranslateAnimation animTop = new TranslateAnimation(0, 0, 3000, 0);// 动画
		animTop.setDuration(500);
		topRelativeLayout.setAnimation(animTop);

		battery_linearLayout = (LinearLayout) findViewById(R.id.battery_linearLayout);
		View layout0 = LayoutInflater.from(this).inflate(R.layout.main_battery,
				null);
		battery_linearLayout.addView(layout0);
		TranslateAnimation animBattery = new TranslateAnimation(0, 0, 3000, 0);// 动画
		animBattery.setInterpolator(new BounceInterpolator());
		animBattery.setDuration(500);
		layout0.setAnimation(animBattery);
		battery_linearLayout.setAnimation(animBattery);

		battery_icon = (ImageButton) layout0.findViewById(R.id.battery_icon);
		battery_soc = (TextView) layout0.findViewById(R.id.battery_soc);
		monitorBatteryState();// 电池电量

		snr_linearLayout = (LinearLayout) findViewById(R.id.snr_linearLayout);
		layout1 = LayoutInflater.from(this).inflate(R.layout.main_snr, null);// 信噪比
		AnimationSet set1 = new AnimationSet(true);
		Animation animation1 = AnimationUtils.loadAnimation(this,
				R.anim.login_in);
		animation1.setDuration(600);
		set1.addAnimation(animation1);
		LayoutAnimationController controller1 = new LayoutAnimationController(
				set1, 0.5f);
		snr_linearLayout.setLayoutAnimation(controller1);
		snr_linearLayout.addView(layout1);
		TranslateAnimation animSnr = new TranslateAnimation(0, 0, 3000, 0);// 动画
		animSnr.setDuration(700);
		snr_linearLayout.setAnimation(animSnr);

		find_star = (TextView) layout1.findViewById(R.id.find_star);// 信噪比搜星颗数

		basic_linearLayout = (LinearLayout) findViewById(R.id.basic_linearLayout);
		View layout2 = LayoutInflater.from(this).inflate(R.layout.main_basic,
				null);
		AnimationSet set2 = new AnimationSet(true);
		Animation animation2 = AnimationUtils.loadAnimation(this,
				R.anim.login_in);
		animation2.setDuration(700);
		set2.addAnimation(animation2);
		LayoutAnimationController controller2 = new LayoutAnimationController(
				set2, 0.5f);
		basic_linearLayout.setLayoutAnimation(controller2);
		basic_linearLayout.addView(layout2);

		basic_1 = (LinearLayout) layout2.findViewById(R.id.basic_1);
		basic_2 = (LinearLayout) layout2.findViewById(R.id.basic_2);
		basic_3 = (LinearLayout) layout2.findViewById(R.id.basic_3);
		basic_4 = (LinearLayout) layout2.findViewById(R.id.basic_4);
		basic_5 = (LinearLayout) layout2.findViewById(R.id.basic_5);
		basic_6 = (LinearLayout) layout2.findViewById(R.id.basic_6);
		basic_7 = (LinearLayout) layout2.findViewById(R.id.basic_7);
		basic_8 = (LinearLayout) layout2.findViewById(R.id.basic_8);
		basic_9 = (LinearLayout) layout2.findViewById(R.id.basic_9);
		TranslateAnimation anim1 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim1.setDuration(800);
		basic_1.setAnimation(anim1);
		TranslateAnimation anim2 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim2.setDuration(900);
		basic_2.setAnimation(anim2);
		/*
		 * TranslateAnimation anim3 = new TranslateAnimation(0, 0, 1000, 0);//动画
		 * anim3.setDuration(1000); basic_3.setAnimation(anim3);
		 */
		TranslateAnimation anim4 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim4.setDuration(1000);
		basic_4.setAnimation(anim4);
		TranslateAnimation anim5 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim5.setDuration(1100);
		basic_5.setAnimation(anim5);
		TranslateAnimation anim6 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim6.setDuration(1200);
		basic_6.setAnimation(anim6);
		/*
		 * TranslateAnimation anim7 = new TranslateAnimation(0, 0, 1000, 0);//动画
		 * anim7.setDuration(1400); basic_7.setAnimation(anim7);
		 */
		TranslateAnimation anim8 = new TranslateAnimation(0, 0, 3000, 0);// 动画
		anim8.setDuration(1300);
		basic_8.setAnimation(anim8);

		tv1_value = (TextView) findViewById(R.id.tv1_value);
		tv2_value = (TextView) findViewById(R.id.tv2_value);
		tv3_value = (TextView) findViewById(R.id.tv3_value);
		tv4_value = (TextView) findViewById(R.id.tv4_value);
		tv5_value = (TextView) findViewById(R.id.tv_location_distance_value);// 距最近作业点
		tv_taskId = (TextView) findViewById(R.id.tv_taskId);// 距最近作业点ID
		if (!deviceInfo.getString("taskPointNum", "").equals("")) {
			taskNum = Integer.valueOf(deviceInfo.getString("taskPointNum", ""));
			String tv1 = "巡检作业：" + taskNum + " 个";
			tv1_value.setText(tv1);
		}

		tvLatitude = (TextView) findViewById(R.id.tv_latitude_value);
		tvLongitude = (TextView) findViewById(R.id.tv_longitude_value);
		tvHigh = (TextView) findViewById(R.id.tv_height_value);
		tvSpeed = (TextView) findViewById(R.id.tv_speed_km_value);
		tvGpsTime = (TextView) findViewById(R.id.tv_time_value);
		tvcSaltnum = (TextView) findViewById(R.id.tv_saltnum_value);
		//提示
		tv_hint_key = (TextView) findViewById(R.id.tv_hint_key);
		tv_hint_value = (TextView) findViewById(R.id.tv_hint_value);

		bottom_relativeLayout = (RelativeLayout) findViewById(R.id.bottom_relativeLayout);
		TranslateAnimation animBottom = new TranslateAnimation(0, 0, 3000, 0);// 动画
		animBottom.setDuration(1500);
		bottom_relativeLayout.setAnimation(animBottom);
		inspection_linearLayout = (LinearLayout) findViewById(R.id.inspection_linearLayout);
		locked_linearLayout = (LinearLayout) findViewById(R.id.locked_linearLayout);
		get_start = (Button) findViewById(R.id.get_start);
		get_photo = (Button) findViewById(R.id.get_photo);
		get_end = (Button) findViewById(R.id.get_end);

		// 缺陷报告照片上传
		takephoto_linearlayout = (LinearLayout) findViewById(R.id.takephoto_linearlayout);
		View layout3 = LayoutInflater.from(this).inflate(R.layout.takephoto,
				null);
		AnimationSet set3 = new AnimationSet(true);
		Animation animation3 = AnimationUtils.loadAnimation(this,
				R.anim.login_in);
		animation3.setDuration(1000);
		set3.addAnimation(animation3);
		LayoutAnimationController controller3 = new LayoutAnimationController(
				set3, 0.5f);
		takephoto_linearlayout.setLayoutAnimation(controller3);
		takephoto_linearlayout.addView(layout3);

		patrol_bug_list = (Spinner) layout3.findViewById(R.id.patrol_bug_list);
		uploadImageView = (ImageView) layout3
				.findViewById(R.id.upload_photo_imgv);
		uploadButton = (Button) layout3.findViewById(R.id.photo_upload_btn);
		saveButton = (Button) layout3.findViewById(R.id.photo_save_btn);
		cancelButton = (Button) layout3.findViewById(R.id.photo_cancel_btn);
		uploadImageView.setOnClickListener(uploadPhoto);
		uploadButton.setOnClickListener(uploadPhoto);// 上传照片
		saveButton.setOnClickListener(uploadPhoto);// 本地保存照片
		cancelButton.setOnClickListener(uploadPhoto);// 取消

		findView();
		init();

		// dbgps.openDB();
		loadSpinnerProvincias();// 加载缺陷报告
		new Thread(new ThreadShow()).start();// 定时器
		InitSound();// 播放声音文件初始化

		// 自定义参数
		setKey();
		setData();// 设置静态信噪比数据
		setAxis();

		// 设置图区宽高、内容宽高
		RaceCommon.layoutWidth = RaceCommon.screenWidth * 5 / 2;
		RaceCommon.layoutHeight = RaceCommon.screenHeight * 6 / 8;
		RaceCommon.viewWidth = RaceCommon.raceWidth
				* (RaceCommon.xScaleArrayInt.length);
		RaceCommon.viewHeight = RaceCommon.screenHeight * 6 / 8;
		// 初始化信噪比图
		initChartSnr();
		// 填充
		addView();
		new Thread(new RefreshSnrThread()).start();// 启动自刷新信噪比图
		getParamInfo();//获取巡检参数
		initLocation();// 初始化数据卫星
	}
	
	/**
	 * 获取参数
	 */
	public void getParamInfo(){
		Param[] pList = dbAdepter.queryParam();
		if(pList!=null){
			for (int i = 0; i < pList.length; i++) {
				if(pList[i].paramName.trim().equals("GpsUpload")){
					gpsUploadTime = Integer.valueOf(pList[i].paramValue.toString()) * 1000;
				}else if(pList[i].paramName.trim().equals("UPTime")){
					UPTime = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("OverSpeed")){
					OverSpeed = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("StartRange")){
					StartRange = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("EndRange")){
					EndRange = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("UPDistance")){
					UPDistance = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("PipeDeviation")){
					PipeDeviation = Integer.valueOf(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("OutOfLineTime")){
					OutOfLineTime = Integer.valueOf(pList[i].paramValue.toString());
				}
			}
		}
	}

	/*
	 * 自动刷新界面
	 */
	class RefreshSnrThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				// 使用postInvalidate可以直接在线程中更新界面，layout1是一个View
				layout1.postInvalidate();
			}
		}
	}

	// GPS获取数据刷新线程处理
	private class AutoThread extends Thread {
		private boolean running = true;
		private Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (sp.getString("start", "").equals("true")) {// 开始巡检
					showInfo(getLastPosition(), 2);
				}else{
					showInfo(null, -1);
				}
			}
		};

		public AutoThread() {
		}

		@Override
		public void run() {
			while (running) {
				try {
					h.sendEmptyMessage(0);
					Thread.sleep(1000);// 界面刷新数据间隔时间
				} catch (Exception e) {

				}
			}
		}
	}

	/**
	 * ========================================================================
	 * ===============================================信噪比图
	 */
	/**
	 * 初始化各绘图组件 包括设置高宽、位置
	 */
	private void initChartSnr() {
		title_layout = (LinearLayout) findViewById(R.id.titleView);

		axisXLayout = (LinearLayout) findViewById(R.id.axisX);
		RelativeLayout.LayoutParams xParams = (android.widget.RelativeLayout.LayoutParams) axisXLayout
				.getLayoutParams();
		xParams.height = RaceCommon.layoutHeight;
		xParams.width = RaceCommon.layoutWidth;
		xParams.setMargins(xParams.leftMargin + RaceCommon.YWidth,
				xParams.topMargin, xParams.rightMargin, xParams.bottomMargin);
		axisXLayout.setLayoutParams(xParams);

		axisXNumLayout = (LinearLayout) findViewById(R.id.axisX_Num);
		RelativeLayout.LayoutParams xNumParams = (android.widget.RelativeLayout.LayoutParams) axisXNumLayout
				.getLayoutParams();
		xNumParams.height = RaceCommon.layoutHeight;
		xNumParams.width = RaceCommon.layoutWidth;
		xNumParams.setMargins(xNumParams.leftMargin + RaceCommon.YWidth,
				xNumParams.topMargin, xNumParams.rightMargin,
				xNumParams.bottomMargin);
		axisXNumLayout.setLayoutParams(xNumParams);

		axisYLayout = (LinearLayout) findViewById(R.id.axisY);
		RelativeLayout.LayoutParams yParams = (android.widget.RelativeLayout.LayoutParams) axisYLayout
				.getLayoutParams();
		yParams.height = RaceCommon.layoutHeight + RaceCommon.XHeight;
		yParams.setMargins(yParams.leftMargin, yParams.topMargin,
				yParams.rightMargin, yParams.bottomMargin + RaceCommon.XHeight);
		axisYLayout.setLayoutParams(yParams);

		threndLine_Layout = (LinearLayout) findViewById(R.id.thrend_line);
		RelativeLayout.LayoutParams hParams = (android.widget.RelativeLayout.LayoutParams) threndLine_Layout
				.getLayoutParams();
		hParams.height = RaceCommon.layoutHeight + RaceCommon.XHeight;
		hParams.width = RaceCommon.layoutWidth;
		hParams.setMargins(hParams.leftMargin + RaceCommon.YWidth,
				hParams.topMargin, hParams.rightMargin, hParams.bottomMargin
						+ RaceCommon.XHeight);
		threndLine_Layout.setLayoutParams(hParams);

		// 实例化View
		// axisY = new AxisYView_LevelType(this);
		axisY_2 = new AxisYView_NormalType(this);
		axisX = new RaceAxisXView(this);
		raceBar = new RaceBarView(this);
		titleView = new TitleView(this);
	}

	// 静态数据取柱状条数据
	private void setData() {
		MyData data = new MyData();
		data.setName("");// 设为空，针对不同机型、不同屏幕位置不好控制
		// data.setData(new int[] { 66, 77, 88, 44, 55, 66, 77, 99, 56
		// });//取柱状条数据
		List<Integer> snrList = new ArrayList<Integer>();
		/*snrList.add(78);
		snrList.add(45);
		snrList.add(87);
		snrList.add(48);
		snrList.add(66);
		snrList.add(25);
		snrList.add(97);
		snrList.add(56);
		snrList.add(34);*/
		data.setDataList(snrList);// 取柱状条数据
		data.setColor(Color.rgb(248, 221, 1));// 柱状条颜色
		RaceCommon.DataSeries = new ArrayList<MyData>();
		RaceCommon.DataSeries.add(data);
	}

	private void setKey() {
		// 设置图例参数
		RaceCommon.keyWidth = 0;
		RaceCommon.keyHeight = 0;
		RaceCommon.keyToLeft = 0;
		RaceCommon.keyToTop = 0;
		RaceCommon.keyToNext = 0;
		RaceCommon.keyTextPadding = 0;
	}

	private void setAxis() {
		// 设置轴参数
		// RaceCommon.xScaleArray = new String[] { "1", "2", "3", "4", "5",
		// "6","7", "8", "9", "10" };//字符串数组X轴刻度数
		RaceCommon.xScaleArrayInt = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
				11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };// 整形数组X轴刻度数
		RaceCommon.xScaleColor = Color.rgb(210, 210, 222);// X轴线的颜色
		RaceCommon.yScaleColor = Color.rgb(210, 210, 222);// Y轴线的颜色

		// yScaleArray需要比levelName和color多出一个数
		RaceCommon.yScaleArray = new int[] { 0, 25, 50, 75, 100, 125 };
		RaceCommon.levelName = new String[] { "优", "良", "弱" };
		RaceCommon.yScaleColors = new int[] { Color.rgb(210, 210, 222),
				Color.rgb(210, 210, 222), Color.rgb(210, 210, 222),
				Color.rgb(210, 210, 222) };
	}

	// 绘制信噪比图
	private void addView() {
		int width = 0;
		// if(mp==false)
		// width=RaceCommon.screenWidth*7/8+10;
		// else
		width = RaceCommon.viewWidth;

		// 设定初始定位Y坐标
		xy.y = RaceCommon.viewHeight - RaceCommon.layoutHeight;

		raceBar.initValue(RaceCommon.viewHeight);// 传入宽、高、是否在折线图上显示数据
		raceBar.scrollTo(0, xy.y);

		axisY_2.initValue(RaceCommon.viewHeight);// 传入高度
		axisY_2.scrollTo(0, xy.y);

		axisX.initValue(width, RaceCommon.viewHeight);// 传入高度
		axisX.scrollTo(0, xy.y);

		axisYLayout.removeAllViews();
		axisYLayout.addView(axisY_2);

		axisXLayout.removeAllViews();
		axisXLayout.addView(axisX);

		threndLine_Layout.removeAllViews();
		threndLine_Layout.addView(raceBar);

		title_layout.removeAllViews();
		title_layout.addView(titleView);

		// 监听滑动事件
		raceBar.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					oldX = event.getX();
					oldY = event.getY();
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					parseXY(xy.x += oldX - event.getX(),
							xy.y += oldY - event.getY(), raceBar.getWidth(),
							raceBar.getHeight(), threndLine_Layout);
					System.out.println("x=" + xy.x + "  y=" + xy.y);
					raceBar.scrollTo(xy.x, xy.y);
					axisY_2.scrollTo(0, xy.y);
					axisX.scrollTo(xy.x, RaceCommon.viewHeight
							- RaceCommon.layoutHeight);
					oldX = event.getX();
					oldY = event.getY();
				}
				return true;
			}
		});
	}

	// 信噪比滑动处理
	protected void parseXY(float x, float y, int width, int height,
			LinearLayout parent) {
		int parentWidth = parent.getWidth();
		int parentHeight = parent.getHeight();
		if (x < 0)
			xy.x = 0;
		else if (x > width - parentWidth)
			xy.x = width - parentWidth;
		else
			xy.x = (int) x;

		if (y < 0)
			xy.y = 0;
		else if (y > height - parentHeight)
			xy.y = height - parentHeight;
		else
			xy.y = (int) y;

		// 初步防抖
		if (width <= parentWidth)
			xy.x = 0;
		if (height <= parentHeight)
			xy.y = 0;
	}

	/**
	 * ========================================================================
	 * ===============================================信噪比图
	 */

	// 线程类-服务器请求数据，更新界面基本信息
	class ThreadShow implements Runnable {
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 本地服务和数据状态，定时线程处理
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				gpsFlag = isOPen(context);
				if(gpsFlag){
					initLocation();// 初始化数据卫星
				}else{
					find_star.setText("GPS不可用，请开启GPS");
				}
				ConnectivityManager connectMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);// 判断是否正在使用GPRS网络
				NetworkInfo wifiNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 判断是否正在使用WIFI网络
				// 网络状态判断
				if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {
					tv4_value.setText("工作状态：在线");
				} else {
					tv4_value.setText("工作状态：离线");
				}
				PatrolTaskPoint[] taskPointList = dbAdepter
						.queryTaskPoint(subline_id);
				String ListtaskIdStr = "0";
				StringBuilder sb = new StringBuilder();
				if (taskPointList != null) {
					for (int i = 0; i < taskPointList.length; i++) {
						sb.append("'"+taskPointList[i].id+"'" + ",");
					}
					ListtaskIdStr = sb.substring(0, sb.toString().length() - 1);
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
				PatrolPersonPath[] pathList = dbAdepter
						.queryPatrolPersonPathTaskPoint(sp.getString("user_id", ""),ListtaskIdStr,df.format(new Date()));
				Editor editor = sp.edit();
				if (pathList != null) {
					editor.putString("completeTaskNum",
							String.valueOf(pathList.length));
				} else {
					editor.putString("completeTaskNum", "0");
				}
				editor.commit();
				tv2_value.setText("已巡检：" + sp.getString("completeTaskNum", "")
						+ " 个");
				if (sp.getString("start", "").equals("true")) {// 开始巡检
					if(Double.valueOf(tvLongitude.getText().toString())>0 || Double.valueOf(tvLatitude.getText().toString())>0){
						tv5_value.setText(min1 + " 米");
						tv_taskId.setText(taskStrId);
						if(min1IsFlag){
							min1IsFlag = false;
							CalTaskDistance();
							//new CalDistanceAsyncTask().execute();// 异步计算
						}
					}
				}else if(sp.getString("start", "").equals("false")){
					tv5_value.setText(0 + " 米");
				}
				PatrolPersonPath[] pList = dbAdepter
						.queryPatrolPersonPathByPersonId(user_id);// 巡检作业待上传数据查询
				PatrolBugPhoto[] bpList = dbAdepter
						.queryPatrolBugPhotoIsUploadBy0_2(user_id);// 巡检作业缺陷报告照片数据查询
				if (bpList != null) {
					toDoBugCount = bpList.length;
				} else {
					toDoBugCount = 0;
				}
				if (pList != null) {
					toDoCount = pList.length;
				} else {
					toDoCount = 0;
				}
				countNum = toDoCount + toDoBugCount;
				tv3_value.setText("待上传：" + countNum + " 条");
				if (wifiNetInfo.isConnected()) {
					if (toDoBugCount > 0) {// 设备不在巡检状态时，待上传的数据在有网络的情况下缺陷报告自动上传
						new UploadAsyncTask().execute();// 异步上传数据
						/*if (toDoBugCount == 1) {
							*//******** 播放提示音 ********//*
							playSound(1, 0);
						}*/
					}
					if (toDoCount > 0) {// 设备不在巡检状态时，待上传的数据在有网络的情况下作业点自动上传
						new UploadAsyncTask().execute();// 异步上传数据
						/*if (toDoCount == 1) {
							*//******** 播放提示音 ********//*
							playSound(1, 0);
						}*/
					}
				}
				// =============================================
			}
		}
	};

	/**
	 * 加载缺陷报告类型
	 */
	private void loadSpinnerProvincias() {
		// 数据
		data_list = new ArrayList<String>();
		BugPatrol[] bList = dbAdepter.queryBugPatrol();
		if (bList != null) {
			for (int j = 0; j < bList.length; j++) {
				data_list.add(bList[j].name);
			}
		} else {
			data_list.add("");
		}
		// 适配器
		arr_adapter = new ArrayAdapter<String>(context,
				R.layout.simple_spinner_item, data_list);
		// 设置样式
		arr_adapter
				.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		patrol_bug_list.setAdapter(arr_adapter);
	}

	// 加载底部tab布局
	private void findView() {
		mPopView = LayoutInflater.from(context)
				.inflate(R.layout.app_exit, null);
		app_about = (Button) mPopView.findViewById(R.id.app_about);
		app_cancle = (Button) mPopView.findViewById(R.id.app_cancle);
		app_change = (Button) mPopView.findViewById(R.id.app_change_user);
		app_exit = (Button) mPopView.findViewById(R.id.app_exit);
	}

	// 初始化
	private void init() {
		mPopupWindow = new PopupWindow(mPopView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		// 关于
		app_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 自定义弹出提示框-模仿iOS8弹出对话框
				CustomDoubleDialog.Builder builder = new CustomDoubleDialog.Builder(
						context);
				builder.setMessage("当前版本为最新版(v1.0)");
				builder.setTitle("关于");
				builder.setPositiveButton("更新",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									int which) {
								CustomProgressDialogA.showProgressDialog(
										context, "正在检测更新...");
								new Thread() {
									@Override
									public void run() {
										try {
											Thread.sleep(3000);
											CustomProgressDialogA
													.hideProgressDialog();
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											CustomProgressDialogA
													.hideProgressDialog();
											dialog.dismiss();
										}
									}
								}.start();
								Thread.interrupted();// 中断线程
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
				mPopupWindow.dismiss();
			}
		});

		// 取消
		app_cancle.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});

		// 注销用户
		app_change.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 自定义弹出提示框-模仿IOS8弹出对话框
				CustomDoubleDialog.Builder builder = new CustomDoubleDialog.Builder(
						context);
				builder.setMessage("确定注销？");
				builder.setTitle("注销用户");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// finish();
								dialog.dismiss();
								((Activity) context).overridePendingTransition(
										R.anim.activity_up, R.anim.fade_out);
								Intent intent = new Intent(context,
										LoginActivity.class);
								startActivity(intent);
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});

		// 退出
		app_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// 自定义弹出提示框-模仿iOS8弹出对话框
				CustomDoubleDialog.Builder builder = new CustomDoubleDialog.Builder(
						context);
				builder.setMessage("确定退出巡检系统？");
				builder.setTitle("退出");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("取消",
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		});
	}

	/**
	 * 开启监听定位线程状态  
	 */
	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location arg0) {
			// isAlive()用于测试线程的状态，即是否活着。
			if (!athread.isAlive())
				athread.start();
		}

		public void onProviderDisabled(String arg0) {
			showInfo(null, -1);
		}

		public void onProviderEnabled(String arg0) {
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// isAlive()用于测试线程的状态，即是否活着。
			if (!athread.isAlive())
				athread.start();
		}
	};

	private GpsStatus.Listener statuslistener = new GpsStatus.Listener() {
		
		public void onGpsStatusChanged(int event) {
			gpsstatus = lm.getGpsStatus(null);
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:// 第一次定位
				int c = gpsstatus.getTimeToFirstFix();
				Log.i("打印第一次定位值：", String.valueOf(c));
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {
				// 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
				Iterable<GpsSatellite> allgps = gpsstatus.getSatellites();
				// 再次转换成Iterator
				Iterator<GpsSatellite> items = allgps.iterator();

				// 通过遍历重新整理为ArrayList
				ArrayList<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();
				int count = 0;
				int maxSatellites = gpsstatus.getMaxSatellites();
				while (items.hasNext() && count <= maxSatellites) {
					GpsSatellite satellite = items.next();
					satelliteList.add(satellite);
					count++;
				}
				// Toast.makeText(context, "总共搜索到"+count+"颗卫星",
				// Toast.LENGTH_LONG).show();
				// tvcSaltnum.setText("卫星数："+String.format("%d 颗", count));
				if (count >= 3) {
					isSnrLocation = true;// 卫星信号正常
				} else {
					isSnrLocation = false;// 卫星信号不正常
				}
				if(gpsFlag){
					if (count > 0 && count < 3) {
						find_star.setText(count + " 颗卫星可用，信号弱");
					}else if(count >= 3){
						find_star.setText(count + " 颗卫星可用，信号正常");
					} else {
						find_star.setText("无可用卫星");
					}
				}else{
					find_star.setText("GPS不可用，请开启GPS");
				}
				

				// 输出卫星信息
				for (int j = 0; j < satelliteList.size(); j++) {
					/*
					 * //卫星的方位角，浮点型数据
					 * System.out.println("卫星的方位角："+satelliteList.
					 * get(j).getAzimuth()); //卫星的高度，浮点型数据
					 * System.out.println("卫星的高度："
					 * +satelliteList.get(j).getElevation()); //卫星的伪随机噪声码，整形数据
					 * System
					 * .out.println("卫星的伪随机噪声码："+satelliteList.get(j).getPrn());
					 * //卫星是否有年历表，布尔型数据
					 * System.out.println("卫星是否有年历表："+satelliteList
					 * .get(j).hasAlmanac()); //卫星是否有星历表，布尔型数据
					 * System.out.println
					 * ("卫星是否有星历表："+satelliteList.get(j).hasEphemeris());
					 * //卫星是否被用于近期的GPS修正计算
					 * System.out.println("卫星是否被用于近期的GPS修正计算："
					 * +satelliteList.get(j).hasAlmanac()); //卫星的信噪比，浮点型数据
					 * System
					 * .out.println("卫星的信噪比："+satelliteList.get(j).getSnr());
					 */
					// Toast.makeText(context,
					// "卫星的信噪比："+satelliteList.get(j).getSnr(),
					// Toast.LENGTH_LONG).show();

					MyData data = new MyData();
					data.setName("");// 设为空，针对不同机型、不同屏幕位置不好控制
					snrList = new ArrayList<Integer>();
					if (count > 0) {
						for (int i = 0; i < count; i++) {
							snrList.add(Integer.valueOf((int) satelliteList
									.get(j).getSnr())
									+ new Random().nextInt(10) + 10);
							data.setDataList(snrList);
							data.setColor(Color.rgb(248, 221, 1));// 信噪比柱状条颜色
							RaceCommon.DataSeries = new ArrayList<MyData>();
							RaceCommon.DataSeries.add(data);
						}
					} else {
						snrList.add(0);
						data.setDataList(snrList);
						data.setColor(Color.rgb(248, 221, 1));// 信噪比柱状条颜色
						RaceCommon.DataSeries = new ArrayList<MyData>();
						RaceCommon.DataSeries.add(data);
					}

				}

				break;
			}
			case GpsStatus.GPS_EVENT_STARTED:
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				break;
			}
		}
	};

	private void initLocation() {
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			ct = new Criteria();
			// ct.setAccuracy(Criteria.ACCURACY_FINE);// 高精度定位慢
			ct.setAccuracy(Criteria.ACCURACY_COARSE);// 非高精度定位更快
			ct.setAltitudeRequired(true);// 显示海拔
			ct.setBearingRequired(true);// 显示方向
			ct.setSpeedRequired(true);// 显示速度
			ct.setCostAllowed(true);// 不允许有花费
			ct.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
			provider = lm.getBestProvider(ct, true);
			// 位置变化监听,默认1秒一次,0表示不考虑距离变化(距离10米以上)
			lm.requestLocationUpdates(provider, 1000, 0, locationListener);
			lm.addGpsStatusListener(statuslistener);//卫星信号
		} else
			showInfo(null, -1);
	}

	// 获取gps信息
	private GpsData getLastPosition() {
		GpsData result = new GpsData();
		loc = lm.getLastKnownLocation(provider);
		if (loc != null) {
			result.Latitude = (int) (loc.getLatitude() * 1E6);
			result.Longitude = (int) (loc.getLongitude() * 1E6);
			result.High = loc.getAltitude();
			result.Direct = loc.getBearing();
			result.Speed = loc.getSpeed();
			Date d = new Date();
			// UTC时间,转北京时间+8小时,不同的手机GPS时间不同,有的手机GPS时间不需要+8小时
			//d.setTime(loc.getTime() + 28800000);
			// UTC时间
			d.setTime(loc.getTime());
			result.GpsTime = DateFormat.format("yyyy-MM-dd kk:mm:ss", d)
					.toString();
			d = null;
		}
		return result;
	}

	/**
	 * gps定位
	 * 
	 * @param cdata
	 * @param infotype
	 */
	private void showInfo(GpsData cdata, int infotype) {
		if (cdata == null) {
			if (infotype == -1) {
				tvLatitude.setText("0");
				tvLongitude.setText("0");
				tvHigh.setText("0.0 米");
				tvSpeed.setText("0.0 米/秒");
				tvGpsTime.setText("-");
				tv5_value.setText("0 米");
				tv_taskId.setText("");
				cdata = null;
			}
		} else {
			DecimalFormat df1 = new DecimalFormat("#.0");// 保留一位小数
			DecimalFormat df2 = new DecimalFormat("#.00");// 保留两位小数
			tvLatitude.setText(String.format("%f",Double.valueOf(cdata.Latitude / 1E6)));// 纬度
			tvLongitude.setText(String.format("%f",Double.valueOf(cdata.Longitude / 1E6)));// 经度
			float high = Float.valueOf(df1.format(cdata.High));
			tvHigh.setText(String.valueOf(high+" 米"));// 海拔
			if((cdata.Speed * 3600.0)/1000.0>20){
				float speed = Float.valueOf(df2.format((cdata.Speed * 3600.0) / 1000.0));
				tvSpeed.setText(String.valueOf(speed+" 千米/小时"));// 速度
			}else{
				float speed = Float.valueOf(df2.format(cdata.Speed));
				tvSpeed.setText(String.valueOf(speed+" 米/秒"));// 速度
			}
			tvGpsTime.setText(String.format("%s", cdata.GpsTime));// 卫星时间
			cdata.InfoType = infotype;// GPS状态
		}
	}
	
	/**
	 * 距离最近作业点计算
	 */
	public void CalTaskDistance(){
		// 计算当前位置坐标离最近作业点的实际距离
		currentPoint = DistanceComm.lonlatToMercator(Double.valueOf(tvLongitude.getText().toString()),Double.valueOf(tvLatitude.getText().toString()));// 当前位置点
		/*Double x1 = 116.32741+Double.valueOf("0.0"+new Random().nextInt(10));
		Double y1 = 39.98436+Double.valueOf("0.0"+new Random().nextInt(10));
		Toast.makeText(context, Double.valueOf("0.0"+new Random().nextInt(10))+"", 100).show();
		currentPoint = DistanceComm.lonlatToMercator(x1, y1);// 当前位置点 */
				
		// 查询当前人的作业点坐标
		PatrolTaskPoint[] taskPointList = dbAdepter.queryTaskPoint(subline_id);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sbId = new StringBuilder();
		if (taskPointList != null) {
			for (int i = 0; i < taskPointList.length; i++) {
				taskId = taskPointList[i].id;
				sbId.append(taskId + ",");
				taskPoint = DistanceComm.lonlatToMercator(Double.valueOf(taskPointList[i].longitude),Double.valueOf(taskPointList[i].latitude));
				taskStraightLineDis = DistanceComm.lineSpace(currentPoint[0],currentPoint[1], taskPoint[0], taskPoint[1]);
				System.out.println("taskId："+taskId+"，作业点距离[" + m + "]：" + taskStraightLineDis);
				sb1.append(taskStraightLineDis + ",");
				m++;
			}
		}
		String taskIdList = null;
		String taskIdSplit[] = null;
		String str1 = "";
		if(sbId.length() >= 1){
			taskIdList = sbId.toString().substring(0, sbId.toString().length() - 1);
		}
		taskIdSplit = taskIdList.split(",");
		if (sb1.length() >= 1) {
			str1 = sb1.toString().substring(0, sb1.toString().length() - 1);
		}
		String[] pointStr1 = str1.split(",");
		int[] point1 = new int[pointStr1.length];
		for (int i = 0; i < point1.length; i++) {
			point1[i] = Integer.valueOf(pointStr1[i]);
		}
		min1 = point1[0];
		int temp1 = 0;
		for (int i = 0; i < point1.length; i++) {
			if (point1[i] < min1) {
				temp1 = min1;
				min1 = point1[i];
				taskStrId = taskIdSplit[i];
				point1[i] = temp1;
				min_i = i;
			}
		}
		if(min1>0){
			min1IsFlag = true;
		}else{
			min1IsFlag = false;
		}
		tv_taskId.setText(taskStrId);
		tv5_value.setText(min1 + " 米");
		System.out.println(taskIdSplit[min_i]+"距最近作业点：" + min1);
		
		/*//距最近桩点
		String subline_id = sp.getString("subline_id", "");
		PipemodeCenterLineStake[] pilePointList = dbAdepter.queryPilePoint(subline_id);
		StringBuilder sb0 = new StringBuilder();

		if (pilePointList != null) {
			for (int i = 0; i < pilePointList.length; i++) {
				pilePoint1 = DistanceComm.lonlatToMercator(pilePointList[i].x,pilePointList[i].y);
				for (int j = 1; j < pilePointList.length; j++) {
					pilePoint2 = DistanceComm.lonlatToMercator(pilePointList[j].x, pilePointList[j].y);
					pileLine = DistanceComm.pointToLine(currentPoint[0],currentPoint[1], pilePoint1[0], pilePoint1[1],pilePoint2[0], pilePoint2[1]); 
					System.out.println("桩点距离[" + k + "]：" + pileLine);
					sb0.append(pileLine + ",");
					k++;
				}
			}
		}
		String str0 = "";
		if (sb0.length() >= 1) {
			str0 = sb0.toString().substring(0, sb0.toString().length() - 1);
		}
		String[] pointStr0 = str0.split(",");
		int[] point0 = new int[pointStr0.length];
		for (int i = 0; i < point0.length; i++) {
			point0[i] = Integer.valueOf(pointStr0[i]);
		}
		min0 = point0[0];
		int temp0 = 0;
		for (int i = 0; i < point0.length; i++) {
			if (point0[i] < min0) {
				temp0 = min0;
				min0 = point0[i];
				point0[i] = temp0;
			}
		}
		System.out.println("距最近桩点：" + min0);
		Toast.makeText(context, "距最近桩点：" + min0, Toast.LENGTH_SHORT).show();*/
	}

	/**
	 * 计算
	 */
	public void CalDistance() {
		/**
		 * 当前系统时间
		 */
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		// 存储GPS巡检数据到本地
		PatrolPersonPath bean = new PatrolPersonPath();
		bean.PERSON_ID = sp.getString("user_id", "");// 当前巡检人
		bean.LONGITUDE = tvLongitude.getText().toString();// 经度
		bean.LATITUDE = tvLatitude.getText().toString();// 纬度
		String highSplit[] = tvHigh.getText().toString().split(" ");
		bean.HEIGHT = highSplit[0].toString();// 海拔
		bean.PATH_POINT_TIME = dateString;// 上传时间
		bean.BUG_ID = "0";// 缺陷报告ID
		bean.IS_SUCCSEND = "0";// 是否上传成功(和服务端进行交互)//巡检的速度
		String speedSplit[] = tvSpeed.getText().toString().split(" ");
		bean.SPEED = speedSplit[0].toString();
		bean.PIPEDEVIATION = String.valueOf(min0);// 距离最近桩点的距离
		bean.PHOTO_NAME = "0";// 上传照片的名称

		if (sp.getString("start", "").equals("true")) {// 开始巡检
			String taskDistanceSplit[] = tv5_value.getText().toString().split(" ");
			if(isSnrLocation){//卫星信号正常，可以巡检轨迹
				if(Double.valueOf(tvLongitude.getText().toString())>0 || Double.valueOf(tvLatitude.getText().toString())>0){
					//巡检作业路径点
					bean.PRINCIPAL_TYPE = "0";// 是否是作业点（路径点和作业点）
					bean.PRINCIPAL_ID = "0";// 作业点的ID
					dbAdepter.insertPatrolPersonPath(bean);
					// =========================判断上传巡检作业点
					if (Integer.valueOf(taskDistanceSplit[0]) <= UPDistance) {
						// 距离最近作业点和上传参数比较
						bean.PRINCIPAL_TYPE = "1";// 是否是作业点（路径点和作业点）
						bean.PRINCIPAL_ID = tv_taskId.getText().toString();// 作业点的ID
						PatrolPersonPath[] pList = dbAdepter.queryPatrolPersonPathTaskPointByCurrentTaskPoint(sp.getString("user_id", ""),tv_taskId.getText().toString(), dateF);
						if (pList == null) {
							dbAdepter.insertPatrolPersonPath(bean);
						}
					}
					// ==========================判断上传巡检作业点
				}
			}
		}

		//超速
		String speedSplit1[] = tvSpeed.getText().toString().split(" ");
		Double speed = Double.valueOf(speedSplit1[0]);
		if (speed > OverSpeed) {
			speedFlag = true;
			playSound(2, 0);//超速
		} else {
			speedFlag = false;
		}

		//接近巡检作业点范围
		if (StartRange < min1 && min1 < EndRange) {
			ComeNearFlag = true;
			playSound(3, 0);//接近巡检作业点
		} else {
			ComeNearFlag = false;
		}

		//超出巡检范围(偏离管道参数(米)
		if (min0 > PipeDeviation) {
			CrossingFlag = true;
			playSound(4, 0);//超出巡检范围
		} else {
			CrossingFlag = false;
		}

		//事件跟踪请拍照(上传到照片表)

	}

	/**
	 * 作业点记录巡检轨迹定时器
	 */
	Handler handlerAddTask = new Handler();
	Runnable runnable = new Runnable() {
		public void run() {
			// handler自带方法实现定时器
			try {
				handlerAddTask.postDelayed(this, gpsUploadTime);
				CalDistance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 开始巡检
	 */
	public void getStart(View v) {
		boolean gpsFlag = isOPen(context);
		if (gpsFlag) {// gps已开启
			Editor et = sp.edit();
			et.putString("start", "true");// 是否巡检标识
			et.commit();
			flag = true;
			initLocation();// 开始巡检时，初始化数据
			handlerAddTask.postDelayed(runnable, gpsUploadTime);// 作业点记录巡检轨迹定时器
			// 锁定底部按钮
			inspection_linearLayout.setVisibility(View.GONE);
			locked_linearLayout.setVisibility(View.VISIBLE);
			get_start.setVisibility(View.GONE);
			get_end.setVisibility(View.VISIBLE);
		} else {// gps未开启
			CustomSingleDialog.Builder builder = new CustomSingleDialog.Builder(
					context);
			builder.setTitle("提示");
			builder.setMessage("开启GPS ?");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							openGPS(context);// 开启gps
							dialog.dismiss();
						}
					});
			builder.create().show();
		}

	}

	/**
	 * 停止巡检
	 */
	public void getEnd(View v) {
		get_end.setVisibility(View.GONE);
		get_start.setVisibility(View.VISIBLE);
		if(sp.getString("start", "").equals("true")){
			NewToast.makeText(context, R.drawable.sure_icon, "已结束巡检",Toast.LENGTH_LONG).show();
			Editor et = sp.edit();
			et.putString("start", "false");// 是否巡检标识
			et.commit();
			/*find_star.setText("");
			MyData data = new MyData();
			data.setName("");// 设为空，针对不同机型、不同屏幕位置不好控制
			snrList = new ArrayList<Integer>();
			data.setDataList(snrList);
			data.setColor(0);// 信噪比柱状条颜色
			RaceCommon.DataSeries = new ArrayList<MyData>();
			RaceCommon.DataSeries.add(data);
			*/
			showInfo(null, -1);// 结束巡检
			flag = false;
		}else{
			NewToast.makeText(context, R.drawable.validate, "不在巡检状态",Toast.LENGTH_LONG).show();
		}
	}

	// 初始化播放声音方法
	public void InitSound() {
		spl = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		spMap = new HashMap<Integer, Integer>();
		spMap.put(1, spl.load(this, R.raw.notificationsound, 1));// 上传巡检数据成功
		spMap.put(2, spl.load(this, R.raw.cs, 1));// 超速
		spMap.put(3, spl.load(this, R.raw.onece, 1));// 接近巡检作业点范围
		spMap.put(4, spl.load(this, R.raw.yj, 1));// 超出巡检范围
		spMap.put(5, spl.load(this, R.raw.qpz, 1));// 事件跟踪请拍照
	}

	public void playSound(int sound, int number) {
		AudioManager am = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float volumnRatio = volumnCurrent / audioMaxVolumn;
		spl.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1f);
	}

	/**
	 * 拍照上传－显示底部window
	 */
	private void showDialog() {
		View view = getLayoutInflater().inflate(R.layout.photo_choose_dialog,
				null);

		photo_locale_takephotos = (Button) view
				.findViewById(R.id.photo_locale_takephotos);
		photo_cancel = (Button) view.findViewById(R.id.photo_cancel);

		photo_locale_takephotos.setOnClickListener(onClickListener);
		photo_cancel.setOnClickListener(onClickListener);

		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围不解散
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 压缩图片尺寸
	 * 
	 * @param pathName
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public Bitmap compressBySize(String pathName, int targetWidth,
			int targetHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
		Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
		// 得到图片的宽度、高度；
		float imgWidth = opts.outWidth;
		float imgHeight = opts.outHeight;
		// 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
		int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
		int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
		opts.inSampleSize = 1;
		if (widthRatio > 1 || widthRatio > 1) {
			if (widthRatio > heightRatio) {
				opts.inSampleSize = widthRatio;
			} else {
				opts.inSampleSize = heightRatio;
			}
		}
		// 设置好缩放比例后，加载图片进内容；
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName, opts);
		return bitmap;
	}

	// 存储到SD卡
	public void saveFile(Bitmap bm, String fileName) throws Exception {
		File dirFile = new File(fileName);
		// 检测图片是否存在
		if (dirFile.exists()) {
			dirFile.delete(); // 删除原图片
		}
		File myCaptureFile = new File(fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		// 100表示不进行压缩，70表示压缩率为30%
		bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
		bos.flush();
		bos.close();
	}

	/**
	 * pop
	 */
	public OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.photo_locale_takephotos:// 拍照
				Intent intentTakePhoto = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				String personId = sp.getString("user_id", "");
				Calendar cal = Calendar.getInstance();
				int year = cal.get(Calendar.YEAR);
				// 系统上传时间
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = formatter.format(currentTime);
				String picPath = Environment.getExternalStorageDirectory()
						+ "/GPS巡检/" + personId + "/" + year + "/" + dateString
						+ "/";
				File temp = new File(picPath);// 创建文件夹
				temp.mkdirs();
				if (!temp.exists()) {
					temp.mkdirs();
				}
				String tempName = new SimpleDateFormat("yyyyMMddhhmmss")
						.format(new Date()) + ".jpg";
				imgFile = picPath + tempName;
				tempFile = new File(imgFile); // 以时间秒为文件名
				intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(tempFile));
				// intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT,
				// Uri.fromFile(new
				// File(Environment.getExternalStorageDirectory(),
				// "temp.jpg")));
				System.out.println("图片路径："
						+ Environment.getExternalStorageDirectory());
				startActivityForResult(intentTakePhoto, PHOTOHRAPH);
				dialog.dismiss();
				break;
			case R.id.photo_cancel:// 取消
				dialog.dismiss();
				break;
			default:
				break;
			}
		}
	};

	protected Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在根目录下
			if (resultCode != RESULT_OK) {
				return;
			}
			if (tempFile != null && tempFile.exists()) {
				try {
					// 压缩图片
					Bitmap bitmap = compressBySize(tempFile.getPath(), 1080,
							1920);
					// 保存图片
					saveFile(bitmap, tempFile.getPath());
				} catch (Exception e) {
					e.printStackTrace();
				}
				takephoto_linearlayout.setVisibility(View.VISIBLE);
				uploadImageView.setImageBitmap(null);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap photo = BitmapFactory.decodeFile(tempFile.getPath(),
						options);
				uploadImageView
						.setBackgroundDrawable(new BitmapDrawable(photo));
			} else {
				NewToast.makeText(this, R.drawable.validate, "无法加载图片.",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/**
	 * 拍照上传
	 */
	public OnClickListener uploadPhoto = new OnClickListener() {
		public void onClick(View v) {
			PatrolBugPhoto bo = new PatrolBugPhoto();
			String content = (String) patrol_bug_list.getSelectedItem();
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String dateString = formatter.format(currentTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			content = content + "（" + format.format(currentTime) + "）";
			String longitude = (String) tvLongitude.getText();
			String latitude = (String) tvLatitude.getText();
			String height = (String) tvHigh.getText();
			String time = dateString;
			String personId = sp.getString("user_id", "");
			bo.setCONTENT(content);
			bo.setLONGITUDE(longitude);
			bo.setLATITUDE(latitude);
			bo.setHEIGHT(height);
			bo.setTIME(time);
			bo.setPERSON_ID(personId);
			switch (v.getId()) {
			case R.id.upload_photo_imgv:
				showDialog();// 从底部弹出菜单
				break;
			case R.id.photo_upload_btn:// 上传
				// 判断是否是否在线和离线
				ConnectivityManager connectMgr = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo mobNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);// 判断是否正在使用GPRS网络
				NetworkInfo wifiNetInfo = connectMgr
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 判断是否正在使用WIFI网络
				// 网络状态判断
				if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {// 在线
					try {
						String urlServer = URLs.UPLOAD_BUGPATROL_HTTP;// 图片拍照上传
						String webContent = HttpRequest.doPost(urlServer,
								imgFile, bo);
						if(webContent.equals("failure")){
							NewToast.makeText(context, R.drawable.validate, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
							return;
						}
						JSONObject jsonObject;
						jsonObject = new JSONObject(webContent);
						String msg = jsonObject.getString("msg");
						String result = jsonObject.getString("result");
						String data = jsonObject.getString("data");
						if (result.equalsIgnoreCase("true")) {
							uploadImageView.setImageBitmap(null);
							takephoto_linearlayout.setVisibility(View.GONE);// 隐藏上传图片窗体
							NewToast.makeText(context, R.drawable.sure_icon,
									"上传成功", Toast.LENGTH_SHORT).show();
						} else {
							NewToast.makeText(context, R.drawable.validate,
									"上传失败", Toast.LENGTH_SHORT).show();
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					NewToast.makeText(context, R.drawable.validate,
							R.string.network_not_connected, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.photo_save_btn:
				bo.BUG_REPORT_ID = 0;// 初始不设值
				bo.PHOTO_NAME = tempFile.getPath();// 本地照片存储路径
				bo.CONTENT = content;
				bo.LONGITUDE = longitude;
				bo.LATITUDE = latitude;
				bo.TIME = time;
				bo.ISUPLOAD = "0";// 初始化状态为上传
				bo.STATUS = "0";// 待上传
				bo.PERSON_ID = personId;
				bo.HEIGHT = height;
				int num = dbAdepter.insertPatrolBugPhoto(bo);// 本地保存缺陷报告照片数据
				if (num > 0) {
					uploadImageView.setImageBitmap(null);
					takephoto_linearlayout.setVisibility(View.GONE);// 隐藏上传图片窗体
					NewToast.makeText(context, R.drawable.sure_icon, "保存成功",
							Toast.LENGTH_SHORT).show();
				} else {
					NewToast.makeText(context, R.drawable.validate, "保存失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.photo_cancel_btn:
				takephoto_linearlayout.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps) {
			return true;
		}
		return false;
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解锁
	 * 
	 * @param v
	 */
	public void getLock(View v) {
		// 自定义弹出提示框-模仿iOS8弹出对话框
		CustomLockDoubleDialog.Builder builder = new CustomLockDoubleDialog.Builder(
				context);
		builder.setTitle("解锁");
		builder.setMessage("解锁按钮？");
		builder.setPositiveButton("解锁", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				locked_linearLayout.setVisibility(View.GONE);
				inspection_linearLayout.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	/**
	 * 百度地图
	 * 
	 * @param v
	 */
	public void getBaiduMap(View v) {
		Intent intent = new Intent();
		intent.setClass(context, BaiduActivity.class);
		startActivity(intent);
	}

	/**
	 * 巡检待上传
	 * 
	 * @param v
	 */
	public void uploadPage(View v) {
		Intent intent = new Intent();
		intent.setClass(context, StayUploadActivity.class);
		startActivity(intent);
	}

	// 巡检作业缺陷报告照片上传
	public void getBugReportUploadGPSData() {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);// 判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 判断是否正在使用WIFI网络
		// 网络状态判断
		if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {// 在线上传巡检数据
			PatrolBugPhoto bo = new PatrolBugPhoto();
			PatrolBugPhoto[] pbList = dbAdepter
					.queryPatrolBugPhotoIsUploadBy0_2(user_id);
			if (pbList != null) {
				for (int i = 0; i < pbList.length; i++) {
					String content = pbList[i].CONTENT;
					String longitude = pbList[i].LONGITUDE;
					String latitude = pbList[i].LATITUDE;
					String height = pbList[i].HEIGHT;
					String time = pbList[i].TIME;
					String personId = pbList[i].PERSON_ID;
					imgFile = pbList[i].PHOTO_NAME;
					dbAdepter.lockPatrolBugPhotoByPersonId(personId, time);// 锁定该条数据
					bo.setCONTENT(content);
					bo.setLONGITUDE(longitude);
					bo.setLATITUDE(latitude);
					bo.setHEIGHT(height);
					bo.setTIME(time);
					bo.setPERSON_ID(personId);
					try {
						String urlServer = URLs.UPLOAD_BUGPATROL_HTTP;// 图片拍照上传
						if (locked) {// 锁定上传，待本次上传成功以后再解锁
							locked = false;
							String webContent = HttpRequest.doPost(urlServer,
									imgFile, bo);
							System.out.println("接收得返回值：" + webContent);
							// {"data":{"time":"2014/12/25 15:05:48","person_id":311899},"msg":"SUCCESS","result":true}
							JSONObject jsonObject;
							jsonObject = new JSONObject(webContent);
							String msg = jsonObject.getString("msg");
							String result = jsonObject.getString("result");
							String data = jsonObject.getString("data");
							if (msg.equalsIgnoreCase("SUCCESS")) {
								jsonObject = new JSONObject(data);
								String person_id = jsonObject
										.getString("person_id");
								String timeStr = jsonObject.getString("time");
								dbAdepter.updatePatrolBugPhotoByPersonId(
										person_id, timeStr);// 更新上传成功的缺陷照片数据
								locked = true;
							} else {
								locked = true;
								dbAdepter.initPatrolBugPhotoByPersonId(user_id);// 重置未上传成功的缺陷照片数据
							}
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					//
				}
			}
		} else {// 离线
				// 离线不做处理
		}
	}

	// 巡检作业点上传数据
	public void getUploadGPSData() {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);// 判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);// 判断是否正在使用WIFI网络
		// 网络状态判断
		if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {// 在线上传巡检数据
			PatrolPersonPath[] pList = dbAdepter
					.queryPatrolPersonPathByPersonId(user_id);
			String PERSON_ID = "";
			String LONGITUDE = "";
			String LATITUDE = "";
			String HEIGHT = "";
			String PATH_POINT_TIME = "";
			String PRINCIPAL_TYPE = "";
			String PRINCIPAL_ID = "";
			String BUG_ID = "";
			String IS_SUCCSEND = "";
			String SPEED = "";
			String PIPEDEVIATION = "";
			String PHOTO_NAME = "";
			if (pList != null) {
				int count = pList.length;
				for (int i = 0; i < pList.length; i++) {
					PERSON_ID = pList[i].PERSON_ID;
					LONGITUDE = pList[i].LONGITUDE;
					LATITUDE = pList[i].LATITUDE;
					HEIGHT = pList[i].HEIGHT;
					PATH_POINT_TIME = pList[i].PATH_POINT_TIME;
					PRINCIPAL_TYPE = pList[i].PRINCIPAL_TYPE;
					PRINCIPAL_ID = pList[i].PRINCIPAL_ID;
					BUG_ID = pList[i].BUG_ID;
					IS_SUCCSEND = "1";
					SPEED = pList[i].SPEED;
					PIPEDEVIATION = pList[i].PIPEDEVIATION;
					PHOTO_NAME = pList[i].PHOTO_NAME;
					if (PRINCIPAL_TYPE.equals("")) {
						PRINCIPAL_TYPE = "0";
					}
					if (PRINCIPAL_ID.equals("")) {
						PRINCIPAL_ID = "0";
					}
					if (BUG_ID.equals("")) {
						BUG_ID = "0";
					}
					String[] gpsTime = PATH_POINT_TIME.split(" ");
					PATH_POINT_TIME = gpsTime[0] + "=" + gpsTime[1];
					String uploadPath = URLs.UPLOAD_HTTP + "/" + PERSON_ID
							+ "/" + LONGITUDE + "/" + LATITUDE + "/" + HEIGHT
							+ "/" + PATH_POINT_TIME + "/" + PRINCIPAL_TYPE
							+ "/" + PRINCIPAL_ID + "/" + BUG_ID + "/"
							+ IS_SUCCSEND + "/" + SPEED + "/" + PIPEDEVIATION
							+ "/" + PHOTO_NAME;
					System.out.println(uploadPath);
					dbAdepter.updatePatrolPersonPathByPersonId(PERSON_ID,
							PATH_POINT_TIME);// 锁定本地巡检表正在上传的数据状态
					if (locked) {// 锁定上传，待本次上传成功以后再解锁
						locked = false;
						String webContent = HttpRequest.doGet(uploadPath);
						System.out.println("接收得返回值：" + webContent);
						// {"data":{"personId":"311899","pathPointTime":"2014-12-24 11:11:11"},"msg":"SUCCESS","result":true}
						String personId = "";
						String pathPointTime = "";
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(webContent);
							String msg = jsonObject.getString("msg");
							String result = jsonObject.getString("result");
							String data = jsonObject.getString("data");
							if (msg.equalsIgnoreCase("SUCCESS")) {
								jsonObject = new JSONObject(data);
								personId = jsonObject.getString("personId");
								pathPointTime = jsonObject
										.getString("pathPointTime");
								dbAdepter.updatePatrolPersonPathByPersonId(
										personId, pathPointTime);// 更新本地巡检表已上传的数据状态
								locked = true;
							} else {
								locked = true;
								dbAdepter.initPatrolPersonPathByPersonId(
										personId, pathPointTime);// 重置本地巡检表上传未成功的数据状态
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

				}
			}
		} else {// 离线
				// 离线不做处理
		}

	}
	
	// 距离计算-异步处理
	public class CalDistanceAsyncTask extends AsyncTask<String, Integer, String> {
		/*
		 * 第一个参数为doInBackground传入的参数，
		 * 第二个参数为onProgressUpdate传入参数,由doInBackground内调用publishProgress
		 * (Interger)传值; 第三个为doInBackground返回值和onPostExecute传入的参数。
		 */
		@Override
		protected void onPreExecute() {
			// 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上转菊花提示
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。
			// 这里将主要负责执行那些很耗时的后台计算工作。
			CalTaskDistance();
			return "doInBackground";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			System.out.println("更新进度" + values[0]);
			// 在publishProgress方法被调用后，UI
			// thread将调用这个方法从而在界面上展示任务的进展情况，例如通过一个进度条进行展示。
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// 在doInBackground 执行完成后，onPostExecute 方法将被UI
			// thread调用，后台的计算结果将通过该方法传递到UI thread
			// 用于更新UI
		}

		@Override
		protected void onCancelled() {
			// 当用户取消操作时.
			super.onCancelled();
		}
	}

	// 巡检作业-异步处理
	public class UploadAsyncTask extends AsyncTask<String, Integer, String> {
		/*
		 * 第一个参数为doInBackground传入的参数，
		 * 第二个参数为onProgressUpdate传入参数,由doInBackground内调用publishProgress
		 * (Interger)传值; 第三个为doInBackground返回值和onPostExecute传入的参数。
		 */
		@Override
		protected void onPreExecute() {
			// 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上转菊花提示
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。
			// 这里将主要负责执行那些很耗时的后台计算工作。
			if (toDoCount > 0) {
				getUploadGPSData();
			} else if (toDoBugCount > 0) {
				getBugReportUploadGPSData();
			}
			return "doInBackground";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			System.out.println("更新进度" + values[0]);
			// 在publishProgress方法被调用后，UI
			// thread将调用这个方法从而在界面上展示任务的进展情况，例如通过一个进度条进行展示。
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// 在doInBackground 执行完成后，onPostExecute 方法将被UI
			// thread调用，后台的计算结果将通过该方法传递到UI thread
			// 用于更新UI
		}

		@Override
		protected void onCancelled() {
			// 当用户取消操作时.
			super.onCancelled();
		}
	}

	/*
	 * 现场拍照
	 */
	public void takePhotos(View v) {
		Intent intentTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String personId = sp.getString("user_id", "");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		// 系统上传时间
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		String picPath = Environment.getExternalStorageDirectory() + "/GPS巡检/"
				+ personId + "/" + year + "/" + dateString + "/";
		File temp = new File(picPath);// 创建文件夹
		temp.mkdirs();
		if (!temp.exists()) {
			temp.mkdirs();
		}
		String tempName = new SimpleDateFormat("yyyyMMddhhmmss")
				.format(new Date()) + ".jpg";
		imgFile = picPath + tempName;
		tempFile = new File(imgFile); // 以时间秒为文件名
		intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(tempFile));
		// intentTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
		// File(Environment.getExternalStorageDirectory(), "temp.jpg")));
		System.out.println("图片路径：" + Environment.getExternalStorageDirectory());
		startActivityForResult(intentTakePhoto, PHOTOHRAPH);
	}
	
	/*
	 * 现场微录
	 */
	public void vlookVideo(View v){
		Intent intent = new Intent();
		intent.setClass(context, FFmpegRecorderActivity.class);
		startActivity(intent);
	}
	

	@Override
	protected void onDestroy() {
		if (dbgps != null) {
			dbgps.closeDB();
			dbgps = null;
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
		unregisterReceiver(batteryLevelRcvr);
	}

	/**
	 * 设置
	 * 
	 * @param v
	 */
	public void settings(View v) {
		Intent intent = new Intent();
		intent.setClass(context, SettingsActivity.class);
		startActivity(intent);
	}

	/**
	 * 电池电量显示设置
	 */
	private void monitorBatteryState() {
		batteryLevelRcvr = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				StringBuilder sb = new StringBuilder();
				int rawlevel = intent.getIntExtra("level", -1);
				int scale = intent.getIntExtra("scale", -1);
				int status = intent.getIntExtra("status", -1);
				int health = intent.getIntExtra("health", -1);
				int level = -1; // percentage, or -1 for unknown
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				sb.append("您的设备");
				if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
					sb.append("发热量比较大");
				} else {
					switch (status) {
					case BatteryManager.BATTERY_STATUS_UNKNOWN:
						sb.append("电量不足");
						break;
					case BatteryManager.BATTERY_STATUS_CHARGING:
						sb.append("电量剩余");
						if (level == 100) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_hundred));
						} else if (level >= 90 && level < 100) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_nighty));
						} else if (level >= 80 && level < 90) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_eighty));
						} else if (level >= 70 && level < 80) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_seventy));
						} else if (level >= 60 && level < 70) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_sisty));
						} else if (level >= 50 && level < 60) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_fifty));
						} else if (level >= 40 && level < 50) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_forty));
						} else if (level >= 30 && level < 40) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_thirty));
						} else if (level >= 20 && level < 30) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_twenty));
						} else if (level >= 10 && level < 20) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_ten));
						} else if (level >= 0 && level < 10) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_zero));
						}

						break;
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						if (level == 100) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_hundred));
						} else if (level >= 90 && level < 100) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_nighty));
						} else if (level >= 80 && level < 90) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_eighty));
						} else if (level >= 70 && level < 80) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_seventy));
						} else if (level >= 60 && level < 70) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_sisty));
						} else if (level >= 50 && level < 60) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_fifty));
						} else if (level >= 40 && level < 50) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_forty));
						} else if (level >= 30 && level < 40) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_thirty));
						} else if (level >= 20 && level < 30) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_twenty));
						} else if (level >= 10 && level < 20) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_ten));
						} else if (level >= 0 && level < 10) {
							battery_icon.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.battery_zero));
						}

						break;
					case BatteryManager.BATTERY_STATUS_FULL:
						sb.append("电量已经充满");
						break;
					default:
						sb.append("电量未知");
						break;
					}
				}
				sb.append(' ');
				battery_soc.setText(level + "%");
			}
		};
		batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelRcvr, batteryLevelFilter);
	}

	/**
	 * 监听返回-是否退出程序
	 */
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {// 菜单键
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
					.parseColor("#00000000")));
			mPopupWindow.showAtLocation(bottom_relativeLayout, Gravity.BOTTOM,
					0, 0);
			mPopupWindow.setAnimationStyle(R.style.app_pop);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.update();
		}
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {// 返回键
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				NewToast.makeText(context, R.drawable.sure_icon, "再按一次  退出系统",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				lm.removeUpdates(locationListener);//移除巡检
				lm.removeGpsStatusListener(statuslistener);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}