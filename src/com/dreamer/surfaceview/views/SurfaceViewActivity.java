package com.dreamer.surfaceview.views;

import com.dreamer.R;
import com.dreamer.surfaceview.SVFrame;
import com.dreamer.surfaceview.SVImageView;
import com.dreamer.surfaceview.SurfaceViewWindow;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

public class SurfaceViewActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private LinearLayout layout;
	private SurfaceViewWindow mSurfaceViewWindow;
	private MainFrame mSVFrame;
	private MainView mMainView;
	private PFChannelView mPFView;
	public static final String FRAME_MAIN = "main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mSurfaceViewWindow = new SurfaceViewWindow(this);
		mSurfaceViewWindow.setFocusable(true);
		mSurfaceViewWindow.requestFocus();
		layout = (LinearLayout) findViewById(R.id.glview);
		layout.addView(mSurfaceViewWindow);
		Log.e(TAG, "onCreate");
		
		mSVFrame = new MainFrame(this);
		mSVFrame.setFrameFocus(MainFrame.MAINVIEW);
		mSurfaceViewWindow.setFrame(mSVFrame);
		mSurfaceViewWindow
				.setSurfaceViewWindowCallback(new SurfaceViewWindow.SurfaceViewWindowCallback() {

					@Override
					public void notifySurfaceViewWindowCallback() {
						// TODO Auto-generated method stub
						AlphaAnimation mAnimationAlpha = new AlphaAnimation(1f,
								0f);
						mAnimationAlpha.setDuration(5000l);
						mSurfaceViewWindow.startAnimation(mAnimationAlpha);
					}
				});

	}
	
	public MainFrame getSVFrame(){
		return mSVFrame;
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	};

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
	};

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODOAuto-generated method stub 
		Log.e("SurfaceViewActivity", "onKeyDown");
		mSurfaceViewWindow.onKeyDown(keyCode, event);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODOAuto-generated method stub 
		Log.e("SurfaceViewActivity","onKeyUp");
		mSurfaceViewWindow.onKeyUp(keyCode, event);
		return true;
	}

}
