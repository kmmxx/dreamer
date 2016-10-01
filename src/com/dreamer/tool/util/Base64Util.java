package com.dreamer.tool.util;

import android.util.Base64;
/**
 * Class help to do the encode and decode handle by Base64 arithmetic.
 * 
 * @Author	Ding Ji
 * @Since	2013-5-17
 */
public class Base64Util {
	public static String base64Encode(String str) {
		String encode = "";
		try {
			encode = new String(Base64.encode(str.getBytes(), Base64.DEFAULT),
					"UTF-8");
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}
		return encode;
	}

	public static String base64Decode(String str) {
		String decode = "";
		try {
			decode = new String(Base64.decode(str, Base64.DEFAULT), "UTF-8");
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}
		return decode;
	}
}
