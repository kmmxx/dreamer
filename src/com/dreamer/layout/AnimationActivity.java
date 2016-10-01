package com.dreamer.layout;

import java.util.ArrayList;

import com.dreamer.R;
import com.dreamer.layout.view.AutoScrollTextView;
import com.dreamer.layout.view.AutoScrollTextView.OnAutoScrollTextCallback;
import com.dreamer.tool.animation.AnimationManager;
import com.dreamer.tool.animation.Rotate3d;
import com.dreamer.tool.download.DownLoadCallback;
import com.dreamer.tool.download.DownloadManager;
import com.dreamer.tool.file.SharedPreferencesManager;
import com.dreamer.tool.http.AsyncHttpClient;
import com.dreamer.tool.http.AsyncHttpResponseHandler;
import com.dreamer.tool.http.RequestParams;
import com.dreamer.tool.http.SyncHttpClient;

import android.R.color;
import android.animation.Keyframe;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AnimationActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private String URL = "http://www.baidu.com/";
	private ImageView img;
	private Vibrator vibrator;
	private AutoScrollTextView scrollTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.animation);
		AnimationManager.getInstance().prepare(this);
		img = (ImageView) findViewById(R.id.img);
		// AnimationManager.startFadeInOutAnimation(this);
		AnimationManager.startSlideInLeftOutRightAnimation(this);
		// AnimationManager.startSlideInRightOutLeftAnimation(this);
		// AnimationManager.startWindowAnimation(this, R.anim.scale_in,
		// R.anim.scale_out);
		// AnimationManager.startWindowAnimation(this, R.anim.zoom_in,
		// R.anim.zoom_out);
		vibrator = AnimationManager.getInstance().startVibrator(null, 2);
		/* marquee */
		// findViewById(R.id.marquee).requestFocus();

		scrollTextView = (AutoScrollTextView) findViewById(R.id.newsview);
		scrollTextView.setTexColor(Color.BLUE);
		scrollTextView.setText("jfosaijfoijsfosjdfsajdf");
		scrollTextView.setOnAutoScrollTextCallback(mOnCallback);
		scrollTextView.startScroll();

		// rotate3dTest();
	}

	OnAutoScrollTextCallback mOnCallback = new OnAutoScrollTextCallback() {
		public void onFinish() {
			// TODO Auto-generated method stub
			scrollTextView.startScroll();
		}
	};
	private int titleIndex;
	private int titleLastIndex;

	public void changeTitleIndex(int i, View focus, float x) {
		titleLastIndex = titleIndex;
		titleIndex += i;
		if (titleIndex < 0) {
			titleIndex = 0;
			return;
		}
		if (titleIndex > 4) {
			titleIndex = 4;
			return;
		}
		focus.clearAnimation();
		if (i > 0) {
			Animation anim1 = AnimationManager.getTranslateAnimation(
					titleLastIndex * x, titleIndex * x, 0, 0, 100);
			anim1.setFillAfter(true);
			focus.startAnimation(anim1);
		} else {
			Animation anim = AnimationManager.getTranslateAnimation(
					titleLastIndex * x, titleIndex * x, 0, 0, 100);
			anim.setFillAfter(true);
			focus.startAnimation(anim);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		// AnimationDrawable anim = AnimationManager.loadAnimationDrawable(
		// (ImageView) findViewById(R.id.img), R.drawable.frame_animation);
		// anim.start();
	};

	private void rotate3dTest() {
		// TODO Auto-generated method stub
		Rotate3d leftAnimation = new Rotate3d(-0, -90, 0, 0, 100, 100);
		Rotate3d rightAnimation = new Rotate3d(-0 + 90, -90 + 90, 0.0f, 0.0f,
				100, 100);

		leftAnimation.setFillAfter(true);
		leftAnimation.setDuration(1000);
		rightAnimation.setFillAfter(true);
		rightAnimation.setDuration(1000);

		findViewById(R.id.img).startAnimation(leftAnimation);
		// findViewById(R.id.image2).startAnimation(rightAnimation);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Animation anim;
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			anim = AnimationManager.getInstance()
					.loadViewAnimation(R.anim.arst);
			anim.setFillAfter(true);
			img.startAnimation(anim);
			break;
		case KeyEvent.KEYCODE_1:
			AnimationManager.startPropertyAnimation(img, "alpha", 3000, .0f,
					3.0f);
			break;
		case KeyEvent.KEYCODE_2:
			anim = AnimationManager.getInstance().loadViewAnimation(
					R.anim.scale_in);
			anim.setFillAfter(true);
			img.startAnimation(anim);
			break;
		case KeyEvent.KEYCODE_3:
			Keyframe[] kfss = new Keyframe[] { Keyframe.ofInt(0, 400),
					Keyframe.ofInt(0.25f, 200), Keyframe.ofInt(0.5f, 400),
					Keyframe.ofInt(0.75f, 100), Keyframe.ofInt(1f, 500) };
			AnimationManager.startPropertykeyFrameAnimation(img, "width", 3000,
					kfss);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			findViewById(R.id.img).requestFocus();
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		AnimationManager.getInstance().stopVibrator(vibrator);
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
