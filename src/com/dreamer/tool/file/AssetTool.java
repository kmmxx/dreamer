package com.dreamer.tool.file;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class AssetTool {

	public static InputStream getAssertInputStream(Context context, String name) {
		try {
			return context.getAssets().open(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
