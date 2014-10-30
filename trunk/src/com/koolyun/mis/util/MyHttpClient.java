package com.koolyun.mis.util;

import android.content.Context;

import com.koolyun.mis.constants.MyConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyHttpClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * get方式提交url请求
	 * BASE_URL = "http://211.147.75.226:8012/kivvi/api/"
	 * @param url 相对BASE_URL的相对url
	 * @param params 需要上传的键值对，无参数时传null
	 * @param responseHandler 返回的参数类型
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * post方式提交url请求
	 * BASE_URL = "http://211.147.75.226:8012/kivvi/api/"
	 * @param url 相对BASE_URL的相对url
	 * @param params 需要上传的键值对，无参数时传null
	 * @param responseHandler 返回的参数类型
	 */
	public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if(!NetworkUtil.isConnected(context)) {
			Throwable e = new Throwable();
			responseHandler.sendMessage(responseHandler.obtainMessage(1, new Object[]{e, null}));
		} else {
			client.post(getAbsoluteUrl(url), params, responseHandler);
		}
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return MyConstants.API_HOST + relativeUrl;
	}
}
