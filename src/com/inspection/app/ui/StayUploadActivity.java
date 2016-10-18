package com.inspection.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.inspection.app.R;
import com.inspection.app.adapter.StayUploadAdapter;
import com.inspection.app.adapter.SyncDbAdapter;
import com.inspection.app.bean.PatrolPersonPath;
import com.inspection.app.common.http.HttpRequest;
import com.inspection.app.common.http.URLs;
import com.inspection.app.widget.CornerListView;
import com.inspection.app.widget.CustomProgressDialogA;

/**
 * 待上传列表
 * 
 * @author liuyx
 * @created 2014/11/24
 */
public class StayUploadActivity extends Activity {

	private Context context;
	public SyncDbAdapter dbAdepter;
	
	private LinearLayout uploadlistview_linearLayout;
	// listview
	private CornerListView listView;
    private ArrayList<String> list1;
    private ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
    
    private StayUploadAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stayupload_panel);
		context = this;
		dbAdepter = new SyncDbAdapter(context);
		dbAdepter.open();
		
		uploadlistview_linearLayout = (LinearLayout)findViewById(R.id.uploadlistview_linearLayout);
		View layout = LayoutInflater.from(context).inflate(R.layout.stayupload_frame, null);
		AnimationSet set = new AnimationSet(true);
		Animation animation = AnimationUtils.loadAnimation(this,R.anim.login_in);
		//animation.setDuration(500);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
		uploadlistview_linearLayout.setLayoutAnimation(controller);
		uploadlistview_linearLayout.addView(layout);
		
		listView = (CornerListView)layout.findViewById(R.id.stayupload_listview);
		listView.setCacheColorHint(0);//滑动列表时底色透明
		cleanlist();//清除处理
		
		PatrolPersonPath[] pList = dbAdepter.queryPatrolPersonPath();
		if(pList!=null){
			int count = pList.length;
			for (int i = 0; i < pList.length; i++) {
				list1 = new ArrayList<String>();
				list1.add(pList[i].PATH_POINT_TIME);
				list1.add(pList[i].HEIGHT);
				lists.add(list1);
			}
		}
		
		adapter = new StayUploadAdapter(context, lists);
		listView.setAdapter(adapter);
	}
	
	//清除处理
    private void cleanlist(){
    	int size=lists.size();
		if(size>0){
			lists.removeAll(lists);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		}
    }
	
	
	/**
	 * 返回
	 * @param v
	 */
	public void goback(View v){
		this.finish();
	}
	

}
