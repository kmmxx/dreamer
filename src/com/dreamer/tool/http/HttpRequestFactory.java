package com.dreamer.tool.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Log;

public class HttpRequestFactory {

	public static final String USERAGENT = "Mozilla/5.0 (Macintosh; "
			+ "U; Intel Mac OS X 10_6_3; en-us) AppleWebKit/533.16 (KHTML, "
			+ "like Gecko) Version/5.0 Safari/533.16";
	public static final String CONTENT_LENGTH = "Content-Length";
	static HttpRequestFactory mHttpRequestFactory;
	static Object mutex = new Object();

	public static HttpRequestFactory getInstance() {
		synchronized (mutex) {
			if (mHttpRequestFactory == null) {
				mHttpRequestFactory = new HttpRequestFactory();
			}
			return mHttpRequestFactory;
		}
	}

	private HttpRequestFactory() {

	}
	
	public HttpRequest create(String url){
		return new HttpRequestImp(url);
	}
	
	public interface HttpRequest{
		
		public void setRequestMethod(String method);

		public String execute();
	}
	
	public class HttpRequestImp implements HttpRequest{
		public static final String POST = "post";
		public static final String GET = "get";
		public static final String PUT = "put";
		public static final String DELETE = "delete";
		public static final String OPTIONS = "options";
		public static final String HEAD = "head";
		public static final String TRACE = "trace";
		
		String mRequestUrl;
		String mMethod = GET;
		List<BasicNameValuePair> mNameValuePairs;
		HttpRequestImp(String url){
			mRequestUrl = url;
		}
		
		@Override
		public void setRequestMethod(String method) {
			// TODO Auto-generated method stub
			if(GET.equals(method)){
				mMethod = GET;
			}else if(POST.equals(method)){
				mMethod = POST;
			}
		}
		
		/**
		 * �÷������õĲ���ֻ��Ӧ����POST����
		 * 
		 * @param key
		 * @param val
		 */
		@SuppressWarnings("unused")
		public synchronized void setParamets(String key, String val) {
			if (mNameValuePairs == null) {
				mNameValuePairs = new ArrayList<BasicNameValuePair>();
			}
			mNameValuePairs.add(new BasicNameValuePair(key, val));
		}

		@TargetApi(Build.VERSION_CODES.FROYO)
		@SuppressLint("NewApi")
		@Override
		public String execute() {
			// TODO Auto-generated method stub
			AndroidHttpClient mAndroidHttpClient = AndroidHttpClient.newInstance(USERAGENT);
			HttpResponse mHttpResponse = null;
			try{
				if (mMethod.equals(POST)) {
					HttpPost mHttpPost = new HttpPost(mRequestUrl);
					if (mNameValuePairs != null) {
						mHttpPost.setEntity(new UrlEncodedFormEntity(mNameValuePairs));
					}
					mHttpResponse = new DefaultHttpClient().execute(mHttpPost);
					if(mHttpResponse.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
						return null;
					}
					return EntityUtils.toString(mHttpResponse.getEntity());
				}else if(mMethod.equals(GET)){
					HttpGet mHttpGet = new HttpGet(mRequestUrl);
					mHttpResponse = mAndroidHttpClient.execute(mHttpGet);
					Header[] header = mHttpResponse.getHeaders(CONTENT_LENGTH);
					InputStream is = mHttpResponse.getEntity().getContent();
					byte[] b = new byte[Integer.valueOf(header[0].getValue())];
					int readsize = 0;
					while(readsize<b.length){
						readsize += is.read(b, readsize, b.length-readsize);
					}
					return new String(b,"gbk");
//					if(mHttpResponse.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
//						return null;
//					}
//					return EntityUtils.toString(mHttpResponse.getEntity());
				}
			}catch (Exception e) {
				// TODO: handle exception
				Log.e("HttpRequestFactory", "HttpRequestFactory error!!!");
			}finally {
				mAndroidHttpClient.close();
			}
			return null;
		}
	}
	

}
