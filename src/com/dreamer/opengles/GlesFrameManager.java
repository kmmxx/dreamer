package com.dreamer.opengles;

import java.util.HashMap;

import com.dreamer.opengles.GlesSurfaceView.GLES2SurfaceViewCallback;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class GlesFrameManager {

	private static GlesFrameManager mGlesFrameManager;
	private Activity mActivity;
	private GlesSurfaceView mGlesSurfaceView;
	private Handler mHandler;
	private MGlesFrame currentFrame;
	private HashMap<String, MGlesFrame> frameMaps;

	private String TAG = GlesFrameManager.class.getSimpleName();

	public static GlesFrameManager getInstance() {
		if (mGlesFrameManager == null) {
			mGlesFrameManager = new GlesFrameManager();
		}
		return mGlesFrameManager;
	}

	GlesFrameManager() {
		frameMaps = new HashMap<String, MGlesFrame>();
	}

	public void prepare(Activity activity, ViewGroup layout, Handler mHandler) {
		this.mActivity = activity;
		this.mHandler = mHandler;
		// GlesSurfaceView.setRGBA(0.5f, 0.5f, 0.5f, 1.0f);
		mGlesSurfaceView = new GlesSurfaceView(mActivity);
		mGlesSurfaceView.setFocusable(true);
		mGlesSurfaceView.setFocusableInTouchMode(true);
		mGlesSurfaceView.requestFocus();
		layout.addView(mGlesSurfaceView);
	}

	public void requestFocus() {
		mGlesSurfaceView.setFocusable(true);
		mGlesSurfaceView.setFocusableInTouchMode(true);
		mGlesSurfaceView.requestFocus();
		// mGlesSurfaceView.getGLES2Frame().requestFocus();
	}

	public void setRenderMode(int b) {
		if (mGlesSurfaceView == null)
			return;
		mGlesSurfaceView.setRenderMode(b);
	}

	public void initGlesSurfaceView(final String frameName) {
		mGlesSurfaceView
				.setGLES2SurfaceViewCallback(new GLES2SurfaceViewCallback() {

					private int count = 0;

					@Override
					public void surfaceDrawFrameFPS(int fps) {
						// TODO Auto-generated method stub
						if (count++ > 10) {
							count = 0;
							Log.d(TAG, "surfaceDrawFrameFPS:" + fps);
						}
					}

					@Override
					public void initGlSurfaceWindow() {
						// TODO Auto-generated method stub
						Log.d(TAG, "initGlSurfaceWindow");
						dispatchGlesFrame(frameName);
					}
				});
	}

	public synchronized void dispatchGlesFrame(final String cmd) {
		mHandler.post(new Runnable() {
			public void run() {
				setGlFrame(cmd);
			}
		});
	}

	public synchronized void dispatchGlesFrame(final String frame,
			final int view) {
		mHandler.post(new Runnable() {
			public void run() {
				setGlFrame(frame, view);
			}
		});
	}

	public MGlesFrame getCurrentFrame() {
		return currentFrame;
	}

	public void addMGlesFrame(String frameName, MGlesFrame frame) {
		if (frameName == null || frameName.equals(""))
			return;
		frameMaps.put(frameName, frame);
	}

	public MGlesFrame getMGlesFrame(String frameName) {
		return frameMaps.get(frameName);
	}

	private void setGlFrame(String cmd) {
		// TODO Auto-generated method stub
		if (frameMaps.containsKey(cmd)) {
			currentFrame = frameMaps.get(cmd);
		}
		mGlesSurfaceView.setGLES2Frame(currentFrame);
		currentFrame.setDefaultFocusable(0);
	}

	private void setGlFrame(String cmd, int view) {
		// TODO Auto-generated method stub
		if (frameMaps.containsKey(cmd)) {
			currentFrame = frameMaps.get(cmd);
		}
		mGlesSurfaceView.setGLES2Frame(currentFrame);
		currentFrame.setDefaultFocusable(view);
	}

	public void onPause() {
		// TODO Auto-generated method stub
		if (mGlesSurfaceView != null) {
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
			// mGlesSurfaceView.onPause();
		}
	}

	public void onResume() {
		if (mGlesSurfaceView != null) {
			setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
			// mGlesSurfaceView.onResume();
		}
	}

	public void onDestroy() {
		if (mGlesSurfaceView != null) {
			mGlesSurfaceView.onDestroy();
		}
	}

}
