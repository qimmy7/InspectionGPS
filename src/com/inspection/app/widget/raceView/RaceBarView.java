package com.inspection.app.widget.raceView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.inspection.app.R;
import com.inspection.app.widget.data.Common;
import com.inspection.app.widget.data.MyData;
import com.inspection.app.widget.data.RaceCommon;

public class RaceBarView extends View{
	
	private float xPoint = 0;// 原点X坐标
	private float startPoint = 0; //画图起点X坐标
    private float yPoint = 0;// 原点Y坐标
    private float xLengh = 0;// X轴长度
    private float yLengh = 0;// Y轴长度
    private int widthBorder = 5;// 内边缘宽度，为了统计图不靠在屏幕的边缘上，向边缘缩进距离。最好大于30。
    private int[] yLableArray;// Y轴标签,用于计算
    private float[] begins;//Y轴刻度的y坐标
    private float each;//均分后刻度之间的长度
    
	public RaceBarView(Context context) {
		super(context);
	}

	public void initValue(int Height){
		xPoint = widthBorder;
        yPoint = Height ;
        xLengh = RaceCommon.viewWidth ;
        yLengh = Height ;
        yLableArray = RaceCommon.yScaleArray;
		startPoint = xPoint + (RaceCommon.raceWidth - (RaceCommon.barWidth + RaceCommon.space)*RaceCommon.DataSeries.size())/2;
		
        help2getPoint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawGrid(canvas);
		drawRect(canvas);
	}

	private void drawGrid(Canvas canvas) {
		Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    	paint.setColor(Common.xScaleColor);// 设置颜色
    	paint.setStrokeWidth(1);
    	paint.setAlpha(50);
    	
		for(float lineHeight:begins){
			canvas.drawLine(xPoint, lineHeight, xPoint+xLengh, lineHeight, paint);
		}
	}

	private void drawRect(Canvas canvas) {
		int count = 0;
		for(MyData data:RaceCommon.DataSeries){
//			startPoint += (RaceCommon.space+RaceCommon.barWidth);
			float point = startPoint + (RaceCommon.space+RaceCommon.barWidth)*count;
			Paint paint = new Paint();
	        paint.setStyle(Paint.Style.FILL);
	        paint.setAntiAlias(true);
	        paint.setColor(data.getColor());
	        paint.setTextSize(Integer.parseInt(getResources().getString(R.string.textsize)));// 设置字体
	        for(int i=0;i<data.getDataList().size();i++){
	        	float ydata = getYDataPoint(Integer.valueOf(data.getDataList().get(i)));
	        	float x = point+RaceCommon.raceWidth*i;
	        	canvas.drawRect(x, ydata, x+RaceCommon.barWidth, yPoint, paint);
	        	canvas.drawText(Integer.valueOf(data.getDataList().get(i))+"", x, ydata-10, paint);
	        }
	        /*for(int i=0;i<data.getData().length;i++){
	        	float ydata = getYDataPoint(data.getData()[i]);
	        	float x = point+RaceCommon.raceWidth*i;
	        	canvas.drawRect(x, ydata, x+RaceCommon.barWidth, yPoint, paint);
	        	canvas.drawText(data.getData()[i]+"", x, ydata-10, paint);
	        }*/
	        count++;
		}
	}

	/**
     * 均分Y轴
     */
    private void help2getPoint(){
    	begins = new float[yLableArray.length];
    	each = yLengh / (yLableArray.length-1);
    	for(int i=0;i<yLableArray.length-1;i++){
    		if(i==0){
    			begins[i] = yPoint;
    		}
    		else{
    			begins[i] = begins[i-1] - each;
    		}
    	}
    }
    
    /**
     * 得到点的Y坐标
     * @param data 输入值
     * @return 对应Y坐标
     */
    private float getYDataPoint(int data){
    	for(int i=0;i<yLableArray.length;i++){
    		if(data == -1)
    			return yPoint;
    		if(data < yLableArray[i]){
    			float f = begins[i] + each*(yLableArray[i] - data)/(yLableArray[i] - yLableArray[i-1]);
    			return f;
    		}
    	}
    	return 0;
    }
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension((int)xLengh, (int)yLengh);  
	}
}
