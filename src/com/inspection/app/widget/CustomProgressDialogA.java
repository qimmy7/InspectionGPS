package com.inspection.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.inspection.app.R;

/**
 * 加载进度条
 * 
 * @author liuyx
 * @created 2014/6/6
 */
public class CustomProgressDialogA extends Dialog {
	
	Context context;
	private static CustomProgressDialogA customProgressDialog = null;

	public CustomProgressDialogA(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialogA(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public static CustomProgressDialogA createDialog(Context context) {
		customProgressDialog = new CustomProgressDialogA(context,R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.view_progress_dialog_a);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (customProgressDialog == null) {
			return;
		}
		/**
		ProgressBar imageView = (ProgressBar) customProgressDialog
				.findViewById(R.id.loading_img_a);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
		**/
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public CustomProgressDialogA setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public CustomProgressDialogA setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.tv_loadingmsg_a);
		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		return customProgressDialog;
	}

	// 显示滚动进度
	public static Dialog showProgressDialog(Context context, String strMessage) {
		CustomProgressDialogA customProgressDialog = null;
		if (customProgressDialog == null) {
			customProgressDialog = createDialog(context);
			customProgressDialog.setMessage(strMessage);
		}
		customProgressDialog.show();
		return customProgressDialog;
	}

	// 隐藏滚动进度
	public static void hideProgressDialog() {
		if (customProgressDialog != null) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}

	@Override
	public void onBackPressed() {
		if (customProgressDialog != null) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}

}
