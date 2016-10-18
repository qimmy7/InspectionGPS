package com.inspection.app.common;

/**
 * android 经纬度度分秒与十进制之间的相互转换
 * @author liuyx
 * @created by 2014/11/24
 */
import java.math.BigDecimal;

public class ConvertLatlng {

	// 经纬度度分秒转换为小数
	public double convertToDecimal(double du, double fen, double miao) {
		if (du < 0)
			return -(Math.abs(du) + (Math.abs(fen) + (Math.abs(miao) / 60)) / 60);

		return Math.abs(du) + (Math.abs(fen) + (Math.abs(miao) / 60)) / 60;

	}

	// 以字符串形式输入经纬度的转换
	public double convertToDecimalByString(String latlng) {

		double du = Double
				.parseDouble(latlng.substring(0, latlng.indexOf("°")));
		double fen = Double.parseDouble(latlng.substring(
				latlng.indexOf("°") + 1, latlng.indexOf("′")));
		double miao = Double.parseDouble(latlng.substring(
				latlng.indexOf("′") + 1, latlng.indexOf("″")));
		if (du < 0)
			return -(Math.abs(du) + (fen + (miao / 60)) / 60);
		return du + (fen + (miao / 60)) / 60;

	}

	// 将小数转换为度分秒
	public String convertToSexagesimal(double num) {

		int du = (int) Math.floor(Math.abs(num)); // 获取整数部分
		double temp = getdPoint(Math.abs(num)) * 60;
		int fen = (int) Math.floor(temp); // 获取整数部分
		double miao = getdPoint(temp) * 60;
		if (num < 0)
			return "-" + du + "°" + fen + "′" + miao + "″";

		return du + "°" + fen + "′" + miao + "″";

	}

	// 获取小数部分
	public double getdPoint(double num) {
		double d = num;
		int fInt = (int) d;
		BigDecimal b1 = new BigDecimal(Double.toString(d));
		BigDecimal b2 = new BigDecimal(Integer.toString(fInt));
		double dPoint = b1.subtract(b2).floatValue();
		return dPoint;
	}

	public static void main(String[] args) {

		ConvertLatlng convert = new ConvertLatlng();
		double latlng1 = convert.convertToDecimal(37, 25, 19.222);
		double latlng2 = convert.convertToDecimalByString("-37°25′19.222″");
		String latlng3 = convert.convertToSexagesimal(121.084095);
		String latlng4 = convert.convertToSexagesimal(-121.084095);

		System.out.println("转换小数(数字参数)" + latlng1);
		System.out.println("转换小数(字符串参数)" + latlng2);
		System.out.println("转换度分秒:" + latlng3);
		System.out.println("转换度分秒:" + latlng4);

	}

}