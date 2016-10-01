package com.dreamer.surfaceview;


import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;


public abstract class SVObject  {

	protected Object mutex = new Object();
	protected boolean visible = true;
	protected boolean focus = false;

	protected SVFrame mSVFrame;
	protected OnKeyListener mOnKeyListener;

	protected int x;
	protected int y;
	protected int w;
	protected int h;
	
	public void setVisible(boolean v) {
		visible = v;
	}

	public final boolean isVisible() {
		return visible;
	}
	

	public boolean hasFocus() {
		return focus;
	}
	
	public void setBound(int x,int y,int w,int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void setOnKeyListener(OnKeyListener listener) {
		mOnKeyListener = listener;
	}
	
	public boolean onDraw(Canvas mCanvas){
		return true;
	}

	public boolean requestFocus() {
		
		if (focus || mSVFrame == null) 
			return focus;
		return mSVFrame.dispatchRequestFocus(this, true);
	}
	
	protected void dispatchFocusEvent(SVObject object, boolean focus) {
		if (object != this)
			return;
		this.focus = focus;
	}
	
	protected boolean dispatchKeyEvent(KeyEvent event) {
		if (event == null) return false;
		if (mOnKeyListener != null) {
			boolean b = mOnKeyListener.onKeyEvent(this, event);
			if (b) return b;
		}
		return onKeyEvent(event);
	}


	protected boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("SVObject", "dispatchKeyEvent4");
		return false;
	}

	protected boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("SVObject", "dispatchKeyEvent5");
		return false;
	}

	protected boolean onKeyEvent(KeyEvent event) {
		Log.e("SVObject", "dispatchKeyEvent3");
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			return onKeyDown(event.getKeyCode(), event);
		case KeyEvent.ACTION_UP:
			return onKeyUp(event.getKeyCode(), event);
		default:
			return false;
		}
	}
	
	public interface OnKeyListener {
		public boolean onKeyEvent(SVObject view, KeyEvent event);
	}


}
