package com.inspection.app.common.http;

import java.io.Serializable;

/**
 * 接口URL实体类
 * @author liuyx
 * @created 2014/10/24
 */
public class URLs implements Serializable {
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -6025586561516610121L;
	
	/**
	 	所有服务调用方法后，返回值共三个参数：
	    参数1：data，实际用到数据
	    参数2：result, 布尔型，true为正确，false说明发生错误，异常见参数3
	    参数3：msg, 返回的错误信息
	 */
	
	public final static String HOST = "192.168.0.106:8081";//服务器请求地址
	
	public final static String HTTP = "http://";
	public final static String URL_SPLITTER = "/";
	public final static String URL_API_HOST = HTTP + HOST + URL_SPLITTER;
	
	//http://223.223.192.105:88/login/test2/123
	//http://192.168.0.106:8080/login/username/password
	
	
	/**
	 * 访问方式：register/{equnum}/{sn}/{imei}/{sim}/{os_version}/{wlan_mac}/{device_name}
	 */
	public final static String REGISTER_VALIDATE_HTTP = URL_API_HOST + "register";//设备注册
	
	/**
	 * 访问方式：login/{username}/{psd}
	 */
	public final static String LOGIN_VALIDATE_HTTP = URL_API_HOST + "login";//用户登录验证
	
	/**
	 * 访问方式：getEquUserInfo/{equnum}/{imei}
	 */
	public final static String GETEQUUSERINFO_VALIDATE_HTTP = URL_API_HOST + "getEquUserInfo";//根据设备参数获取已分配的用户
	
	/**
	 * 访问方式：getInspectionParam
	 */
	public final static String GETPARAM_VALIDATE_HTTP = URL_API_HOST + "getInspectionParam";//获取巡检参数
	
	/**
	 * 访问方式：getPatrolBugType
	 */
	public final static String GETBUGTYPE_VALIDATE_HTTP = URL_API_HOST + "getPatrolBugType";//获取缺陷报告类型
	
	/**
	 * 访问方式：getStartAndEndPointBySublineId/{subline_id}
	 */
	public final static String SUBLINE_HTTP = URL_API_HOST + "getStartAndEndPointBySublineId";//根据当前用户的管线subline_id获取任务管线开始点和结束点
	
	/**
	 * 访问方式：getPilePoint/{subline_id}
	 */
	public final static String PILEPOINT_HTTP = URL_API_HOST + "getPilePoint";//根据当前用户的管线subline_id获取桩点
	
	/**
	 * 访问方式：getOperatingPoint/{subline_id}
	 */
	public final static String TASKPOINT_HTTP = URL_API_HOST + "getOperatingPoint";//根据当前用户的管线subline_id获取任务作业点
	
	/**
	 * uploadStayUploadData/{personId}/{longitude}/{latitude}/{height}/{pathPointTime}/{principalType}/{principalId}/{bugId}/{isSuccSend}/{speed}/{pipeDeviation}/{photoName}
	 */
	public final static String UPLOAD_HTTP = URL_API_HOST + "uploadStayUploadData";//GPS巡检待上传
	
	/**
	 * uploadBugPatrolPic 以POST方式提交
	 */
	public final static String UPLOAD_BUGPATROL_HTTP = URL_API_HOST + "uploadBugPatrolPic";//缺陷报告上传图片和巡检数据
	
	/**
	 * 访问方式：getCompleteOperatingPointNum/{subline_id}/{personId}
	 */
	public final static String COMPLETE_TASKPOINT_HTTP = URL_API_HOST + "getCompleteOperatingPointNum";//根据当前用户的管线subline_id获取已巡检任务作业点数
	
	
	
}
