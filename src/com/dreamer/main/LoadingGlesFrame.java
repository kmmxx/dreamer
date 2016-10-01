package com.dreamer.main;

import com.dreamer.opengles.GlesAnimation;
import com.dreamer.opengles.MGlesFrame;
import com.dreamer.tool.log.Mlog;

import android.app.Activity;

public class LoadingGlesFrame extends MGlesFrame {

	public static final int DEFAULT = 0;
	public static final int MAIN = 1;
	
	private String TAG = LoadingGlesFrame.class.getSimpleName();
	private LoadingView mLoadingView;
	private GlesAnimation viewAnim;

	public LoadingGlesFrame(Activity mActivity) {
		// TODO Auto-generated constructor stub
		super(mActivity);
		mLoadingView = new LoadingView((MainActivity)mActivity);
		viewAnim = new GlesAnimation();
		mLoadingView.setGlesAnimation(viewAnim);
		mLoadingView.setVisible(true);
		addView(mLoadingView);
	}
	
	public LoadingView getLoadingView(){
		return mLoadingView;
	}
	
	@Override
	public void setDefaultFocusable(int area) {
		// TODO Auto-generated method stub
		Mlog.d(TAG, "area:" + area);
		switch (area) {
		case DEFAULT:
			mLoadingView.setVisible(true);
			mLoadingView.requestFocus();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

}
