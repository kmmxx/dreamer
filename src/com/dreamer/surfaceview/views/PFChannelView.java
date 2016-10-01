package com.dreamer.surfaceview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;

import com.dreamer.R;
import com.dreamer.surfaceview.SVImageView;
import com.dreamer.surfaceview.SVObject;

public class PFChannelView extends SVObject{
	
	private Context mContext;
	private SVImageView mSVImageView;
	
	public PFChannelView(Context ctx){
		mContext = ctx;
		initView();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		mSVImageView = new SVImageView();
		mSVImageView.setBackground(mContext, R.drawable.ic_launcher);
	}

	public boolean onDraw(Canvas canvas){
		for(int i = 0;i<100;i++){
			mSVImageView.setXY(i*30+ i, i*7);
			mSVImageView.onDraw(canvas);
		}
		
		return true;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("PFView", "onKeyUp");
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			setVisible(false);
			((SurfaceViewActivity)mContext).getSVFrame().setFrameFocus(MainFrame.MAINVIEW);
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			return true;
		case KeyEvent.KEYCODE_BACK:
//			setVisible(false);
			return true;
		default:
			return true;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		Log.e("PFView", "onKeyDown");
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return true;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_BACK:
//			setVisible(false);
			return true;
		}
		return true;
	}
	
	protected boolean onKeyEvent(KeyEvent event) {
		Log.e("PFView", "onKeyEvent");
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			return onKeyDown(event.getKeyCode(), event);
		case KeyEvent.ACTION_UP:
			return onKeyUp(event.getKeyCode(), event);
		default:
			return false;
		}
	}
}
