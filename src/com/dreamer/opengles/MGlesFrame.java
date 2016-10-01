package com.dreamer.opengles;

import android.app.Activity;

public abstract class MGlesFrame extends GlesFrame {

	protected Activity mActivity;

	protected MGlesFrame(Activity activity) {
		mActivity = activity;
	}

	public void onDestroy() {
	}

	public abstract void setDefaultFocusable(int area);

}
