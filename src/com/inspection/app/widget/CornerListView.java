package com.inspection.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ListView;

import com.inspection.app.R;

/**
 * @Description: 继承ListView，自定义实现圆角ListView
 * @Date 2013/8/20
 */
public class CornerListView extends ListView {

	public CornerListView(Context context) {
		super(context);
	}

	public CornerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			int itemnum = pointToPosition(x, y);

			if (itemnum == AdapterView.INVALID_POSITION)
				break;
			else {
				if (itemnum == 0) {
					// 只有一行的时候
					if (itemnum == (getAdapter().getCount() - 1)) {
						// setSelector(R.drawable.app_list_corner_round);
						// setSelector(R.drawable.app_list_corner_round_bottom);
						setSelector(R.drawable.app_list_corner_shape);
					} else {
						// 第一行
						// setSelector(R.drawable.app_list_corner_round_top);
						setSelector(R.drawable.app_list_corner_shape);
					}
				} else if (itemnum == (getAdapter().getCount() - 1)) {
					// 最后一行
					setSelector(R.drawable.app_list_corner_round_bottom);
				} else {
					// 中间行
					setSelector(R.drawable.app_list_corner_shape);
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}
}