package com.inspection.app.bean;

/**
 * 巡检数据表
 * 
 * @author liuyx
 * @created 2014/11/21
 */
public class PatrolPersonPath {

	public String PERSON_ID;//当前巡检人
	public String LONGITUDE;//经度
	public String LATITUDE;//纬度
	public String HEIGHT;//海拔
	public String PATH_POINT_TIME;//上传时间
	public String PRINCIPAL_TYPE;//是否是作业点（路径点和作业点）
	public String PRINCIPAL_ID;//作业点的ID
	public String BUG_ID;//缺陷报告ID
	public String IS_SUCCSEND;//是否上传成功(和服务端进行交互)
	public String SPEED;//巡检的速度
	public String PIPEDEVIATION;//距离最近作业点的距离
	public String PHOTO_NAME;//上传照片的名称

	public String getPERSON_ID() {
		return PERSON_ID;
	}

	public void setPERSON_ID(String pERSON_ID) {
		PERSON_ID = pERSON_ID;
	}

	public String getLONGITUDE() {
		return LONGITUDE;
	}

	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}

	public String getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(String hEIGHT) {
		HEIGHT = hEIGHT;
	}

	public String getPATH_POINT_TIME() {
		return PATH_POINT_TIME;
	}

	public void setPATH_POINT_TIME(String pATH_POINT_TIME) {
		PATH_POINT_TIME = pATH_POINT_TIME;
	}

	public String getPRINCIPAL_TYPE() {
		return PRINCIPAL_TYPE;
	}

	public void setPRINCIPAL_TYPE(String pRINCIPAL_TYPE) {
		PRINCIPAL_TYPE = pRINCIPAL_TYPE;
	}

	public String getPRINCIPAL_ID() {
		return PRINCIPAL_ID;
	}

	public void setPRINCIPAL_ID(String pRINCIPAL_ID) {
		PRINCIPAL_ID = pRINCIPAL_ID;
	}

	public String getBUG_ID() {
		return BUG_ID;
	}

	public void setBUG_ID(String bUG_ID) {
		BUG_ID = bUG_ID;
	}

	public String getIS_SUCCSEND() {
		return IS_SUCCSEND;
	}

	public void setIS_SUCCSEND(String iS_SUCCSEND) {
		IS_SUCCSEND = iS_SUCCSEND;
	}

	public String getSPEED() {
		return SPEED;
	}

	public void setSPEED(String sPEED) {
		SPEED = sPEED;
	}

	public String getPIPEDEVIATION() {
		return PIPEDEVIATION;
	}

	public void setPIPEDEVIATION(String pIPEDEVIATION) {
		PIPEDEVIATION = pIPEDEVIATION;
	}

	public String getPHOTO_NAME() {
		return PHOTO_NAME;
	}

	public void setPHOTO_NAME(String pHOTO_NAME) {
		PHOTO_NAME = pHOTO_NAME;
	}

}
