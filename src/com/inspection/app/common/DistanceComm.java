package com.inspection.app.common;

/**
 * 计算距离
 * 
 * @author liuyx
 * @created by 2014/11/27
 */
public class DistanceComm {

	/**
	 * 计算地球上任意两点距离 第一个点经度，第一个点纬度；第二个点经度，第二个点纬度 <param
	 * name="long1">第一个点经度</param> <param name="lat1">第一个点纬度</param> <param
	 * name="long2">第二个点经度</param> <param name="lat2">第二个点纬度</param>
	 * 
	 * @param long1
	 * @param lat1
	 * @param long2
	 * @param lat2
	 * @return
	 */
	public static int Distance(double long1, double lat1, double long2,
			double lat2) {
		double a, b, R, PI;
		R = 6378137.5; // 地球半径
		PI = 3.14159265358979323846264338327950288;
		lat1 = lat1 * PI / 180.0;
		lat2 = lat2 * PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return (int) Math.round(d);
	}

	// 转成墨卡托投影坐标
	public static double[] lonlatToMercator(double long1, double lat1) {
		double semimajorAxis = 6378137.0;// 椭球体长半轴

		double semiminorAxis = 6356752.314245;// 椭球体短半轴
		// x = 110.37663833333333, y = 19.7343125
		// 经度转换成弧度
		double lon = Math.PI * long1 / 180;
		double originLon = Math.PI * 110 / 180;
		// lon = 1.9264357561996528, originLon = 1.9198621771937625

		// 纬度转换成弧度
		double lat = Math.PI * lat1 / 180;
		double originLat = 0;
		// lat = 0.3444287287424735 ,originLat = 0

		// 第一偏心率 e
		double e = Math.sqrt(1 - semiminorAxis * semiminorAxis
				/ (semimajorAxis * semimajorAxis));
		// e = 0.08181919084296556

		double N = semimajorAxis
				/ Math.sqrt(1 - e * e * Math.sin(originLat)
						* Math.sin(originLat));
		// N = 6378137.0

		double sinB = Math.sin(lat);
		// sinB = 0.33765901273440996

		double x = N * (lon - originLon);
		// x = 41927.18747989204

		double y = N
				* Math.log(Math.tan(Math.PI / 4 + lat / 2)
						* Math.pow((1 - e * sinB) / (1 + e * sinB), e / 2));
		// y = 2227162.0900114905

		return new double[] { x, y };
	}

	// 计算两点之间的距离
	public static int lineSpace(double x1, double y1, double x2, double y2) {
		double lineLength = 0;
		lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		return (int) Math.round(lineLength);
	}

	// 当前坐标到桩点的垂足距离，点到直线的最短距离的判断点（x0,y0）到由两点组成的线段（x1,y1）,( x2,y2 )
	public static int pointToLine(double x0, double y0, double x1, double y1, double x2, double y2) {
		int space = 0;
		double a, b, c;
		a = lineSpace(x1, y1, x2, y2);// 线段的长度
		b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
		c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
		if (c <= 0.000001 || b <= 0.000001) {
			space = 0;
			return space;
		}
		if (a <= 0.000001) {
			space = (int) b;
			return space;
		}
		if (c * c >= a * a + b * b) {
			space = (int) b;
			return space;
		}
		if (b * b >= a * a + c * c) {
			space = (int) c;
			return space;
		}
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		space = (int) (2 * s / a);// 返回点到线的距离（利用三角形面积公式求高）
		return space;
	}
	

}
