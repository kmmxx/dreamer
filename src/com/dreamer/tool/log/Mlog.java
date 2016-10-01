package com.dreamer.tool.log;

import android.util.Log;

public class Mlog {

	private static String special = "kemm";
	private static boolean V = false;
	private static boolean D = true;
	private static boolean I = false;
	private static boolean W = true;
	private static boolean E = true;
	private static String seprateor = "->";

	public static void v(String tag, String debugInfo) {
		if (V) {
			Log.v(tag, special + seprateor + tag + seprateor + debugInfo);
		}
	}

	public static void d(String tag, String debugInfo) {
		if (D) {
			Log.d(tag, special + seprateor + tag + seprateor + debugInfo);
		}
	}

	public static void i(String tag, String debugInfo) {
		if (I) {
			Log.i(tag, special + seprateor + tag + seprateor + debugInfo);
		}
	}

	public static void w(String tag, String debugInfo) {
		if (W) {
			Log.w(tag, special + seprateor + tag + seprateor + debugInfo);
		}
	}

	public static void e(String tag, String debugInfo) {
		if (E) {
			Log.e(tag, special + seprateor + tag + seprateor + debugInfo);
		}
	}

	public static void v(String debugInfo) {
		if (V) {
			Log.v("", special + seprateor + debugInfo);
		}
	}

	public static void d(String debugInfo) {
		if (D) {
			Log.d("", special + seprateor + debugInfo);
		}
	}

	public static void i(String debugInfo) {
		if (I) {
			Log.i("", special + seprateor + debugInfo);
		}
	}

	public static void w(String debugInfo) {
		if (W) {
			Log.w("", special + seprateor + debugInfo);
		}
	}

	public static void e(String debugInfo) {
		if (E) {
			Log.e("", special + seprateor + debugInfo);
		}
	}

	public static String getSpecial() {
		return special;
	}

	public static void setSpecial(String special) {
		Mlog.special = special;
	}

	public static boolean isV() {
		return V;
	}

	public static void setV(boolean v) {
		V = v;
	}

	public static boolean isD() {
		return D;
	}

	public static void setD(boolean d) {
		D = d;
	}

	public static boolean isI() {
		return I;
	}

	public static void setI(boolean i) {
		I = i;
	}

	public static boolean isW() {
		return W;
	}

	public static void setW(boolean w) {
		W = w;
	}

	public static boolean isE() {
		return E;
	}

	public static void setE(boolean e) {
		E = e;
	}

	public static void setDebugs(boolean b) {
		if (b) {
			V = true;
			D = true;
			I = true;
			W = true;
			E = true;
		} else {
			V = false;
			D = false;
			I = false;
			W = false;
			E = false;
		}
	}

	public static String getSeprateor() {
		return seprateor;
	}

	public static void setSeprateor(String seprateor) {
		Mlog.seprateor = seprateor;
	}

}
