package com.dreamer.tool.version;

import android.os.Build;

public class VersionTool {

	public boolean isLaterVersion(int v) {
		return Build.VERSION.SDK_INT >= v ? true : false;
	}

}
