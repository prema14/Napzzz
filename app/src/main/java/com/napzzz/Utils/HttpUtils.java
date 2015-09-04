package com.napzzz.Utils;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

public class HttpUtils {

	static int retrycount = 0;
	static int max_retry = 4;
	
	/**
	 * post the request to server from get request.
	 * @param context
	 * @param endpoint
	 * @param params
	 * @return
	 * @throws IOException
	 */

	public static String get(Context context, String endpoint, Map<String, String> params) throws IOException {
		Log.i("test", "getCalled for url : " + endpoint);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		URL url = null;
		if (endpoint == null || endpoint.equals("")) {
			Log.i("test", "Endpoint error in get request.");
			return null;
		}
		if (!endpoint.endsWith("?") && params != null
				&& !endpoint.contains("?"))
			endpoint = endpoint + "?";
		String tailurl = null;
		if (params != null && params.size() > 0) {
			StringBuilder bodyBuilder = new StringBuilder();
			Iterator<Entry<String, String>> iterator = params.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				bodyBuilder.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					bodyBuilder.append('&');
				}
			}
			tailurl = bodyBuilder.toString();
		}
		if (tailurl == null)
			tailurl = "";
		else
			tailurl = "&" + tailurl;
		try {
			url = new URL(endpoint + tailurl);
		} catch (MalformedURLException e) {
			Log.i("test", "invalid url: " + endpoint);
		}

		HttpURLConnection conn = null;
		try {
			Log.i("test", "hitting endpoint for get : " + url.toString());
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Get failed with error code " + status);
			}

			InputStream in = conn.getInputStream();

			if (in != null) {
				StringBuilder builder = new StringBuilder();
				String line;
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in, "UTF-8"));
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
				} finally {
					in.close();
				}

				String response = builder.toString();

				if (response != null) {
					Log.i("test", "get response : " + response);
					return response;
				} else
					retrycount = 0;
				return null;
			}
		} catch (UnknownHostException ue) {
			ue.printStackTrace();
		} catch (EOFException eof) {
			if (retrycount < max_retry) {
				eof.printStackTrace();
				get(context, endpoint, params);
				retrycount = 1;
			}
		} catch (Exception th) {
			th.printStackTrace();
			throw new IOException("Error in get :" + th.getMessage());
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		Log.i("test", "Unexpected error in get request.");
		return null;
	}
	
	/**
	 * post the request on server.
	 * @param context
	 * @param endpoint
	 * @param params
	 * @return
	 */

	public static String post(Context context, String endpoint,
			Map<String, String> params) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		StringBuffer paramString = new StringBuffer("");
		StringBuffer tempBuffer = new StringBuffer("");
		if (params != null) {
			Iterator<Entry<String, String>> iterator = params.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				if (param != null) {
					if (paramString.length() > 0) {
						paramString.append("&");
					}
					Log.i("test", "post key : " + param.getKey());
					String value;
					try {
						value = URLEncoder.encode(param.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						value = "";
						e.printStackTrace();
					}
					paramString.append(param.getKey()).append("=")
							.append(value);
				}
			}
		}
		Log.i("test", "post Stringbuffer  : " + paramString.toString());

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(endpoint);
			try {
				// Add your data
				httppost.addHeader("Content-Type",
						"application/x-www-form-urlencoded");
				if (!paramString.equals("")) {
					String data = "";
					if (tempBuffer.length() > 0) {
						data = data + tempBuffer.toString();
					}
					data = data + paramString.toString();
					if (data.endsWith("&")) {
						data = data.substring(0, data.length() - 1);
					}
					Log.i("test", "post data : " + data);
					httppost.setEntity(new ByteArrayEntity(data.getBytes()));
				}

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				int statuscode = response.getStatusLine().getStatusCode();
				Log.i("test", "Response code : " + statuscode);
				if (statuscode != 200) {
					return null;
				}
				HttpEntity entity = response.getEntity();
				InputStream in = null;
				if (entity != null) {
					in = entity.getContent();
				}

				if (in != null) {
					StringBuilder builder = new StringBuilder();
					String line;
					try {
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(in, "UTF-8"));
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					} finally {
						in.close();
					}

					String response2 = builder.toString();

					Log.i("test", "response :" + response2);
					retrycount = 0;
					return response2;
				}
			} catch (EOFException eof) {
				if (retrycount < max_retry) {
					eof.printStackTrace();
					post(context, endpoint, params);
					retrycount = 1;
				}
			} catch (Throwable th) {
				th.printStackTrace();
				throw new IOException("Error in posting :" + th.getMessage());
			}
			retrycount = 0;
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * check the internet connection.
	 * @param context
	 * @return
	 */

	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager localConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if ((localConnectivityManager.getActiveNetworkInfo() != null)
				&& (localConnectivityManager.getActiveNetworkInfo()
						.isAvailable())
				&& (localConnectivityManager.getActiveNetworkInfo()
						.isConnected())) {
			return true;
		} else {
			try {
				Toast.makeText(context, "Internet is not available",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
	}
}
