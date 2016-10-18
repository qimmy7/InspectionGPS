package com.inspection.app.common.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

import com.inspection.app.bean.PatrolBugPhoto;

/**
 * http网络请求
 * 
 * @author liuyx
 * @creatd 2014/10/24
 */

public class HttpRequest {

	/**
	 * Get请求
	 */
	public static String doGet(String url) {
		String result = "failure";

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		HttpClient httpClient = new DefaultHttpClient(httpParams);
		// GET
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			System.out.println("get请求返回的code："
					+ response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.i("GET", "请求失败！");
			} else {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 图片上传－POST请求
	 */
	public static String doUploadPost(String url, String consiteId,
			String consiteState, byte[] imgFile) {
		String result = "failure";

		String sendString = "";
		try {
			sendString = new String(imgFile, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 尚未处理异常
			e.printStackTrace();
		}
		System.out.println(sendString);

		// 第一步，创建HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// 设置HTTP POST请求参数必须用NameValuePair对象
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("consiteId", consiteId));
		params.add(new BasicNameValuePair("consiteState", consiteState));
		params.add(new BasicNameValuePair("imgFile", sendString));

		HttpResponse httpResponse = null;
		try {
			// 设置httpPost请求参数
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			// System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法获得返回结果
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result:" + result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * post方式请求
	 * 
	 */
	public static String doPost(String urlServer, String pathToOurFile, PatrolBugPhoto bo) throws ClientProtocolException,
			IOException, JSONException {
		String result = "failure";
		HttpClient httpclient = new DefaultHttpClient();
		// 设置通信协议版本
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		//请求超时	
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); 
		//读取超时
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		HttpPost httppost = new HttpPost(urlServer);
		File file = new File(pathToOurFile);// 文件路径

		String person_id = bo.getPERSON_ID();
		String content = bo.getCONTENT();
		String longitude = bo.getLONGITUDE();
		String latitude = bo.getLATITUDE();
		String height = bo.getHEIGHT();
		String time = bo.getTIME();
				
		MultipartEntity mpEntity = new MultipartEntity(); // 文件传输
		StringBody par1 = new StringBody(person_id);
		StringBody par2 = new StringBody(content);
		StringBody par3 = new StringBody(longitude);
		StringBody par4 = new StringBody(latitude);
		StringBody par5 = new StringBody(height);
		StringBody par6 = new StringBody(time);
		mpEntity.addPart("person_id", par1);
		mpEntity.addPart("content", par2);
		mpEntity.addPart("longitude", par3);
		mpEntity.addPart("latitude", par4);
		mpEntity.addPart("height", par5);
		mpEntity.addPart("time", par6);
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("imgFile", cbFile);

		httppost.setEntity(mpEntity);
		System.out.println("executing request： " + httppost.getRequestLine());
		try {
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity resEntity = response.getEntity();
			System.out.println("响应码：" + response.getStatusLine());// 通信OK
			if (response.getStatusLine().getStatusCode() == 200) {// 成功
				result = EntityUtils.toString(resEntity, "utf-8");
				/*System.out.println("返回得结果是：" + json);
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(json);
					System.out.println("json格式的数据：" + jsonObject);
					result = jsonObject.getString("result");
				} catch (Exception e) {
					e.printStackTrace();
				}*/
			}else{//请求失败！
				Log.i("GET", "请求失败！");
			}
			if (resEntity != null) {
				resEntity.consumeContent();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			StackTraceElement[] messages = e.getStackTrace();
			int length = messages.length;
			for (int i = 0; i < length; i++) {
				System.out.println("ClassName:" + messages[i].getClassName());
				System.out.println("getFileName:" + messages[i].getFileName());
				System.out.println("getLineNumber:"
						+ messages[i].getLineNumber());
				System.out.println("getMethodName:"
						+ messages[i].getMethodName());
				System.out.println("toString:" + messages[i].toString());
			}
		}
		httpclient.getConnectionManager().shutdown();
		return result;
	}

}
