package com.dreamer.main;

import java.util.ArrayList;
import java.util.List;

import com.dreamer.R;
import com.dreamer.global.GlobalApp;
import com.dreamer.opengles.GlesFrameManager;
import com.dreamer.opengles.expend.GlesObjectManager;
import com.dreamer.tool.bitmap.BitmapToolkit;
import com.dreamer.tool.message.HandlerManager;
import com.dreamer.tool.message.HandlerManager.HandlerCallback;
import com.dreamer.tool.network.NetworkManager;
import com.dreamer.tool.network.NetworkManager.NetworkObserver;
import com.dreamer.tool.network.NetworkManager.NetworkType;
import com.dreamer.tool.system.LockLayer;
import com.dreamer.tool.util.UtilTools;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private LinearLayout layout;
	private Bitmap bitmap;
	private EditText input;
	private int focus;
	public static MainActivity me;

	// public void onAttachedToWindow() {
	// this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
	// super.onAttachedToWindow();
	// }
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// View lock = View.inflate(this, R.layout.http, null);
		// LockLayer lockLayer = LockLayer.getInstance(this);
		// lockLayer.setLockView(lock);
		// lockLayer.lock();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		layout = (LinearLayout) findViewById(UtilTools.getResoureId(R.id.class,
				"glview"));
		me = this;

		ImageView view = (ImageView) findViewById(R.id.loadingImg);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// view.layout(100, 100, 200, 400);
		// bitmap = BitmapFactory.decodeResource(getResources(),
		// UtilTools.getResoureId(R.drawable.class, "loading"));
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.apploadimg);
//		view.setBackgroundResource(R.drawable.apploadimg);
		
		// view.setBackgroundDrawable(BitmapToolkit.toGray(getResources()
		// .getDrawable(R.drawable.ic_launcher)));
		view.setImageBitmap(BitmapToolkit.toGrayscale(BitmapToolkit
				.createBitmap(this, R.drawable.ic_launcher)));
		// layout.addView(view);

		// initNetworkStatus();
		intiOpenglesFrame();

		HandlerManager.getInstance().prepare(this);

		HandlerManager.getInstance().getHandler(this, "main")
				.setHandlerCallback(new HandlerCallback() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						switch (msg.what) {
						case 0:
							focus = 1;
							input.setVisibility(View.VISIBLE);
							input.requestFocus();
							break;
						case 1:
							focus = 0;
							input.setVisibility(View.INVISIBLE);
							layout.requestFocus();
							// GlesFrameManager.getInstance().requestFocus();
							break;
						default:
							break;
						}
					}
				});
		input = (EditText) findViewById(R.id.editText);

	}

	public void inputRequestFocus() {
		HandlerManager.getInstance().getHandler(this, "main")
				.sendEmptyMessage(0);
		// mHandler.sendMessage(mHandler.obtainMessage(0));
	}

	public void glRequestFocus() {
		HandlerManager.getInstance().getHandler(this, "main")
				.sendEmptyMessage(1);
	}

	public int getFocus() {
		return focus;
	}

	private void intiOpenglesFrame() {
		// TODO Auto-generated method stub
		GlesObjectManager.getInstance().prepare(this);
		GlesFrameManager.getInstance().prepare(this, layout, mHandler);
		GlesFrameManager.getInstance().addMGlesFrame("loading",
				new LoadingGlesFrame(this));
		GlesFrameManager.getInstance().initGlesSurfaceView("loading");
	}

	private void initNetworkStatus() {
		// TODO Auto-generated method stub
		NetworkManager.getInstance().prepare(this);
		NetworkManager.getInstance().registerNetworkReceiver();
		NetworkManager.getInstance().regeistNetworkObserver(
				new NetworkObserver() {

					@Override
					public void onDisconnect() {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "onDisconnect fail",
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onConnect(NetworkType networkType) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "onConnect success ",
								Toast.LENGTH_LONG).show();
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		GlobalApp.threadRun = true;
		GlesFrameManager.getInstance().onResume();
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_BACK && focus == 1) {
			glRequestFocus();
		}
		return true;

	}

	@Override
	public boolean onKeyUp(int keycode, KeyEvent event) {
		return true;

	}

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
		GlesFrameManager.getInstance().onPause();
		NetworkManager.getInstance().unRegisterNetworkReceiver();
	};

	@Override
	protected void onStop() {
		super.onStop();
		GlobalApp.threadRun = false;
		Log.e(TAG, "onStop");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
		GlesFrameManager.getInstance().onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());

	}
}
