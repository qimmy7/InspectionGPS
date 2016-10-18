package com.inspection.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inspection.app.R;

/**
 * 弹出对话框－ 单个按钮
 * @author liuyx
 * @createtime 2014/7/18
 */

public class CustomSingleDialog extends Dialog {

	public CustomSingleDialog(Context context) {
		super(context);
	}

	public CustomSingleDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;
		private Button confirm_btn_single_dialog;
		private String title;
		private String message;
		private String confirmButtonText;
		private View contentView;
		private DialogInterface.OnClickListener confirmButtonClickListener;
		LinearLayout linearLayout;

		
		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * @param 提示内容-String
		 */
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * @param 提示内容-int
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * @param 提示标题-int
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * @param 提示标题-String
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * @param 确定-int
		 */
		public Builder setPositiveButton(int confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText = (String) context
					.getText(confirmButtonText);
			this.confirmButtonClickListener = listener;
			return this;
		}

		/**
		 * @param 确定-String
		 */
		public Builder setPositiveButton(String confirmButtonText,
				DialogInterface.OnClickListener listener) {
			this.confirmButtonText = confirmButtonText;
			this.confirmButtonClickListener = listener;
			return this;
		}

		public CustomSingleDialog create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// dialog样式
			final CustomSingleDialog dialog = new CustomSingleDialog(context,R.style.customDialogStyle);
			dialog.setCanceledOnTouchOutside(false);// 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
			// 弹出窗口透明度设置
			Window window = dialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			// 设置透明度
			lp.alpha = 0.9f;
			window.setAttributes(lp);
			
			final View layout = inflater.inflate(R.layout.custom_single_dialog, null);
			// 根据id在布局中找到控件对象
			confirm_btn_single_dialog = (Button) layout.findViewById(R.id.confirm_btn_single_dialog);
			// 设置按钮的文本颜色
			confirm_btn_single_dialog.setTextColor(Color.parseColor("#67A584"));
			((TextView) layout.findViewById(R.id.custom_single_dialog_title)).setTextColor(Color.parseColor("#6B6B6C"));
			((TextView) layout.findViewById(R.id.custom_single_dialog_message)).setTextColor(Color.parseColor("#6B6B6C"));
			
			// 弹出加动画效果
			linearLayout = (LinearLayout) layout.findViewById(R.id.custom_single_dialog_layout);
			
			TranslateAnimation anim1 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			anim1.setDuration(600);
			TranslateAnimation anim2 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			anim2.setDuration(500);
			((TextView) layout.findViewById(R.id.custom_single_dialog_title)).setAnimation(anim1);
			((TextView) layout.findViewById(R.id.custom_single_dialog_message)).setAnimation(anim2);
			
			TranslateAnimation animLine = new TranslateAnimation(0, 0, -400, 0);//设置动画
			animLine.setInterpolator(new BounceInterpolator());
			animLine.setDuration(1200);
			((LinearLayout) layout.findViewById(R.id.single_dialog_line)).setAnimation(animLine);
			
			TranslateAnimation animBtn1 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			animBtn1.setDuration(400);
			confirm_btn_single_dialog.setAnimation(animBtn1);
			
			/*AnimationSet set = new AnimationSet(true);
			Animation animation = AnimationUtils.loadAnimation(context,R.anim.rotate_anim);
			animation.setDuration(200);
			set.addAnimation(animation);
			LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
			linearLayout.setLayoutAnimation(controller);*/
			
			// 添加视图
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			// dialog 标题
			((TextView) layout.findViewById(R.id.custom_single_dialog_title)).setText(title);
			// 确定
			if (confirmButtonText != null) {
				((Button) layout.findViewById(R.id.confirm_btn_single_dialog)).setText(confirmButtonText);
				if (confirmButtonClickListener != null) {
					((Button) layout.findViewById(R.id.confirm_btn_single_dialog)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									confirmButtonClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// 设置可见性
				layout.findViewById(R.id.confirm_btn_single_dialog).setVisibility(
						View.GONE);
			}
			
			// 提示内容
			if (message != null) {
				((TextView) layout.findViewById(R.id.custom_single_dialog_message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.custom_single_dialog_message)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.custom_single_dialog_message)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}
