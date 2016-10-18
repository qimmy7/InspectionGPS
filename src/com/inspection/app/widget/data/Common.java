package com.inspection.app.widget.data;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;

/**
 * 全局数据
 * @author ZLL
 */
public class Common {
	
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	
	/**
	 * 绘图区域宽度
	 */
	public static int layoutWidth = 0;
	
	/**
	 * 绘图区域高度
	 */
	public static int layoutHeight = 0;
	
	/**
	 * 图宽
	 */
	public static int viewWidth = 0;
	
	/**
	 * 图高
	 */
	public static int viewHeight = 0;
	
	/**
	 * Y轴区域宽度
	 */
	public static int YWidth = 55;
	
	/**
	 * Y轴标题
	 */
	public static String YName = "";
	
	/**
	 * Y轴标题X坐标
	 */
	public static int YName2Left = 0;
	
	/**
	 * Y轴标题Y坐标
	 */
	public static int YName2Top = 0;
	
	/**
	 * X轴距下方高度（为刻度文本预留高度）
	 */
	public static int XHeight = 20;
	
	/**
	 * 标题
	 */
	public static String title = "";
	
	/**
	 * 标题横坐标
	 */
	public static int  titleX = 40; 
	
	/**
	 * 标题纵坐标
	 */
	public static int  titleY = 40; 
	
	/**
	 * 副标题
	 */
	public static String secondTitle = "";
	
	/**
	 * 副标题横坐标
	 */
	public static int  StitleX = 40; 
	
	/**
	 * 副标题纵坐标
	 */
	public static int  StitleY = 40; 
	
	/**
	 * 标题颜色
	 */
	public static int titleColor = Color.WHITE;
	
	/**
	 * 图例宽度
	 */
	public static int keyWidth = 30;
	
	/**
	 * 图例高度
	 */
	public static int keyHeight = 10;
	
	/**
	 * 图例横坐标（第一个）
	 */
	public static int keyToLeft = 200;
	
	/**
	 * 图例纵坐标（第一个）
	 */
	public static int keyToTop = 80;
	
	/**
	 * 图例之间的距离
	 */
	public static int keyToNext = 80;
	
	/**
	 * 图例文字与图例距离
	 */
	public static int keyTextPadding = 10;
	
	/**
	 * X轴刻度名称
	 */
	//public static String[] xScaleArray = new String[]{"1","2","3","4","5","6","7","8","9","10"};
	
	public static int[] xScaleArrayInt = new int[]{1,2,3,4,5};
	
	/**
	 * X轴颜色
	 */
	public static int xScaleColor = Color.rgb(210,210,222);
	
	/**
	 * Y轴颜色
	 */
	public static int yScaleColor = Color.rgb(210,210,222);
	
	/**
	 * Y轴刻度名称
	 */
	public static int[] yScaleArray = new int[]{0,25,50,100,200,300,500};
	
	/**
	 * Y轴等级分区名称
	 */
	public static String[] levelName = new String[]{"优","良","轻度","中度","重度","严重"};
	
	/**
	 * Y轴等级分区颜色
	 */
	public static int[] yScaleColors = new int[]{0xff00ff00,0xffffff00,0xffffa500,0xffff4500,0xffdc143c,0xffa52a2a};
	
	
	/**
	 * 绘图数据
	 */
	public static List<MyData> DataSeries = new ArrayList<MyData>();
	
	/**
	 * 标题等字体大小
	 */
	public static int bigSize = 25;
	
	/**
	 * 副标题等字体大小
	 */
	public static int smallSize = 20;
}
