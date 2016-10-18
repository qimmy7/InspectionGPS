package com.inspection.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.app.R;

public class AppToast extends Toast {

	public AppToast(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, CharSequence text,
			int duration) {
		Toast result = new Toast(context);

		// 获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 由layout文件创建一个View对象
		View layout = inflater.inflate(R.layout.apptoast, null);
		
		// 实例化ImageView和TextView对象
		TextView textView = (TextView) layout.findViewById(R.id.apptoast_text);
		textView.setText(text);

		result.setView(layout);
		result.setGravity(Gravity.BOTTOM, 0, 150);
		result.setDuration(duration);
		
		return result;
	}
	
	@SuppressLint("NewApi")
	public static Toast makeText(Context context, int text,
			int duration) {
		Toast result = new Toast(context);

		// 获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 由layout文件创建一个View对象
		View layout = inflater.inflate(R.layout.apptoast, null);
		
		// 实例化ImageView和TextView对象
		TextView textView = (TextView) layout.findViewById(R.id.apptoast_text);
		textView.setText(text);

		result.setView(layout);
		result.setGravity(Gravity.BOTTOM, 0, 150);
		result.setDuration(duration);

		return result;
	}

}