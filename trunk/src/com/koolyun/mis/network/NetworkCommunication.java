package com.koolyun.mis.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.koolyun.mis.util.MyLog;

public class NetworkCommunication {

	// public static final String BASE_URL = "http://192.168.1.20/sirius/";
	public static final String BASE_URL = "http://gokivvi.com:8012/kivvi/api/api/";

	private static int connTimeout = 10000;
	private static int rdTimeout = 10000;

	static {
		try {
			openHttps();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static String getRequest(String urlString) {
		HttpURLConnection httpURLConnection = null;
		String result = null;
		try {
			URL url = new URL(urlString);
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(connTimeout);
			httpURLConnection.setReadTimeout(rdTimeout);
			if (httpURLConnection.getResponseCode() == 200) {
				result = readStream(httpURLConnection.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null)
				httpURLConnection.disconnect();
		}
		return result;
	}

	public static String postRequest(String urlString, String jsonString) {
		HttpURLConnection httpURLConnection = null;
		String result = null;

		// AssetManager am = context.getAssets();
		// InputStream ins = am.open("robusoft.cer");
		// try {
		// //读取证书
		// CertificateFactory cerFactory =
		// CertificateFactory.getInstance("X.509"); //问1
		// Certificate cer = cerFactory.generateCertificate(ins);
		// //创建一个证书库，并将证书导入证书库
		// KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC"); //问2
		// keyStore.load(null, null);
		// keyStore.setCertificateEntry("trust", cer);
		// } finally {
		// ins.close();
		// }
		// TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
		// tmf.init(keyStore);
		//
		// SSLContext context = SSLContext.getInstance("TLS");
		// context.init(null, tmf.getTrustManagers(), null);

		try {
			URL url = new URL(BASE_URL + urlString);
			MyLog.i("url", url.toString());
			httpURLConnection = (HttpURLConnection) url.openConnection();

			// httpURLConnection.setSSLSocketFactory(context.getSocketFactory());

			httpURLConnection.setConnectTimeout(connTimeout);
			httpURLConnection.setReadTimeout(rdTimeout);

			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// httpURLConnection.setRequestProperty("Content-Type",
			// "application/json");
			// httpURLConnection.setRequestProperty("Accept",
			// "application/json");
			// httpURLConnection.setRequestProperty("Charset", HTTP.UTF_8);
			// httpURLConnection.setRequestProperty("application/json",
			// "charset=UTF-8");
			// httpURLConnection.setRequestProperty("text/json",
			// "charset=gb2312");
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setInstanceFollowRedirects(false);
			httpURLConnection.setChunkedStreamingMode(0);
			// 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
			httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
			// 要注意的是connection.getOutputStream会隐含的进行connect。
			// httpURLConnection.connect();

			// OutputStream postOutputStream =
			// httpURLConnection.getOutputStream();
			writeStream(httpURLConnection.getOutputStream(), jsonString);

			if (httpURLConnection.getResponseCode() == 200) {
				result = readStream(httpURLConnection.getInputStream());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null)
				httpURLConnection.disconnect();
		}
		return result;
	}

	public static String uploadFile(String urlString) {
		// String end = "\r\n";
		// String twoHyphens = "--";
		// String boundary = "******";
		// String result = null;
		// HttpURLConnection httpURLConnection = null;
		//
		// try {
		// URL url = new URL(BASE_URL + urlString);
		// httpURLConnection = (HttpURLConnection) url.openConnection();
		// // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
		// // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
		// httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
		// // 允许输入输出流
		// httpURLConnection.setDoInput(true);
		// httpURLConnection.setDoOutput(true);
		// httpURLConnection.setUseCaches(false);
		// // 使用POST方法
		// httpURLConnection.setRequestMethod("POST");
		// httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		// httpURLConnection.setRequestProperty("Charset", "UTF-8");
		// httpURLConnection.setRequestProperty("Content-Type",
		// "multipart/form-data;boundary=" + boundary);
		// // httpURLConnection.setRequestProperty("enctype",
		// // "multipart/form-data;boundary=" + boundary);

		// DataOutputStream dos = new DataOutputStream(
		// httpURLConnection.getOutputStream());
		// // dos.writeBytes("sid=" + CloudPosApp.getInstance().getSessionID()
		// // + end);
		// dos.writeBytes(twoHyphens + boundary + end);
		// dos.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\""
		// + CloudPosApp.getInstance().getOrder().getSignature()
		// + "\"" + end);
		// dos.writeBytes(end);
		//
		// FileInputStream fis = new FileInputStream(Common.SIGNATURE_PATH
		// + CloudPosApp.getInstance().getOrder().getSignature());
		// byte[] buffer = new byte[2048]; // 2k
		// int count = 0;
		// // 读取文件
		// while ((count = fis.read(buffer)) != -1) {
		// dos.write(buffer, 0, count);
		// }
		// fis.close();

		// dos.writeBytes(end);
		// // dos.writeBytes("sid=" + CloudPosApp.getInstance().getSessionID()
		// // + end);
		//
		// dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
		// dos.flush();
		// MyLog.i(TAG, "ResponseCode: " + httpURLConnection.getResponseCode());
		//
		// if (httpURLConnection.getResponseCode() == 200) {
		// result = readStream(httpURLConnection.getInputStream());
		// MyLog.i(TAG, "result200: " + result);
		// } else {
		// result = readStream(httpURLConnection.getInputStream());
		// MyLog.i(TAG, "result: " + result);
		// }
		// dos.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// if (httpURLConnection != null)
		// httpURLConnection.disconnect();
		// }

		// return result;
		return null;
	}

	// 设置网络超时
	public static void setTimeout(int connectTimeout, int readTimeout) {
		connTimeout = connectTimeout;
		rdTimeout = readTimeout;
	}

	private static String readStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	private static void writeStream(OutputStream os, String jsonString) throws IOException {
		DataOutputStream out = new DataOutputStream(os);
		out.write(jsonString.getBytes());
		MyLog.i("out..", jsonString);
		out.flush();
		out.close();
	}

	// private void enableHttpResponseCache() {
	// try {
	// long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
	// File httpCacheDir = new File(getCacheDir(), "http");
	// Class.forName("android.net.http.HttpResponseCache")
	// .getMethod("install", File.class, long.class)
	// .invoke(null, httpCacheDir, httpCacheSize);
	// } catch (Exception httpResponseCacheNotAvailable) {
	// MyLog.d(TAG, "HTTP response cache is unavailable.");
	// }
	// }

	private static void openHttps() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
		Class<?> opensslsocketimpl = Class.forName("org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl");
		Field[] fieldlist = opensslsocketimpl.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field field = fieldlist[i];
			if (field.getName().equals("sslengine")) {
				field.setAccessible(true);
				field.set(null, true);
			}

			if (field.getName().equals("certstring")) {
				field.setAccessible(true);
				String certid = "01";
				field.set(null, certid);
			}
		}

		Class<?> httpsconnection = Class.forName("android.net.http.HttpsConnection");
		fieldlist = httpsconnection.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			Field field = fieldlist[i];
			if (field.getName().equals("sslwebview")) {
				field.setAccessible(true);
				field.set(null, false);
			}
		}
	}
}
