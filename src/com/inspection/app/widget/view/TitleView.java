package com.inspection.app.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.inspection.app.R;
import com.inspection.app.widget.data.Common;
import com.inspection.app.widget.data.MyData;

/**
 * 标题及图例等
 * @author ZLL
 */
@SuppressLint("DrawAllocation")
public class TitleView extends View {
    public TitleView(Context context) {
        super(context);
    }

    /**
     * 实例化值
     *
     */
    public void initValue() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 设置画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);// 设置画笔样式
        paint.setAntiAlias(true);// 去锯齿
        paint.setColor(Color.rgb(210,210,222));// 设置颜色
        paint.setTextSize(Integer.parseInt(getResources().getString(R.string.textsizeX)));// 设置字体
        paint.setStrokeWidth(2);

        
        drawTitle(canvas);
        drawRect(canvas);
        drawYName(canvas);
    }
    
    private void drawTitle(Canvas canvas){
    	// 设置画笔
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);// 设置画笔样式
        paint.setAntiAlias(true);// 去锯齿
        paint.setColor(Common.titleColor);// 设置颜色
        paint.setTextSize(Common.bigSize);// 设置字体
        
        canvas.drawText(Common.title, Common.titleX, Common.titleY, paint);
        
        paint.setTextSize(Common.smallSize);
        canvas.drawText(Common.secondTitle, Common.StitleX, Common.StitleY , paint);
    }    
    
    private void drawRect(Canvas canvas){
    	int count = 0;
    	
    	int width = Common.keyWidth;
    	int height = Common.keyHeight;
    	int toLeft = Common.keyToLeft;
    	int toTop = Common.keyToTop;
    	int toNext = Common.keyToNext;
    	int textPadding = Common.keyTextPadding;
    	
    	for(MyData data:Common.DataSeries){
    		Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL_AND_STROKE);// 设置画笔样式
            paint.setAntiAlias(true);// 去锯齿
            paint.setColor(data.getColor());// 设置颜色
            paint.setStrokeWidth(2);
            
            if(toLeft+toNext*count+width > Common.screenWidth){
            	toTop += height*3/2;
            	count = 0;
            }
	        canvas.drawRect(toLeft+toNext*count, toTop, toLeft+toNext*count+width, toTop+height, paint);
	        canvas.drawText(data.getName(), toLeft+toNext*count+width+textPadding, toTop+height, paint);
            
            count++;
    	}
    }
    
    private void drawYName(Canvas canvas){
    	
    	Paint paint = new Paint();
    	paint.setStyle(Paint.Style.FILL_AND_STROKE);
    	paint.setAntiAlias(true);
    	paint.setTextSize(Common.bigSize);
    	paint.setColor(Common.titleColor);
    	canvas.rotate(-90, Common.YName2Left, Common.YName2Top);
    	canvas.drawText(Common.YName, Common.YName2Left, Common.YName2Top, paint);
    }
}
