package com.inspection.app.ui;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.app.R;
import com.inspection.app.adapter.SyncDbAdapter;
import com.inspection.app.bean.Param;
import com.inspection.app.bean.PatrolPersonPath;
import com.inspection.app.common.AppContext;
import com.inspection.app.widget.AppToast;
import com.inspection.app.widget.CustomDoubleDialog;
import com.inspection.app.widget.CustomProgressDialogA;

/**
 * 参数设置
 * @author liuyx
 * @created by 2014/10/20
 */
public class SettingsActivity extends Activity {
	
	private Context context;
	private AppContext appContext;// 全局Context
	
	private TextView tv_gps_uploadtime_title,tv_uptime_title,tv_overspeed_title,tv_startrange_title,tv_endrange_title,tv_updistance_title,tv_pipedeviation_title,tv_outoflinetime_title;
	private TextView tv_gps_uploadtime_value,tv_uptime_value,tv_overspeed_value,tv_startrange_value,tv_endrange_value,tv_updistance_value,tv_pipedeviation_value,tv_outoflinetime_value;
	private TextView tv_clearthecache_value,tv_update_value;
	public SyncDbAdapter dbAdepter;
	
	private SharedPreferences sp;// 声明一个SharedPreferences,存储用户信息
	private SharedPreferences deviceInfo;// 存储设备信息
	
	private RelativeLayout settings_topbar;
	private LinearLayout settings_info_1,settings_info_2,settings_info_3,settings_info_4,settings_info_5,settings_info_6,settings_info_7,settings_info_8,settings_info_9,settings_info_10,settings_info_11;
	private View settings_line_1,settings_line_2,settings_line_3,settings_line_4,settings_line_5,settings_line_6,settings_line_7,settings_line_8,settings_line_9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		context = this;
		dbAdepter = new SyncDbAdapter(context);
        dbAdepter.open();
        
        // 获得实例对象
 		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
 		deviceInfo = this.getSharedPreferences("device", Context.MODE_WORLD_READABLE);
 		
 		settings_topbar = (RelativeLayout)findViewById(R.id.settings_topbar);
 		settings_info_1 = (LinearLayout)findViewById(R.id.settings_info_1);
 		settings_info_2 = (LinearLayout)findViewById(R.id.settings_info_2);
 		settings_info_3 = (LinearLayout)findViewById(R.id.settings_info_3);
 		settings_info_4 = (LinearLayout)findViewById(R.id.settings_info_4);
 		settings_info_5 = (LinearLayout)findViewById(R.id.settings_info_5);
 		settings_info_6 = (LinearLayout)findViewById(R.id.settings_info_6);
 		settings_info_7 = (LinearLayout)findViewById(R.id.settings_info_7);
 		settings_info_8 = (LinearLayout)findViewById(R.id.settings_info_8);
 		settings_info_9 = (LinearLayout)findViewById(R.id.settings_info_9);
 		settings_info_10 = (LinearLayout)findViewById(R.id.settings_info_10);
 		settings_info_11 = (LinearLayout)findViewById(R.id.settings_info_11);
 		
 		settings_line_1 = (View)findViewById(R.id.settings_line_1);
 		settings_line_2 = (View)findViewById(R.id.settings_line_2);
 		settings_line_3 = (View)findViewById(R.id.settings_line_3);
 		settings_line_4 = (View)findViewById(R.id.settings_line_4);
 		settings_line_5 = (View)findViewById(R.id.settings_line_5);
 		settings_line_6 = (View)findViewById(R.id.settings_line_6);
 		settings_line_7 = (View)findViewById(R.id.settings_line_7);
 		settings_line_8 = (View)findViewById(R.id.settings_line_8);
 		settings_line_9 = (View)findViewById(R.id.settings_line_9);
 		
 		TranslateAnimation animTop = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animTop.setDuration(500);
 		settings_topbar.setAnimation(animTop);
 		TranslateAnimation animInfo1 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo1.setDuration(600);
 		settings_info_1.setAnimation(animInfo1);
 		TranslateAnimation animInfo2 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo2.setDuration(700);
 		settings_info_2.setAnimation(animInfo2);
 		TranslateAnimation animInfo3 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo3.setDuration(800);
 		settings_info_3.setAnimation(animInfo3);
 		TranslateAnimation animInfo4 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo4.setDuration(900);
 		settings_info_4.setAnimation(animInfo4);
 		TranslateAnimation animInfo5 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo5.setDuration(1000);
 		settings_info_5.setAnimation(animInfo5);
 		TranslateAnimation animInfo6 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo6.setDuration(1100);
 		settings_info_6.setAnimation(animInfo6);
 		TranslateAnimation animInfo7 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo7.setDuration(1200);
 		settings_info_7.setAnimation(animInfo7);
 		TranslateAnimation animInfo8 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo8.setDuration(1300);
 		settings_info_8.setAnimation(animInfo8);
 		TranslateAnimation animInfo9 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo9.setDuration(1400);
 		settings_info_9.setAnimation(animInfo9);
 		TranslateAnimation animInfo10 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo10.setDuration(1500);
 		settings_info_10.setAnimation(animInfo10);
 		TranslateAnimation animInfo11 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animInfo11.setDuration(1600);
 		settings_info_11.setAnimation(animInfo11);
 		
 		TranslateAnimation animLine1 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine1.setDuration(650);
 		settings_line_1.setAnimation(animLine1);
 		TranslateAnimation animLine2 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine2.setDuration(750);
 		settings_line_2.setAnimation(animLine2);
 		TranslateAnimation animLine3 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine3.setDuration(850);
 		settings_line_3.setAnimation(animLine3);
 		TranslateAnimation animLine4 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine4.setDuration(950);
 		settings_line_4.setAnimation(animLine4);
 		TranslateAnimation animLine5 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine5.setDuration(1050);
 		settings_line_5.setAnimation(animLine5);
 		TranslateAnimation animLine6 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine6.setDuration(1150);
 		settings_line_6.setAnimation(animLine6);
 		TranslateAnimation animLine7 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine7.setDuration(1250);
 		settings_line_7.setAnimation(animLine7);
 		TranslateAnimation animLine8 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine8.setDuration(1350);
 		settings_line_8.setAnimation(animLine8);
 		TranslateAnimation animLine9 = new TranslateAnimation(0, 0, 3000, 0);//动画
 		animLine9.setDuration(1450);
 		settings_line_9.setAnimation(animLine9);
        
		tv_gps_uploadtime_title = (TextView)findViewById(R.id.tv_gps_uploadtime_title);
		tv_uptime_title = (TextView)findViewById(R.id.tv_uptime_title);
		tv_overspeed_title = (TextView)findViewById(R.id.tv_overspeed_title);
		tv_startrange_title = (TextView)findViewById(R.id.tv_startrange_title);
		tv_endrange_title = (TextView)findViewById(R.id.tv_endrange_title);
		tv_updistance_title = (TextView)findViewById(R.id.tv_updistance_title);
		tv_pipedeviation_title = (TextView)findViewById(R.id.tv_pipedeviation_title);
		tv_outoflinetime_title = (TextView)findViewById(R.id.tv_outoflinetime_title);
		
		tv_gps_uploadtime_value = (TextView)findViewById(R.id.tv_gps_uploadtime_value);
		tv_uptime_value = (TextView)findViewById(R.id.tv_uptime_value);
		tv_overspeed_value = (TextView)findViewById(R.id.tv_overspeed_value);
		tv_startrange_value = (TextView)findViewById(R.id.tv_startrange_value);
		tv_endrange_value = (TextView)findViewById(R.id.tv_endrange_value);
		tv_updistance_value = (TextView)findViewById(R.id.tv_updistance_value);
		tv_pipedeviation_value = (TextView)findViewById(R.id.tv_pipedeviation_value);
		tv_outoflinetime_value = (TextView)findViewById(R.id.tv_outoflinetime_value);
		
		tv_clearthecache_value = (TextView)findViewById(R.id.tv_clearthecache_value);
		tv_update_value = (TextView)findViewById(R.id.tv_update_value);
		
		//计算照片文件大小
		String personId = sp.getString("user_id", "");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		//系统上传时间
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = formatter.format(currentTime);
		String filePath = Environment.getExternalStorageDirectory()+"/GPS巡检/"+personId+"/";
		Double photoSize = getDirSize(new File(filePath));
		PatrolPersonPath[] pList = dbAdepter.querySuccessPatrolPersonPathByPersonId(personId);
		Double taskSize = 0.0;
		Double size = 0.0;
		if(pList!=null){
			taskSize = 0.1;
		}
		if(photoSize>0){
			photoSize = photoSize+0.1;
		}else{
			photoSize = 0.0;
		}
		size = taskSize+photoSize;
		tv_clearthecache_value.setText(size+"M");
		tv_update_value.setText("v1.0");
		
		getParamInfo();
	}
	
	/**
	 * 计算文件大小
	 * @param file
	 * @return
	 */
	public static double getDirSize(File file) {     
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                double size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                DecimalFormat df = new DecimalFormat("#.0");//保留一位小数
                size = Double.valueOf(df.format(size));
                return size;     
            } else {//如果是文件则直接返回其大小,以“兆”为单位   
                double size = (double) file.length() / 1024 / 1024;        
                DecimalFormat df = new DecimalFormat("#.0");//保留一位小数
                size = Double.valueOf(df.format(size));
                return size;     
            }     
        } else {     
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");     
            return 0.0;     
        }     
    }  
	
	/**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
	/** * 清除自定义路径下的文件，使用需小心，请不要误删  * **/
    public static void cleanCustomCache(String filePath) {
    	deleteDir(new File(filePath));
    }
	
	/**
	 * 清除缓存
	 * @param view
	 */
	public void cleanCache(View view){
		String personId = sp.getString("user_id", "");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		//系统上传时间
		Date currentTime = new Date();
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String dateString = formatter.format(currentTime);
		String filePath = Environment.getExternalStorageDirectory()+"/GPS巡检/"+personId+"/";
		//照片文件删除
		cleanCustomCache(filePath);
		//删除巡检表已上传的数据
		Long count1 = dbAdepter.deletePatrolPersonPathByPersonId(personId);
		//清除已上传的照片表数据
		Long count2 = dbAdepter.deletePatrolBugPhoto(personId);
		Long count = count1+count2;
		if(count==0){
			AppToast.makeText(context, "暂无缓存，请稍候再试！", Toast.LENGTH_SHORT).show();
			return;
		}
		// 自定义弹出提示框
		CustomProgressDialogA.showProgressDialog(context, "正在清除缓存...");
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					CustomProgressDialogA.hideProgressDialog();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					//
				}
			}
		}.start();
		Thread.interrupted();// 中断线程
		tv_clearthecache_value.setText("0.0M");
	}
	
	/**
	 * 版本更新
	 * @param view
	 */
	public void update(View view){
		// 自定义弹出提示框
		CustomProgressDialogA.showProgressDialog(context, "正在检测新版本...");
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					CustomProgressDialogA.hideProgressDialog();
					tv_update_value.setText("已是最新版本(v1.0)");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					//
				}
			}
		}.start();
		Thread.interrupted();// 中断线程
	}
	
	/**
	 * 获取参数
	 */
	public void getParamInfo(){
		Param[] pList = dbAdepter.queryParam();
		if(pList!=null){
			for (int i = 0; i < pList.length; i++) {
				if(pList[i].paramName.trim().equals("GpsUpload")){
					tv_gps_uploadtime_title.setText(pList[i].chName.toString());
					tv_gps_uploadtime_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("UPTime")){
					tv_uptime_title.setText(pList[i].chName.toString());
					tv_uptime_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("OverSpeed")){
					tv_overspeed_title.setText(pList[i].chName.toString());
					tv_overspeed_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("StartRange")){
					tv_startrange_title.setText(pList[i].chName.toString());
					tv_startrange_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("EndRange")){
					tv_endrange_title.setText(pList[i].chName.toString());
					tv_endrange_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("UPDistance")){
					tv_updistance_title.setText(pList[i].chName.toString());
					tv_updistance_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("PipeDeviation")){
					tv_pipedeviation_title.setText(pList[i].chName.toString());
					tv_pipedeviation_value.setText(pList[i].paramValue.toString());
				}else if(pList[i].paramName.trim().equals("OutOfLineTime")){
					tv_outoflinetime_title.setText(pList[i].chName.toString());
					tv_outoflinetime_value.setText(pList[i].paramValue.toString());
				}
			}
			
		}
		
	}

	/**
	 * 返回
	 * @param v
	 */
	public void goback(View v){
		finish();
	}
	
	/**
	 * 退出当前帐号
	 * @param v
	 */
	public void logout(View v){
		// 自定义弹出提示框-模仿IOS8弹出对话框
		CustomDoubleDialog.Builder builder = new CustomDoubleDialog.Builder(context);
		builder.setMessage("确定退出当前帐号?");
		builder.setTitle("退出");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((Activity) context).overridePendingTransition(
								R.anim.activity_up, R.anim.fade_out);
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
