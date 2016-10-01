package com.dreamer.tool.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * This class is used to provide different net informations, which is include
 * upload condition, net type and so on.
 * 
 * @author DJ
 * @Date 2012-02-14
 */
@SuppressLint("DefaultLocale")
public class NetInfoUtil {
	/**
	 * <p>
	 * Weather it has "WIFI" to upload the statistics information.
	 * </p>
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifiNetWork(Context context) {
		boolean result = false;

		try {
			String netType = getNetType(context);
			if (netType.equals("WIFI")) {
				result = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return result;
	}

	/**
	 * <p>
	 * Weather it has "3G" to upload the statistics information.
	 * </p>
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean is3GNetwork(Context context) {
		boolean result = false;

		try {
			String typeName = getNetType(context);
			if (typeName.equals("3GNET") || typeName.equals("3GWAP")) {
				result = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return result;
	}

	/**
	 * <p>
	 * Weather it has "2G" to upload the statistics information.
	 * </p>
	 * 
	 * @param context
	 * @return boolean
	 */

	public static boolean is2GNetwork(Context context) {
		boolean result = false;
		try {
			String netType = getNetType(context);
			if (netType.equals("UNINET") || netType.equals("UNIWAP")
					|| netType.equals("CMNET") || netType.equals("CMWAP")
					|| netType.equals("CTNET") || netType.equals("CTWAP")) {
				result = true;
			}
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}
		return result;
	}

	/**
	 * <p>
	 * Weather it has network to upload the statistics information.
	 * </p>
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isConnectNet(Context context) {
		boolean result = false;

		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (null != info) {
				result = info.isConnected();
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return result;
	}

	/**
	 * <p>
	 * Get the ways of connecting net.
	 * </p>
	 * 
	 * @param context
	 * @return String
	 */
	@SuppressLint("DefaultLocale")
	public static String getNetType(Context context) {
		String netType = "0";

		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (null != info) {
				netType = info.getTypeName().toUpperCase(); // WIFI/MOBILE
				if (netType.equals("MOBILE")) {
					String extraInfo = info.getExtraInfo();
					if (!TextUtils.isEmpty(extraInfo)) {
						netType = extraInfo.toUpperCase();
					}
				}
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return netType;
	}

	/**
	 * Get the net-type and return the net-type-code.
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetTypeId(Context context) {
		try {
			String typeName = getNetType(context);

			if (typeName.equals("3GNET"))
				return 3;
			else if (typeName.equals("3GWAP"))
				return 4;
			else if (typeName.equals("UNINET"))
				return 5;
			else if (typeName.equals("UNIWAP"))
				return 6;
			else if (typeName.equals("CMNET"))
				return 7;
			else if (typeName.equals("CMWAP"))
				return 8;
			else if (typeName.equals("CTNET"))
				return 9;
			else if (typeName.equals("CTWAP"))
				return 10;
			else if (typeName.equals("WIFI"))
				return 2;
			else
				return 0;
		} catch (Exception e) {
			LogUtil.e(e);
			return 0;
		}
	}
	
	public static boolean isWapNet(Context context) {
		String netType = getNetType(context);
		if (netType.equals("CMWAP") || netType.equals("3GWAP")
				|| netType.equals("UNIWAP")) {
			return true;
		}
		return false;
	}
}
