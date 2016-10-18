package com.inspection.app.bean;

/**
 * 巡检作业点
 * 
 * @author liuyx
 * @created 2014/11/28
 */
public class PatrolTaskPoint {

	public Integer id;
	public Integer subline_id;
	public String longitude;
	public String latitude;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSubline_id() {
		return subline_id;
	}

	public void setSubline_id(Integer subline_id) {
		this.subline_id = subline_id;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
