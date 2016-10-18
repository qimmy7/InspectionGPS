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
 * 折线图
 * @author ZLL
 */
@SuppressLint("DrawAllocation")
public class LineView extends View {
	private float xPoint = 0;// 原点X坐标
    private float yPoint = 0;// 原点Y坐标
    private float xLengh = 240;// X轴长度
    private float yLengh = 320;// Y轴长度
    private float xScale = 5;// X轴一个刻度长度
    private int widthBorder = 5;// 内边缘宽度，为了统计图不靠在屏幕的边缘上，向边缘缩进距离。最好大于30。
    private int[] yLableArray;// Y轴标签,用于计算
    private int[] xLableArray;// x轴标签
    private boolean mp=false;
    
    private float[] begins;//Y轴刻度的y坐标
    private float each;//均分后刻度之间的长度
    public LineView(Context context) {
        super(context);
    }

    /**
     * 实例化值
     *
     * @param screenWidth  图表宽度
     * @param ScreenHeight 图表高度
     * @param xLable       X轴标签
     * @param yLable       Y轴标签
     * @param dataArray    统计数据
     */
    public void initValue(int Width, int Height, boolean mp) {
        xPoint = widthBorder;
        yPoint = Height ;
        xLengh = Width - widthBorder * 2 ;
        yLengh = Height ;
        xScale = getScale(Common.xScaleArrayInt.length - 1, xLengh);
        yLableArray = Common.yScaleArray;
        xLableArray = Common.xScaleArrayInt;
        this.mp=mp;

        help2getPoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGrid(canvas);
        // 画折线
        drawCurve(canvas);
    }

    private void drawGrid(Canvas canvas) {
    	Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    	paint.setColor(Color.rgb(210,210,222));// 设置颜色
        paint.setStrokeWidth(2);
    	paint.setAlpha(100);
    	
		for(float lineHeight:begins){
			canvas.drawLine(xPoint, lineHeight, xPoint+xLengh, lineHeight, paint);
		}
		
		for(int i=0;i<xLableArray.length;i++){
			canvas.drawLine(xPoint+xScale*i, yPoint, xPoint+xScale*i, yPoint-yLengh, paint);
		}
	}

	private void drawCurve(Canvas canvas) {
    	for(MyData data:Common.DataSeries){
    		if(data.getData().length > 0)
    	    	drawLine(canvas, data.getData(), data.getColor());
    	}
    }

    /**
     * 画AQI折线
     *
     * @param @param canvas
     * @param @param i
     * @return void
     * @Title: drawCurve
     * @Description: TODO
     */
    private void drawLine(Canvas canvas, int[] array, int color) {
    	int radius=3;
    	int lineWidth=2;
    	
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(210,210,222));// 设置颜色
        paint.setStrokeWidth(2);
        paint.setTextSize(Integer.parseInt(getResources().getString(R.string.textsizeX)));// 设置字体
        for (int i = 0; i < array.length; i++) {
            if (array[i] > -1) {
                float ydata = getYDataPoint(array[i]);
                if (ydata >= 0) {
                    if(mp){
                    	paint.setStrokeWidth(1);
                    	if(i < array.length - 1)
                    		canvas.drawText(array[i]+"", xPoint + xScale * i, ydata-5, paint);
                    	else 
                    		canvas.drawText(array[i]+"", xPoint - 30 + xScale * i, ydata-5, paint);
                    	paint.setStrokeWidth(3);
                    	if(Common.screenWidth<540)
                    		radius=5;
                    	else 
                    		radius=8;
                    	lineWidth=3;
                    }
                    paint.setStrokeWidth(lineWidth);
                	canvas.drawCircle(xPoint + xScale * i, ydata, radius, paint);
                    if (i < array.length - 1) {
                        if (array[i + 1] > -1) {
                            canvas.drawLine(
                                    xPoint + xScale * i,
                                    ydata,
                                    xPoint + xScale * (i + 1),
                                    getYDataPoint(array[i + 1]),
                                    paint);
                        }
                    } else {
                        canvas.drawLine(xPoint + xScale * i, ydata, xPoint
                                + xScale * i, ydata, paint);
                    }
                }
            }
        }
    }

    
    
    /**
     * 得到每一等分的长度
     *
     * @param num    所要分成的等份
     * @param length 要分割的总长度
     * @return
     */
    private float getScale(int num, float length) {
        if (num > 0 && length > 0) {
            length -= 10;// 为了美观，缩进
            length = length - (length % num);
            return length / num;
        } else {
            return 0;
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
