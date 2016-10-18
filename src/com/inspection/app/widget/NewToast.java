package com.inspection.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inspection.app.R;

public class NewToast extends Toast {

	public NewToast(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, int resId, CharSequence text,
			int duration) {
		Toast result = new Toast(context);

		// 获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 由layout文件创建一个View对象
		View layout = inflater.inflate(R.layout.newtoast, null);
		
		// 实例化ImageView和TextView对象
		ImageView imageView = (ImageView) layout.findViewById(R.id.toast_image);
		TextView textView = (TextView) layout.findViewById(R.id.toast_text);

		imageView.setImageResource(resId);
		textView.setText(text);

		result.setView(layout);
		result.setGravity(Gravity.BOTTOM, 0, 150);
		result.setDuration(duration);
		
		return result;
	}
	
	@SuppressLint("NewApi")
	public static Toast makeText(Context context, int resId, int text,
			int duration) {
		Toast result = new Toast(context);

		// 获取LayoutInflater对象
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 由layout文件创建一个View对象
		View layout = inflater.inflate(R.layout.newtoast, null);
		
		// 实例化ImageView和TextView对象
		ImageView imageView = (ImageView) layout.findViewById(R.id.toast_image);
		TextView textView = (TextView) layout.findViewById(R.id.toast_text);

		imageView.setImageResource(resId);
		textView.setText(text);

		result.setView(layout);
		result.setGravity(Gravity.BOTTOM, 0, 150);
		result.setDuration(duration);

		return result;
	}

}