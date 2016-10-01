package com.dreamer.surfaceview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;

public abstract class SVFrame extends SVObject {

	private SVObject mFocusObject;
	private SVObject mLostFocusObject;
	private List<SVObject> mSVObjects;

	SVImageView mSVBackground;
	SurfaceViewWindow mSurfaceViewWindow;

	public SVFrame() {
		mSVObjects = new ArrayList<SVObject>();
		setBound(0, 0, SurfaceViewWindow.SV_WIDTH, SurfaceViewWindow.SV_HEIGHT);
		mSVBackground = new SVImageView();
	}
	
	public void setBackGround(SVImageView view){
		this.mSVBackground = view;
		mSVBackground.setXYWH(x, y, w, h);
	}


	public SurfaceViewWindow getSurfaceViewWindow() {
		return mSurfaceViewWindow;
	}

	public void addView(SVObject mSVObject) {
		synchronized (mutex) {
			if (mSVObject instanceof SVFrame) {
				return;
			}
			mSVObject.mSVFrame = this;
			mSVObjects.add(mSVObject);
		}
	}

	public final void remove(SVObject mSVObject) {
		if (mSVObject == null)
			return;
		synchronized (mutex) {
			if (mSVObjects.contains(mSVObject)) {
				mSVObject.mSVFrame = null;
				mSVObjects.remove(mSVObject);
			}
		}
	}

	public final int count() {
		synchronized (mutex) {
			return mSVObjects.size();
		}
	}

	public boolean onDraw(Canvas mCanvas) {
		mSVBackground.onDraw(mCanvas);
		return onDrawChilds(mCanvas);
	}

	protected boolean onDrawChilds(Canvas mCanvas) {

		synchronized (mutex) {
			Iterator<SVObject> it = mSVObjects.iterator();
			boolean end = false;
			while (it.hasNext()) {
				SVObject mSVObject = it.next();
				if (mSVObject == null || !mSVObject.isVisible())
					continue;
				if (mSVObject.onDraw(mCanvas))
					end = true;
			}
			return end;
		}
	}

	@Override
	protected boolean dispatchKeyEvent(KeyEvent event) {
		if (mFocusObject != null) {
			if (mFocusObject.dispatchKeyEvent(event)) {
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected boolean onKeyEvent(KeyEvent event) {
		if (event == null)
			return false;
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_BACK:
				if (mSurfaceViewWindow == null)
					return false;
				mSurfaceViewWindow.finish();
				return true;
			}
		}
		return false;
	}

	protected final boolean dispatchRequestFocus(SVObject view, boolean focus) {
		if (view == null)
			return false;
		if (mSVObjects.contains(view)) {
			if (focus) {
//				FocusRunnable focusEvent = new FocusRunnable();
				if (mFocusObject != null) {
					mFocusObject.focus = false;
//					mLostFocusObject = mFocusObject;
					mFocusObject = null;
				}
				mFocusObject = view;
//				focusEvent.mGainFocusObject = mFocusObject;
//				mSurfaceViewWindow.queueEvent(focusEvent);
			} else {
				if (mLostFocusObject != null) {
					mLostFocusObject = null;
//					FocusRunnable focusEvent = new FocusRunnable();
//					focusEvent.mLostFocusObject = mFocusObject;
//					mSurfaceViewWindow.queueEvent(focusEvent);
				}
				mLostFocusObject = view;
			}
			return focus;
		}
		return false;
	}

	class FocusRunnable implements Runnable {

		SVObject mLostFocusObject;
		SVObject mGainFocusObject;

		public void run() {
			if (mLostFocusObject != null) {
				mLostFocusObject.dispatchFocusEvent(mLostFocusObject, false);
			}
			if (mGainFocusObject != null) {
				mGainFocusObject.dispatchFocusEvent(mGainFocusObject, true);
			}
		}
	}

	public boolean setFinalFrameFocus(SVObject svObject) {
		// TODO Auto-generated method stub
		if (svObject == null)
			return false;
		else {
			this.mFocusObject = svObject;
			return true;
		}

	}

	public void setSurfaceViewWindow(SurfaceViewWindow surfaceViewWindow) {
		// TODO Auto-generated method stub
		this.mSurfaceViewWindow = surfaceViewWindow;
	}
	
	protected abstract void setFrameFocus(int area);

}
