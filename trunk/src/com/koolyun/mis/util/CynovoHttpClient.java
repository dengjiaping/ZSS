package com.koolyun.mis.util;

import com.koolyun.mis.network.NetworkUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CynovoHttpClient {
	private static final String BASE_URL = "http://gokivvi.com:8012/kivvi/api/";

	private static AsyncHttpClient client = new AsyncHttpClient();

	/**
	 * get方式提交url请求 BASE_URL = "http://211.147.75.226:8012/kivvi/api/"
	 * 
	 * @param url
	 *            相对BASE_URL的相对url
	 * @param params
	 *            需要上传的键值对，无参数时传null
	 * @param responseHandler
	 *            返回的参数类型
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	/**
	 * post方式提交url请求 BASE_URL = "http://211.147.75.226:8012/kivvi/api/"
	 * 
	 * @param url
	 *            相对BASE_URL的相对url
	 * @param params
	 *            需要上传的键值对，无参数时传null
	 * @param responseHandler
	 *            返回的参数类型
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if (!NetworkUtil.isConnected()) {
			Throwable e = new Throwable();
			responseHandler.sendMessage(responseHandler.obtainMessage(1, new Object[] { e, null }));
		} else {
			client.post(getAbsoluteUrl(url), params, responseHandler);
		}
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}
