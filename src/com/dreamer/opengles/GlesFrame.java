package com.dreamer.opengles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.view.KeyEvent;
import android.view.MotionEvent;

public class GlesFrame extends GlesView {

	List<GlesView> mGlesViews;
	GlesSurfaceView mGlesSurfaceView;
	GlesView mFocusGlesView;
	GlesRectangle bg;

	private String TAG = GlesFrame.class.getSimpleName();

	public GlesFrame() {
		mGlesViews = new ArrayList<GlesView>();
	}

	public void setBackGround(GlesRectangle bg) {
		// TODO Auto-generated method stub
		this.bg = bg;
	}

	@Override
	public final void setVisible(boolean b) {
	}

	public void addView(GlesView view) {
		if (view == null)
			return;
		synchronized (mutex) {
			if (view instanceof GlesFrame) {
				return;
			}
			view.mGlesFrame = this;
			mGlesViews.add(view);
		}
	}

	public void addView(GlesView view, int i) {
		if (view == null)
			return;
		synchronized (mutex) {
			if (view instanceof GlesFrame) {
				return;
			}
			view.mGlesFrame = this;
			mGlesViews.add(i, view);
		}
	}

	public void removeView(GlesView view) {
		if (view == null)
			return;
		synchronized (mutex) {
			if (mGlesViews.contains(view)) {
				view.mGlesFrame = null;
				mGlesViews.remove(view);
			}
		}
	}

	public void removeAll() {
		synchronized (mutex) {
			for (GlesView v : mGlesViews) {
				v.mGlesFrame = null;
				v = null;
			}
			mGlesViews.clear();
		}
	}

	public final int count() {
		synchronized (mutex) {
			return mGlesViews.size();
		}
	}

	@Override
	protected boolean dispatchTouchEvent(MotionEvent event) {
		if (mFocusGlesView != null) {
			if (mFocusGlesView.dispatchTouchEvent(event)) {
				return true;
			}
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	protected boolean dispatchKeyEvent(KeyEvent event) {
		if (mFocusGlesView != null) {
			if (mFocusGlesView.dispatchKeyEvent(event)) {
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public boolean onDraw() {
		// TODO Auto-generated method stub
		synchronized (mutex) {
			if (bg != null) {
				bg.onDraw();
			}
			Iterator<GlesView> it = mGlesViews.iterator();
			boolean end = false;
			while (it.hasNext()) {
				GlesView view = it.next();
				if (view == null || !view.isVisible) {
					continue;
				}
				// try {
				if (view.onDraw())
					end = true;
				// } catch (Exception e) {
				// TODO: handle exception
				// e.printStackTrace();
				// Mlog.e(TAG,
				// "kemm--gl draw frame fatal exception *** ***");
				// }
			}
			return end;
		}
	}

	class FocusRunnable implements Runnable {
		GlesView mGainFocusView;
		GlesView mLostFocusView;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mGainFocusView != null) {
				mGainFocusView.dispatchFocusEvent(mGainFocusView, true);
			}
			if (mLostFocusView != null) {
				mLostFocusView.dispatchFocusEvent(mLostFocusView, false);
			}
		}

	}

	public boolean dispatchRequestFocus(GlesView glesView, boolean b) {
		// TODO Auto-generated method stub
		if (glesView == null)
			return false;
		if (mGlesViews.contains(glesView)) {
			FocusRunnable focusEvent = new FocusRunnable();
			if (b) {
				if (mFocusGlesView != null) {
					focusEvent.mLostFocusView = mFocusGlesView;
					mFocusGlesView = null;
				}
				mFocusGlesView = glesView;
				focusEvent.mGainFocusView = mFocusGlesView;
				mGlesSurfaceView.queueEvent(focusEvent);
			} else {
				if (mFocusGlesView != null) {
					focusEvent.mLostFocusView = mFocusGlesView;
					mFocusGlesView = null;
					mGlesSurfaceView.queueEvent(focusEvent);
				}
			}
		}
		return false;
	}

}
