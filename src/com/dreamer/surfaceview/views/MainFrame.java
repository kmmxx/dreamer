package com.dreamer.surfaceview.views;

import android.app.Activity;

import com.dreamer.R;
import com.dreamer.surfaceview.SVFrame;
import com.dreamer.surfaceview.SVImageView;

public class MainFrame extends SVFrame{
	
	private Activity mActivity;
	private MainView mMainView;
	private PFChannelView mPFView;
	private int area = MAINVIEW;
	public static final int MAINVIEW = 0x0;
	public static final int PFVIEW = 0x1;

	public MainFrame(Activity activity){
		super();
		mMainView = new MainView(activity);
		mPFView = new PFChannelView(activity);
		mPFView.setVisible(true);
		mMainView.setVisible(true);
		addView(mMainView);
		addView(mPFView);
		setBackGround(new SVImageView(SVImageView.getBitmap(activity,R.drawable.ic_launcher)));
		mMainView.requestFocus();
		this.mActivity = activity;
	}

	@Override
	public void setFrameFocus(int area) {
		// TODO Auto-generated method stub
		switch(area){
		case MAINVIEW:
			mMainView.setVisible(true);
			mMainView.requestFocus();
			break;
		case PFVIEW:
			mPFView.setVisible(true);
			mPFView.requestFocus();
			break;
		default:
			break;
		}
	}

	
}
