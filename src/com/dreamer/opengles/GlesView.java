package com.dreamer.opengles;

import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GlesView extends Gles {

	Object mutex = new Object();
	GlesFrame mGlesFrame;
	boolean isVisible = false;
	boolean hasFocus = false;
	private OnKeyListener mOnKeyListener;
	private OnFocusChangeListener mOnFocusChangeListener;
	private GlesBaseAdapter mGlesBaseAdapter;

	protected boolean onDraw() {
		onDrawObject();
		return true;
	}

	public void setAdapter(GlesBaseAdapter adapter) {
		mGlesBaseAdapter = adapter;
	}

	public GlesBaseAdapter getGlesBaseAdapter() {
		return mGlesBaseAdapter;
	}

	public boolean requestFocus() {
		if (hasFocus || mGlesFrame == null)
			return hasFocus;
		return mGlesFrame.dispatchRequestFocus(this, true);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isHasFocus() {
		return hasFocus;
	}

	public void setHasFocus(boolean hasFocus) {
		this.hasFocus = hasFocus;
	}

	protected boolean dispatchKeyEvent(KeyEvent event) {
		if (event == null)
			return false;
		if (mOnKeyListener != null) {
			boolean b = mOnKeyListener.onKeyEvent(this, event);
			if (b)
				return b;
		}
		return onKeyEvent(event);
	}

	protected void dispatchFocusEvent(GlesView view, boolean focus) {
		if (view != this)
			return;
		this.hasFocus = focus;
		if (mOnFocusChangeListener != null) {
			mOnFocusChangeListener.onFocusChanged(this, focus);
		}
	}

	protected boolean dispatchTouchEvent(MotionEvent event) {
		if (event == null)
			return false;
		return onTouchEvent(event);
	}

	protected boolean onTouchEvent(MotionEvent event) {
		return false;
	}

	protected boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	protected boolean onKeyUp(int keyCode, KeyEvent event) {
		return false;
	}

	protected boolean onKeyEvent(KeyEvent event) {
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			return onKeyDown(event.getKeyCode(), event);
		case KeyEvent.ACTION_UP:
			return onKeyUp(event.getKeyCode(), event);
		default:
			return false;
		}
	}

	public void setOnKeyListener(OnKeyListener listener) {
		mOnKeyListener = listener;
	}

	public void setOnFocusChangeListener(OnFocusChangeListener listener) {
		mOnFocusChangeListener = listener;
	}

	public interface OnKeyListener {

		public boolean onKeyEvent(GlesView view, KeyEvent event);
	}

	public interface OnFocusChangeListener {

		public void onFocusChanged(GlesView view, boolean focus);
	}

}
