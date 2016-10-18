package com.inspection.app.widget.data;

import java.util.List;

import android.R.integer;

public class MyData {
	/**
	 * 数据名称
	 */
	private String name;
	
	/**
	 * 数据内容
	 */
	private int[] data;
	
	/**
	 * 数据源
	 */
	private List<Integer> dataList;
	
	/**
	 * 数据颜色
	 */
	private int color;
	
	
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public void setData(int[] data){
		this.data = data;
	}
	public int[] getData(){
		return data;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	public int getColor(){
		return color;
	}
	
	public List<Integer> getDataList() {
		return dataList;
	}
	public void setDataList(List<Integer> dataList) {
		this.dataList = dataList;
	}
	
	
}
