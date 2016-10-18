package com.inspection.app.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * 加载进度条
 * 
 * @author liuyx
 * @created 2014/6/6
 */
public class CustomProgressDialog1 extends Dialog {

	private static Dialog customProgressDialog = null;

	public CustomProgressDialog1(Context context) {
		super(context);
	}

	public CustomProgressDialog1(Context context, int theme) {
		super(context, theme);
	}

	// 显示滚动进度
	public static void showRoundProcessDialog(Context mContext, int layout) {
		OnKeyListener keyListener = new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				return false;
			}
		};
		customProgressDialog = new AlertDialog.Builder(mContext).create();
		customProgressDialog.setOnKeyListener(keyListener);
		customProgressDialog.show();
		// 注意此处要放在show之后 否则会报异常
		customProgressDialog.setContentView(layout);
	}

	// 隐藏滚动进度
	public static void hideProgressDialog() {
		if (customProgressDialog != null) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}

}
