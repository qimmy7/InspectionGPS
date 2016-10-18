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
import android.view.animation.AlphaAnimation;
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
 * 弹出对话框－两个按钮
 * @author liuyx
 * @createtime 2014/7/18
 */

public class CustomDoubleDialog extends Dialog {

	public CustomDoubleDialog(Context context) {
		super(context);
	}

	public CustomDoubleDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {

		private Context context;
		private Button left_btn_double_dialog,right_btn_double_dialog;
		private String title;
		private String message;
		private String leftButtonText;
		private String rightButtonText;
		private View contentView;
		private DialogInterface.OnClickListener leftButtonClickListener;
		private DialogInterface.OnClickListener rightButtonClickListener;
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
		public Builder setPositiveButton(int leftButtonText,
				DialogInterface.OnClickListener listener) {
			this.leftButtonText = (String) context
					.getText(leftButtonText);
			this.leftButtonClickListener = listener;
			return this;
		}

		/**
		 * @param 确定-String
		 */
		public Builder setPositiveButton(String leftButtonText,
				DialogInterface.OnClickListener listener) {
			this.leftButtonText = leftButtonText;
			this.leftButtonClickListener = listener;
			return this;
		}

		/**
		 * @param 取消-int
		 */
		public Builder setNegativeButton(int rightButtonText,
				DialogInterface.OnClickListener listener) {
			this.rightButtonText = (String) context
					.getText(rightButtonText);
			this.rightButtonClickListener = listener;
			return this;
		}

		/**
		 * @param 取消-String
		 */
		public Builder setNegativeButton(String rightButtonText,
				DialogInterface.OnClickListener listener) {
			this.rightButtonText = rightButtonText;
			this.rightButtonClickListener = listener;
			return this;
		}

		public CustomDoubleDialog create() {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// dialog样式
			final CustomDoubleDialog dialog = new CustomDoubleDialog(context,R.style.customDialogStyle);
			dialog.setCanceledOnTouchOutside(false);// 调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
			// 弹出窗口透明度设置
			Window window = dialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			// 设置透明度
			lp.alpha = 0.9f;
			window.setAttributes(lp);
			
			final View layout = inflater.inflate(R.layout.custom_double_dialog, null);
			// 根据id在布局中找到控件对象
			right_btn_double_dialog = (Button) layout.findViewById(R.id.right_btn_double_dialog);
			left_btn_double_dialog = (Button) layout.findViewById(R.id.left_btn_double_dialog);
			// 设置按钮的文本颜色
			((TextView) layout.findViewById(R.id.custom_double_dialog_title)).setTextColor(Color.parseColor("#6B6B6C"));
			((TextView) layout.findViewById(R.id.custom_double_dialog_message)).setTextColor(Color.parseColor("#6B6B6C"));
			left_btn_double_dialog.setTextColor(Color.parseColor("#67A584"));
			right_btn_double_dialog.setTextColor(0xff1E90FF);
			
			// 弹出加动画效果
			linearLayout = (LinearLayout) layout.findViewById(R.id.custom_double_dialog_layout);
			
			TranslateAnimation anim1 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			anim1.setDuration(600);
			TranslateAnimation anim2 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			anim2.setDuration(500);
			((TextView) layout.findViewById(R.id.custom_double_dialog_title)).setAnimation(anim1);
			((TextView) layout.findViewById(R.id.custom_double_dialog_message)).setAnimation(anim2);
			
			//弹出窗口中间水平分割线
			TranslateAnimation animLine = new TranslateAnimation(0, 0, -200, 0);//设置动画
			//animLine.setInterpolator(new BounceInterpolator());
			animLine.setDuration(800);
			((LinearLayout) layout.findViewById(R.id.double_dialog_line)).setAnimation(animLine);
			
			//弹出窗口中间按钮分割线
			TranslateAnimation animHorizontalLine = new TranslateAnimation(0, 0, -150, 0);//设置动画
			animHorizontalLine.setDuration(900);
			((LinearLayout) layout.findViewById(R.id.double_dialog_horizontalline)).setAnimation(animHorizontalLine);
			
			TranslateAnimation animBtn1 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			animBtn1.setDuration(400);
			TranslateAnimation animBtn2 = new TranslateAnimation(0, 0, -1000, 0);//设置动画
			animBtn2.setDuration(500);
			left_btn_double_dialog.setAnimation(animBtn1);
			right_btn_double_dialog.setAnimation(animBtn2);
			
			/*AnimationSet set = new AnimationSet(true);
			Animation animation = AnimationUtils.loadAnimation(context,R.anim.rotate_anim);
			animation.setDuration(200);
			set.addAnimation(animation);
			LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
			linearLayout.setLayoutAnimation(controller);*/
			
			// 添加视图
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			// dialog 标题
			((TextView) layout.findViewById(R.id.custom_double_dialog_title)).setText(title);
			// 确定
			if (leftButtonText != null) {
				((Button) layout.findViewById(R.id.left_btn_double_dialog)).setText(leftButtonText);
				if (leftButtonClickListener != null) {
					((Button) layout.findViewById(R.id.left_btn_double_dialog)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									leftButtonClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// 设置可见性
				layout.findViewById(R.id.left_btn_double_dialog).setVisibility(
						View.GONE);
			}
			// 取消
			if (rightButtonText != null) {
				((Button) layout.findViewById(R.id.right_btn_double_dialog)).setText(rightButtonText);
				if (rightButtonClickListener != null) {
					((Button) layout.findViewById(R.id.right_btn_double_dialog)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									rightButtonClickListener.onClick(dialog,DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// 设置可见性
				layout.findViewById(R.id.right_btn_double_dialog).setVisibility(
						View.GONE);
			}
			// 提示内容
			if (message != null) {
				((TextView) layout.findViewById(R.id.custom_double_dialog_message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.custom_double_dialog_message)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.custom_double_dialog_message)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

	}

}
