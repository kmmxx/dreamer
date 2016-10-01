package com.dreamer.main;

import com.dreamer.R;
import com.dreamer.tool.download.DownLoadCallback;
import com.dreamer.tool.download.DownloadManager;
import com.dreamer.tool.file.SharedPreferencesManager;
import com.dreamer.tool.http.AsyncHttpClient;
import com.dreamer.tool.http.AsyncHttpResponseHandler;
import com.dreamer.tool.http.RequestParams;
import com.dreamer.tool.http.SyncHttpClient;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HttpActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private LinearLayout layout;
	private Bitmap bitmap;
	private WebView webView;
	private String URL = "http://www.baidu.com/";
	private SyncHttpClient syncHttpClient;
	private AsyncHttpClient asyncHttpClient;
	private String content;
	private DownloadManager mDownloadManager;
	private TextView textView;
	private TextView textView1;
	private String url0 = "http://img.yingyonghui.com/apk/16457/com.rovio.angrybirdsspace.ads.1332528395706.apk";
	private String url1 = "http://192.168.1.100:8080/hlsPlayer.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.http);
		webView = (WebView) findViewById(R.id.web);
		textView = (TextView) findViewById(R.id.text);
		textView1 = (TextView) findViewById(R.id.text1);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		syncHttpClient = new SyncHttpClient();
		asyncHttpClient = new AsyncHttpClient();
		SharedPreferencesManager.getInstance().prepare(this);
		mDownloadManager = DownloadManager.getDownloadManager(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// synGet();
				// synPost();
				// asynPost();
			}
		}).start();
		startCom(url0);
		startCom(url1);
	}

	private void startCom(String url) {
		// TODO Auto-generated method stub
		mDownloadManager.addHandler(url);
		mDownloadManager.setDownLoadCallback(new DownLoadCallback() {

			@Override
			public void onLoading(String url, long totalSize, long currentSize,
					long speed) {
				// TODO Auto-generated method stub
				super.onLoading(url, totalSize, currentSize, speed);
				long downloadPercent = currentSize * 100 / totalSize;
				if (url.equals(url0)) {
					textView.setText("percent:" + downloadPercent + "--------"
							+ speed + "kbps");
				} else {
					textView1.setText("percent:" + downloadPercent + "--------"
							+ speed + "kbps");
				}

			}

			@Override
			public void onSuccess(String url) {
				// TODO Auto-generated method stub
				super.onSuccess(url);
				if (url.equals(url0)) {
					textView.setText("下载成功了！");
				} else {
					textView1.setText("下载成功了！");
				}
			}

			@Override
			public void onFinish(String url) {
				// TODO Auto-generated method stub
				super.onFinish(url);
			}

			@Override
			public void onAdd(String url, Boolean isInterrupt) {
				// TODO Auto-generated method stub
				super.onAdd(url, isInterrupt);
			}

		});
	}

	private void stopCom() {
		// TODO Auto-generated method stub
		mDownloadManager
				.pauseHandler("http://img.yingyonghui.com/apk/16457/com.rovio.angrybirdsspace.ads.1332528395706.apk");
	}

	private void synGet() {
		content = syncHttpClient.get(URL);
		HttpActivity.this.content = content;
		mHandler.sendEmptyMessage(0);
		showWebView(content);
	}

	private void asynGet() {
		asyncHttpClient.get(URL, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				Log.d(TAG, "content:" + content);
				HttpActivity.this.content = content;
				mHandler.sendEmptyMessage(0);
				showWebView(content);
			}
		});

	}

	private void synPost() {
		RequestParams params = new RequestParams();
		params.put("username", "white_cat");
		params.put("password", "123456");
		params.put("email", "2640017581@qq.com");
		// params.put("profile_picture", new File("/mnt/sdcard/testpic.jpg"));
		// // 上传文件
		// params.put("profile_picture2", inputStream); // 上传数据流
		// params.put("profile_picture3", new ByteArrayInputStream(bytes)); //
		// 提交字节流
		String content = syncHttpClient.post("http://www.baidu.com/");
		HttpActivity.this.content = content;
		mHandler.sendEmptyMessage(0);
		showWebView(content);
	}

	private void asynPost() {
		RequestParams params = new RequestParams();
		params.put("username", "white_cat");
		params.put("password", "123456");
		params.put("email", "2640017581@qq.com");
		// params.put("profile_picture", new File("/mnt/sdcard/testpic.jpg"));
		// // 上传文件
		// params.put("profile_picture2", inputStream); // 上传数据流
		// params.put("profile_picture3", new ByteArrayInputStream(bytes)); //
		// 提交字节流
		asyncHttpClient.post(URL, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				// TODO Auto-generated method stub
				super.onSuccess(content);
				showWebView(content);
			}
		});
	}

	private void showWebView(String content) {
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(HttpActivity.this,
						"goto URL:" + URL + "content:" + content,
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
	};

	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

}
