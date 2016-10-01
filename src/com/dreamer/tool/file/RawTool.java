package com.dreamer.tool.file;

import android.content.Context;

public class RawTool {

	public static String getRawString(Context context, int rawId) {
		return "android.resource://" + context.getPackageName() + "/" + rawId;
	}
}
