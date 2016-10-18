package com.inspection.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inspection.app.R;

/**
 * 业务办理-待上传数据源适配器类
 * @author liuyx
 * @createtd 2014/11/25
 */
public class StayUploadAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private ArrayList<ArrayList<String>> lists;
	public List<ViewHolder> mHolderList = new ArrayList<ViewHolder>();//表头列list
	
	public StayUploadAdapter(Context context, ArrayList<ArrayList<String>> lists) {
		super();
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return lists.size();
	}

	public Object getItem(int id) {
		return null;
	}

	public long getItemId(int id) {
		return 0;
	}
	
	View view1;
	public View getView(int index, View view, ViewGroup parentView) {
		this.view1 = view;
		ViewHolder holder = null;
		if (view == null) {
			synchronized (StayUploadAdapter.this) {
				view = inflater.inflate(R.layout.stayupload_item, null);
				holder = new ViewHolder();
				
				//view.setBackgroundColor(Color.WHITE);
				holder.txt1 = (TextView) view.findViewById(R.id.stayupload_month);
				holder.txt1.setTextColor(Color.BLACK);
				
				view.setTag(holder);
				mHolderList.add(holder);
			}
		}else{
			holder = (ViewHolder) view.getTag();
		}
		
		/*if (index % 2 != 0) {
			view.setBackgroundColor(Color.argb(250, 255, 255, 255));
		} else {
			view.setBackgroundColor(Color.argb(250, 224, 243, 250));
		}*/
		
		if(lists.size()>0){
			ArrayList<String> list = lists.get(index);
			holder.txt1.setText(list.get(0));
		}
		
		return view;
	}
	

	class ViewHolder {
		TextView txt1;
		TextView txt2;
		TextView txt3;
		TextView txt4;
		TextView txt5;
	}
	

}
