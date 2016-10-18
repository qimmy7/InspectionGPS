package com.inspection.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.inspection.app.R;
import com.inspection.app.adapter.SyncDbAdapter;
import com.inspection.app.bean.BugPatrol;
import com.inspection.app.bean.Param;
import com.inspection.app.bean.PatrolTaskPoint;
import com.inspection.app.bean.PipemodeCenterLineStake;
import com.inspection.app.bean.Subline;
import com.inspection.app.bean.User;
import com.inspection.app.common.AppContext;
import com.inspection.app.common.http.HttpRequest;
import com.inspection.app.common.http.URLs;
import com.inspection.app.widget.AppToast;
import com.inspection.app.widget.CustomDoubleDialog;
import com.inspection.app.widget.CustomProgressDialogA;
import com.inspection.app.widget.CustomSingleDialog;
import com.inspection.app.widget.NewToast;
import com.inspection.app.widget.UISwitchButton;

/**
 *  登录
 * @author liuyx
 * @created by 2014/9/30
 */
@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnItemSelectedListener {
	
	private Context context;
	private AppContext appContext;// 全局Context
	
	private LinearLayout linearLayout,ll_user;
	private RelativeLayout rl_login_top,rl_copyright_bottom;
	private Button loginButton;
	private Spinner userName;//用户名
	private EditText passWord;//密码
	private View empty_username_line,full_username_line,empty_password_line,full_password_line;
	private UISwitchButton rememberPwd,automaticLogin;
	private SharedPreferences sp;// 声明一个SharedPreferences,存储用户信息
	private String userNameValue, passWordValue;
	
	private LinearLayout sync_login_linearLayout,register_linearLayout;
	
	private SharedPreferences deviceInfo;// 存储设备信息
	private String equnum;
	private String sn;
	private String imei;
	private String sim;
	private String os_version;
	
	private List<String> data_list;
	private ArrayAdapter<String> arr_adapter;
	public SyncDbAdapter dbAdepter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//界面不浮动
		setContentView(R.layout.login_panel);
		context = this;
		//应用程序异常问题处理
		appContext = (AppContext) getApplication();
		//打开本地数据库
		dbAdepter = new SyncDbAdapter(context);
        dbAdepter.open();
		// 获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		deviceInfo = this.getSharedPreferences("device", Context.MODE_WORLD_READABLE);
		
		// 模拟线程
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		/*StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());*/
		
		rl_login_top = (RelativeLayout)this.findViewById(R.id.rl_login_top);
		//rl_login_top.setAlpha((float) 0.6);
		TranslateAnimation anim1 = new TranslateAnimation(0, 0, 3000, 0);//设置动画
		//anim1.setInterpolator(new BounceInterpolator());
		anim1.setDuration(800);
		rl_login_top.setAnimation(anim1);
		
		linearLayout = (LinearLayout)this.findViewById(R.id.login_layout);
		rl_copyright_bottom = (RelativeLayout)this.findViewById(R.id.copyright_layout);
		AnimationSet setCopyright = new AnimationSet(true);
		Animation animationCopyright = AnimationUtils.loadAnimation(this,R.anim.login_in);
		setCopyright.addAnimation(animationCopyright);
		rl_copyright_bottom.setAnimation(setCopyright);
		
		View layout = LayoutInflater.from(this).inflate(R.layout.login, null);
		
		sync_login_linearLayout = (LinearLayout)layout.findViewById(R.id.sync_login_linearLayout);
		register_linearLayout = (LinearLayout)layout.findViewById(R.id.register_linearLayout);
		
		//设备注册
		if(deviceInfo.getString("register", "").equals("yes")){
			register_linearLayout.setVisibility(View.GONE);
			sync_login_linearLayout.setVisibility(View.VISIBLE);
		}else{
			sync_login_linearLayout.setVisibility(View.GONE);
			register_linearLayout.setVisibility(View.VISIBLE);
		}
		
		AnimationSet set = new AnimationSet(true);
		Animation animation = AnimationUtils.loadAnimation(this,R.anim.login_in);
		//animation.setDuration(500);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
		linearLayout.setLayoutAnimation(controller);
		linearLayout.addView(layout);
		
		ll_user = (LinearLayout)this.findViewById(R.id.ll_user);//
		//布局设置淡入淡出动画
		AnimationSet setUserInfo = new AnimationSet(true);
		Animation animationUserInfo = AnimationUtils.loadAnimation(this,R.anim.alpha_anim);
		setUserInfo.addAnimation(animationUserInfo);
		LayoutAnimationController controllerUserInfo = new LayoutAnimationController(setUserInfo, 0.5f);
		ll_user.setLayoutAnimation(controllerUserInfo);
		loginButton = (Button)this.findViewById(R.id.login_button);
		//登录按钮设置淡入淡出动画
		AnimationSet setButton = new AnimationSet(true);
		Animation animationConfigButton = AnimationUtils.loadAnimation(this,R.anim.alpha_anim);
		setButton.addAnimation(animationConfigButton);
		loginButton.setAnimation(setButton);
		
		userName = (Spinner) findViewById(R.id.user_name);
		passWord = (EditText) findViewById(R.id.user_pwd);
		
		empty_username_line = (View)findViewById(R.id.empty_username_line);
		full_username_line = (View)findViewById(R.id.full_username_line);
		empty_password_line = (View)findViewById(R.id.empty_password_line);
		full_password_line = (View)findViewById(R.id.full_password_line);
		
		//密码输入框改变icon
		passWord.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (passWord.getText().length() > 0) {
					empty_password_line.setVisibility(View.GONE);
					full_password_line.setVisibility(View.VISIBLE);
				} else {
					empty_password_line.setVisibility(View.VISIBLE);
					full_password_line.setVisibility(View.GONE);
				}
			}
		});
		
		rememberPwd = (UISwitchButton)findViewById(R.id.remember_pwd);
		automaticLogin = (UISwitchButton)findViewById(R.id.automatic_login);
		
		
		// 判断记住密码框的状态
		if (sp.getBoolean("ISCHECK", true)) {
			// 设置默认是记录密码状态
			rememberPwd.setChecked(true);
			userName.setPrompt(sp.getString("USER_NAME", ""));
			passWord.setText(sp.getString("PASSWORD", ""));
		} else {
			rememberPwd.setChecked(false);
		}
		
		// 判断自动登录的状态
		if (sp.getBoolean("AUTO_LOGIN", true)) {
			automaticLogin.setChecked(true);
		} else {
			automaticLogin.setChecked(false);
		}

		// 监听记住密码
		rememberPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					// 记住密码已打开
					rememberPwd.setChecked(true);
					sp.edit().putBoolean("ISCHECK", true).commit();
					NewToast.makeText(context, R.drawable.sure_icon, R.string.rem_open, Toast.LENGTH_SHORT).show();
				} else {
					rememberPwd.setChecked(false);
					automaticLogin.setChecked(false);
					// 记住密码已关闭
					sp.edit().putBoolean("ISCHECK", false).commit();
					NewToast.makeText(context, R.drawable.sure_icon, R.string.rem_close, Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// 监听自动登录
		automaticLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					rememberPwd.setChecked(true);
					automaticLogin.setChecked(true);
					// 自动登录已打开
					sp.edit().putBoolean("AUTO_LOGIN", true).commit();
					NewToast.makeText(context, R.drawable.sure_icon, R.string.auto_login_open, Toast.LENGTH_SHORT).show();  
				} else {
					// 自动登录已关闭
					automaticLogin.setChecked(false);
					sp.edit().putBoolean("AUTO_LOGIN", false).commit();
					NewToast.makeText(context, R.drawable.sure_icon, R.string.auto_login_close, Toast.LENGTH_SHORT).show();  
				}
			}
		});
		loadSpinnerProvincias();
	}
	
	/**
	 * 用户下拉选择
	 */
	private void loadSpinnerProvincias() {
		//数据
        data_list = new ArrayList<String>();
        User[] uList = dbAdepter.queryUser();
		if(uList!=null){
			for (int j = 0; j < uList.length; j++) {
				data_list.add(uList[j].name);
			}
		}else{
			data_list.add("");
		}
        //适配器
        arr_adapter= new ArrayAdapter<String>(context, R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //加载适配器
        userName.setAdapter(arr_adapter);
		userName.setOnItemSelectedListener(this);
	}
	
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(!userName.getSelectedItem().toString().equals("")){
			//NewToast.makeText(context, R.drawable.sure_icon, userName.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
			empty_username_line.setVisibility(View.GONE);
			full_username_line.setVisibility(View.VISIBLE);
		}else{
			empty_username_line.setVisibility(View.VISIBLE);
			full_username_line.setVisibility(View.GONE);
		}
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	/**
	 * 同步数据
	 * @param v
	 */
	public void syncData(View v){
		//判断是否是否在线和离线
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//判断是否正在使用WIFI网络  
		//网络状态判断
		if(mobNetInfo.isConnected() || wifiNetInfo.isConnected()){//在线
			//设置同步的时间
			String sync_time = deviceInfo.getString("sync_time", "");
			if(sync_time.equals("")){
				sync_time = "无";
			}
			// 自定义弹出提示框
			CustomDoubleDialog.Builder builder = new CustomDoubleDialog.Builder(this);
			builder.setMessage("上一次同步时间为："+sync_time);
			builder.setTitle("同步");
			builder.setPositiveButton("同步",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog,int which) {
							
							JSONObject jsonObject;
							equnum = deviceInfo.getString("equnum", "");
							imei = deviceInfo.getString("imei", "");
							//根据设备参数获取已分配的用户
							String pathUrl = URLs.GETEQUUSERINFO_VALIDATE_HTTP + "/" + equnum + "/" + imei;
							String webContent = HttpRequest.doGet(pathUrl);
							System.out.println("根据设备参数获取已分配的用户：" + webContent);
							if(webContent.equals("failure")){
								NewToast.makeText(context, R.drawable.validate, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
								return;
							}
							
							//获取巡检参数
							String pathUrl2 = URLs.GETPARAM_VALIDATE_HTTP;
							String webContent2 = HttpRequest.doGet(pathUrl2);
							System.out.println("获取巡检参数：" + webContent2);
							try {
								jsonObject = new JSONObject(webContent);
								String msg = jsonObject.getString("msg");  
								String result = jsonObject.getString("result");  
								String data = jsonObject.getString("data");  
								if(msg.equalsIgnoreCase("SUCCESS")){
									jsonObject = new JSONObject(data);
						            JSONArray jsonArray = (JSONArray) jsonObject.get("user");
						            String isused = "";
						            for (int i = 0; i < jsonArray.length(); ++i) {
						                JSONObject o = (JSONObject) jsonArray.get(i);
						                isused = o.getString("isused");
						            }
						            if(isused.equals("未使用")){
						            	NewToast.makeText(context, R.drawable.validate, "该设备状态为："+isused+"，请等待管理员分配！", Toast.LENGTH_LONG).show();
						            }else{//设备可用，同步数据
						            	CustomProgressDialogA.showProgressDialog(context, "正在同步，请稍后...");
										new Thread() {
											@Override
											public void run() {
												try {
													Thread.sleep(3000);
													CustomProgressDialogA.hideProgressDialog();
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													CustomProgressDialogA.hideProgressDialog();
												}
											}
										}.start();
										Thread.interrupted();// 中断线程
										
						            	dbAdepter.deleteUserData();//新增之前，删除用户表数据
										System.out.println("正在用户表的旧数据......");
										SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
										Editor editor = deviceInfo.edit();
										editor.putString("sync_time", df.format(new Date()));
										editor.commit();
										try {
											jsonObject = new JSONObject(data);
								            jsonArray = (JSONArray) jsonObject.get("user");
								            //取数据
							                data_list = new ArrayList<String>();
								            String name = "";
								            String password = "";
								            for (int i = 0; i < jsonArray.length(); ++i) {
								                JSONObject o = (JSONObject) jsonArray.get(i);
								                name = o.getString("name");
								                password = o.getString("password");
								                
								                //存储到本地数据库
								                User user = new User();
								                user.name = name;
								                user.password = password;
												dbAdepter.insertUser(user);
												System.out.println("正在添加用户表数据......");
												//用户信息已经同步到本地
												Editor et = sp.edit();
												et.putString("LOCAL_SYNC", "TRUE");
												et.commit();
												
												data_list.add(name);
								                //适配器
								                arr_adapter= new ArrayAdapter<String>(context, R.layout.simple_spinner_item, data_list);
								                //设置样式
								                arr_adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
								                //加载适配器
								                userName.setAdapter(arr_adapter);
								            }
								        } catch (JSONException e) {
								            e.printStackTrace();
								        }
										
										//获取巡检参数
										jsonObject = new JSONObject(webContent2);
										String msg2 = jsonObject.getString("msg");  
										String result2 = jsonObject.getString("result");  
										String data2 = jsonObject.getString("data");  
										String paramName = "";
										String paramValue = "";
										String chname = "";
										String remark = "";
										
										if(msg2.equalsIgnoreCase("SUCCESS")){
											dbAdepter.deleteParamData();
											System.out.println("正在删除本地巡检参数表的旧数据......");
											try {
												jsonObject = new JSONObject(data2);
									            jsonArray = (JSONArray) jsonObject.get("param");
									            for (int i = 0; i < jsonArray.length(); ++i) {
									                JSONObject o = (JSONObject) jsonArray.get(i);
									                paramName = o.getString("paramName");
									                paramValue = o.getString("paramValue");
									                chname = o.getString("chname");
									                remark = o.getString("remark");
									                
									                if(paramName.equals("GpsUpload")){
									                	Editor et = sp.edit();
									                	et.putString("GpsUpload", paramValue);
									                	et.commit();
									                }
									                
									                //存储 巡检参数 到本地数据库
									                Param param = new Param();
									                param.paramName = paramName;
									                param.paramValue = paramValue;
									                param.chName = chname;
									                param.remark = remark;
													dbAdepter.insertParam(param);
													System.out.println("正在存储本地巡检参数表数据......");
									            }
									            
											}catch (JSONException e) {
									            e.printStackTrace();
									        }
										}else if(msg2.equalsIgnoreCase("FAIL")){
											NewToast.makeText(context, R.drawable.validate, R.string.sync_fail, Toast.LENGTH_SHORT).show();
										}
										
										//存储缺陷报告
										String patrolBugPath = URLs.GETBUGTYPE_VALIDATE_HTTP;
										String patrolBugContent = HttpRequest.doGet(patrolBugPath);
										System.out.println("patrolBugPath返回值："+patrolBugContent);
										try {
											jsonObject = new JSONObject(patrolBugContent);
											String msgBug = jsonObject.getString("msg");  
											String resultBug = jsonObject.getString("result");  
											String dataBug = jsonObject.getString("data");  
											String bugId = "";
											String bugName = "";
											if(resultBug.equals("true")){
												if(msgBug.equalsIgnoreCase("SUCCESS")){
													dbAdepter.deleteBugPatrol();//删除缺陷报告参数数据
													System.out.println("删除缺陷报告参数数据......");
													jsonObject = new JSONObject(dataBug);
													JSONArray jsonArrayBug = (JSONArray) jsonObject.get("bugType");
													for (int i = 0; i < jsonArrayBug.length(); ++i) {
										                JSONObject o = (JSONObject) jsonArrayBug.get(i);
										                bugId = o.getString("id");
										                bugName = o.getString("name");
										                //存储到本地数据库
										                BugPatrol bean = new BugPatrol();
										                bean.id = Integer.valueOf(bugId);
										                bean.name = bugName;
														dbAdepter.insertBugPatrol(bean);
														System.out.println("添加缺陷报告参数数据["+Integer.valueOf(i+1)+"]......");
													}
												}
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
										
						            }
						            
								}else if(msg.equalsIgnoreCase("FAIL")){
									NewToast.makeText(context, R.drawable.validate, R.string.sync_fail, Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							dialog.dismiss();
						}
					});
					builder.setNegativeButton("取消",new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}else{//无网络-离线
			AppToast.makeText(context, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
		}
		//
	}
	
	/**
	 * 登录
	 * @param view
	 */
	public void login(View view){
		if (userName.getSelectedItem().toString().trim().equals("")) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			findViewById(R.id.user_name).startAnimation(shake);
			NewToast.makeText(context, R.drawable.validate, R.string.username_hint, Toast.LENGTH_SHORT).show(); 
			return;
		} else if (passWord.getText().toString().trim().equals("")) {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			findViewById(R.id.user_pwd).startAnimation(shake);
			NewToast.makeText(context, R.drawable.validate, R.string.password_hint, Toast.LENGTH_SHORT).show(); 
			return;
		}else{
			userNameValue = userName.getSelectedItem().toString();
			passWordValue = passWord.getText().toString();
			
			//判断是否是否在线和离线
			ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//判断是否正在使用GPRS网络
			NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//判断是否正在使用WIFI网络  
			//网络状态判断
			if(mobNetInfo.isConnected() || wifiNetInfo.isConnected()){//在线
				String loginPath = URLs.LOGIN_VALIDATE_HTTP+"/"+userNameValue+"/"+passWordValue;
				String webContent = HttpRequest.doGet(loginPath);
				System.out.println("loginPath接收得返回值："+webContent);
				if(webContent.equals("failure")){
					NewToast.makeText(context, R.drawable.validate, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
					return;
				}
				//Toast.makeText(context, "接收得返回值："+webContent, Toast.LENGTH_SHORT).show();
				//接收得返回值：{"data":{"userId":104300},"result":true,"msg":"登录成功"}
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(webContent);
					String msg = jsonObject.getString("msg");  
					String result = jsonObject.getString("result");  
					String data = jsonObject.getString("data");  
					
					String subline_id = "";
					String user_id = "";
					if(result.equals("true")){
						if(msg.equalsIgnoreCase("SUCCESS")){
							NewToast.makeText(context, R.drawable.sure_icon, "用户验证成功", Toast.LENGTH_SHORT).show();
							loginButton.setText("登录中...");
							CustomProgressDialogA.showProgressDialog(context, "正在奋力加载数据...");
							
							jsonObject = new JSONObject(data);
							JSONArray jsonArray = (JSONArray) jsonObject.get("user");
							for (int i = 0; i < jsonArray.length(); ++i) {
				                JSONObject o = (JSONObject) jsonArray.get(i);
				                subline_id = o.getString("subline_id");  
				                user_id = o.getString("id");
				                Editor editor = sp.edit();
								editor.putString("subline_id", subline_id);
								editor.putString("user_id", user_id);
								editor.commit();
							}
							new SyncTaskPointAsyncTask().execute();//同步
							// 登录成功和记住密码框为选中状态才保存用户信息
							if (rememberPwd.isChecked()) {
								// 记住用户名、密码
								Editor editor = sp.edit();
								editor.putString("USER_NAME", userNameValue);
								editor.putString("PASSWORD", passWordValue);
								editor.commit();
							}
							new Thread() {
								@Override
								public void run() {
									try {
										Thread.sleep(5000);
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										CustomProgressDialogA.hideProgressDialog();
										Intent intent = new Intent();
										intent.setClass(context, MainActivity.class);
										startActivity(intent);
										finish();//销毁登录Activity
									}
								}
							}.start();
							Thread.interrupted();// 中断线程
							
						}else if(msg.equalsIgnoreCase("FAIL")){
							Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
							findViewById(R.id.full_password_line).startAnimation(shake);
							findViewById(R.id.user_pwd).startAnimation(shake);
							NewToast.makeText(context, R.drawable.validate, R.string.login_failure, Toast.LENGTH_SHORT).show();
						}
					}else{
						Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
						findViewById(R.id.full_password_line).startAnimation(shake);
						findViewById(R.id.user_pwd).startAnimation(shake);
						NewToast.makeText(context, R.drawable.validate, R.string.login_failure, Toast.LENGTH_SHORT).show();
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//...在线
			}else{//无网络-离线
				User[] uList = dbAdepter.queryUserByUserNameAndPsd(userNameValue, passWordValue);
				if(uList!=null){
					// 登录成功和记住密码框为选中状态才保存用户信息
					if (rememberPwd.isChecked()) {
						// 记住用户名、密码
						Editor editor = sp.edit();
						editor.putString("USER_NAME", userNameValue);
						editor.putString("PASSWORD", passWordValue);
						editor.commit();
					}
					loginButton.setText("登录中...");
					CustomProgressDialogA.showProgressDialog(context, "离线登录，正在努力加载...");
					Intent intent = new Intent();
					intent.setClass(context, MainActivity.class);
					startActivity(intent);
					finish();//销毁登录Activity
				}else{
					Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
					findViewById(R.id.full_password_line).startAnimation(shake);
					findViewById(R.id.user_pwd).startAnimation(shake);
					NewToast.makeText(context, R.drawable.validate, R.string.login_failure, Toast.LENGTH_SHORT).show();
				}
			}
			//
		}
	}
	
	/**
	 * 同步数据
	 */
	public void syncTaskPointData(){
		//判断是否是否在线和离线
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//判断是否正在使用WIFI网络  
		//网络状态判断
		if(mobNetInfo.isConnected() || wifiNetInfo.isConnected()){//在线
			//登录之前同步数据标识
			Editor et = sp.edit();
			et.putString("LOCAL_SYNC", "TRUE");
			et.commit();
			
			//存储管线桩点数据
			JSONObject jsonObject;
			String subline_id = sp.getString("subline_id", "");
			String pilePointPath = URLs.PILEPOINT_HTTP+"/"+subline_id;
			String pilePointContent = HttpRequest.doGet(pilePointPath);
			System.out.println("pilePointPath返回值："+pilePointContent);
			if(pilePointContent.equals("failure")){
				NewToast.makeText(context, R.drawable.validate, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				jsonObject = new JSONObject(pilePointContent);
				String msg0 = jsonObject.getString("msg");  
				String result0 = jsonObject.getString("result");  
				String data0 = jsonObject.getString("data");  
				String bid = "";
				String serial_NO = "";
				String stake_NO = "";
				String x = "";
				String y = "";
				if(result0.equals("true")){
					if(msg0.equalsIgnoreCase("SUCCESS")){
						dbAdepter.deletePilePoint(subline_id);//删除分配给当前人的管线数据
						System.out.println("删除管线桩点数据......");
						jsonObject = new JSONObject(data0);
						JSONArray jsonArray0 = (JSONArray) jsonObject.get("taskPoint");
						for (int i = 0; i < jsonArray0.length(); ++i) {
			                JSONObject o = (JSONObject) jsonArray0.get(i);
			                bid = o.getString("bid");  
			                serial_NO = o.getString("serial_NO");
			                stake_NO = o.getString("stake_NO");
			                x = o.getString("x");
			                y = o.getString("y");
			                //存储到本地数据库
			                PipemodeCenterLineStake bean = new PipemodeCenterLineStake();
			                bean.subline_id = Integer.valueOf(subline_id);
			                bean.bid = Integer.valueOf(bid);
			                bean.serial_NO = Integer.valueOf(serial_NO);
			                bean.stake_NO = stake_NO;
			                bean.x = Double.valueOf(x);
			                bean.y = Double.valueOf(y);
							dbAdepter.insertPilePoint(bean);
							System.out.println("添加管线点数据["+Integer.valueOf(i+1)+"]......");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//存储任务作业点数据
			String taskPointPath = URLs.TASKPOINT_HTTP+"/"+subline_id;
			String taskPointContent = HttpRequest.doGet(taskPointPath);
			System.out.println("taskPointPath返回值："+taskPointContent);
			try {
				jsonObject = new JSONObject(taskPointContent);
				String msg2 = jsonObject.getString("msg");  
				String result2 = jsonObject.getString("result");  
				String data2 = jsonObject.getString("data");  
				String taskId = "";
				String sublineId = "";
				String longitude = "";
				String latitude = "";
				if(result2.equals("true")){
					if(msg2.equalsIgnoreCase("SUCCESS")){
						dbAdepter.deleteTaskPoint(subline_id);//删除分配给当前人的管线数据
						System.out.println("删除任务作业点数据......");
						jsonObject = new JSONObject(data2);
						JSONArray jsonArray2 = (JSONArray) jsonObject.get("taskPoint");
						for (int i = 0; i < jsonArray2.length(); ++i) {
			                JSONObject o = (JSONObject) jsonArray2.get(i);
			                taskId = o.getString("id");  
			                sublineId = o.getString("subline_id");
			                longitude = o.getString("longitude");
			                latitude = o.getString("latitude");
			                //存储到本地数据库
			                PatrolTaskPoint bean = new PatrolTaskPoint();
			                bean.id = Integer.valueOf(taskId);
			                bean.subline_id = Integer.valueOf(sublineId);
			                bean.longitude = longitude;
			                bean.latitude = latitude;
							dbAdepter.insertTaskPoint(bean);
							
							Editor editor = deviceInfo.edit();
							editor.putString("taskPointNum", String.valueOf(i+1));
							editor.commit();
							
							System.out.println("添加任务作业点数据["+Integer.valueOf(i+1)+"]......");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//巡检任务管线开始点和结束点获取
			String sublinePath = URLs.SUBLINE_HTTP+"/"+subline_id;
			String sublineContent = HttpRequest.doGet(sublinePath);
			System.out.println("sublinePath返回值："+sublineContent);
			try {
				jsonObject = new JSONObject(sublineContent);
				String msg1 = jsonObject.getString("msg");  
				String result1 = jsonObject.getString("result");  
				String data1 = jsonObject.getString("data");  
				String name = "";
				String end  = "";
				String line_id = "";
				String begin = "";
				if(result1.equals("true")){
					if(msg1.equalsIgnoreCase("SUCCESS")){
						dbAdepter.deleteSublineData();//删除当前用户的管线表数据
						System.out.println("删除旧管线数据......");
						jsonObject = new JSONObject(data1);
						JSONArray jsonArray1 = (JSONArray) jsonObject.get("subline");
						for (int i = 0; i < jsonArray1.length(); ++i) {
			                JSONObject o = (JSONObject) jsonArray1.get(i);
			                name = o.getString("name");  
			                end = o.getString("end");
			                line_id = o.getString("line_id");
			                begin = o.getString("begin");
			                
			                //存储到本地数据库
			                Subline bean = new Subline();
			                bean.name = name;
			                bean.line_id = line_id;
			                bean.begin = begin;
			                bean.end = end;
							dbAdepter.insertSubline(bean);
							System.out.println("添加新管线数据......");
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//
		}else{//无网络
			AppToast.makeText(context, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
		}
	}
	
	// 异步处理
	public class SyncTaskPointAsyncTask extends AsyncTask<String, Integer, String> {
		/*
		 * 第一个参数为doInBackground传入的参数，
		 * 第二个参数为onProgressUpdate传入参数,由doInBackground内调用publishProgress
		 * (Interger)传值; 第三个为doInBackground返回值和onPostExecute传入的参数。
		 */
		@Override
		protected void onPreExecute() {
			// 该方法将在执行实际的后台操作前被UI thread调用。可以在该方法中做一些准备工作，如在界面上转菊花提示
			System.out.println("登录同步数据......");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// 将在onPreExecute 方法执行后马上执行，该方法运行在后台线程中。
			// 这里将主要负责执行那些很耗时的后台计算工作。
			System.out.println("doInBackground---->");
			syncTaskPointData();//同步数据方法
			return "doInBackground";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			System.out.println("更新进度" + values[0]);
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
	
	/**
	 * 设备注册
	 * @param v
	 */
	public void register(View view){
		//判断是否是否在线和离线
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);//判断是否正在使用GPRS网络
		NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);//判断是否正在使用WIFI网络  
		//网络状态判断
		if(mobNetInfo.isConnected() || wifiNetInfo.isConnected()){//在线
			equnum = deviceInfo.getString("equnum", "");
			sn = deviceInfo.getString("sn", "");
			imei = deviceInfo.getString("imei", "");
			sim = deviceInfo.getString("sim", "");
			os_version = deviceInfo.getString("os_version", "");
			
			// 自定义弹出提示框
			CustomSingleDialog.Builder builder = new CustomSingleDialog.Builder(context);
			builder.setMessage("设备型号："+equnum+"\n\n"+"序列号："+sn+"\n\n"+"IMEI："+imei+"\n\n"+"系统版本："+os_version+"\n");
			builder.setTitle("设备注册");
			builder.setPositiveButton("注册",
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, int which) {
							String registerPath = URLs.REGISTER_VALIDATE_HTTP + "/" + equnum + "/" + sn + "/" + imei + "/" + sim + "/" + os_version;
							String webContent = HttpRequest.doGet(registerPath);
							System.out.println("接收得返回值：" + webContent);
							if(webContent.equals("failure")){
								NewToast.makeText(context, R.drawable.validate, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
								return;
							}else{
								Editor editor = deviceInfo.edit();
								editor.putString("register", "yes");
								editor.commit();
								sync_login_linearLayout.setVisibility(View.VISIBLE);
								register_linearLayout.setVisibility(View.GONE);
								dialog.dismiss();
							}
							CustomProgressDialogA.showProgressDialog(context, "正在提交注册信息...");
							//加载进度条
							//CustomProgressDialog1.showRoundProcessDialog(context, R.layout.view_progress_dialog_1);
							new Thread() {
								@Override
								public void run() {
									try {
										Thread.sleep(500);
										CustomProgressDialogA.hideProgressDialog();
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										CustomProgressDialogA.hideProgressDialog();
									}
								}
							}.start();
							Thread.interrupted();// 中断线程 		
						}
					});
			builder.create().show();
		}else{//无网络
			AppToast.makeText(context, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
		}
		
	}
	
	/**
	 * 监听返回-是否退出程序
	 */
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				NewToast.makeText(context, R.drawable.sure_icon, "再按一次  退出系统", Toast.LENGTH_SHORT).show(); 
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}
