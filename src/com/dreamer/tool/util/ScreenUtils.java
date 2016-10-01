package com.dreamer.tool.util;

import android.view.View;

public class ScreenUtils {

	public static int getLeftInWindow(View v) {
		View parent = (View) v.getParent();
		if (parent == v.getRootView())
			return v.getLeft();
		return v.getLeft() + getLeftInWindow(parent);
	}

	public static int getTopInWindow(View v) {
		View parent = (View) v.getParent();
		if (parent == v.getRootView())
			return v.getTop();
		return v.getTop() + getTopInWindow(parent);
	}
}
