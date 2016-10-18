package com.inspection.app.bean;

/**
 * 管线桩点
 * 
 * @author liuyx
 * @created by 2014/11/27
 */
public class PipemodeCenterLineStake {

	public Integer subline_id;
	public Integer bid;
	public Integer serial_NO;
	public String stake_NO;
	public Double x;
	public Double y;

	public Integer getSubline_id() {
		return subline_id;
	}

	public void setSubline_id(Integer subline_id) {
		this.subline_id = subline_id;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getSerial_NO() {
		return serial_NO;
	}

	public void setSerial_NO(Integer serial_NO) {
		this.serial_NO = serial_NO;
	}

	public String getStake_NO() {
		return stake_NO;
	}

	public void setStake_NO(String stake_NO) {
		this.stake_NO = stake_NO;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

}
