package com.dreamer.tool.system;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class provides different system informations. It includes:
 * 平台类型，机型，SDK版本，系统类型，IMEI号，硬件制�?�商，屏幕大小，系统语言类型
 * 
 * @author DJ
 * @Date 2012-02-28
 */
@SuppressLint("DefaultLocale")
public class SystemInfoUtil {
	
	public static final String SYSTEM_NAME = "Android";

	private static final Pattern MTK_PATTERN = Pattern
			.compile("^[MT]{2}[a-zA-Z0-9]{0,10}$");

	/**
	 * <p>
	 * 平台类型
	 * </p>
	 */
	private static int STATISTICS_PLATFORM_MTK = 1;
	private static int STATISTICS_PLATFORM_QUALCOMM = 2;
	
	public static boolean hasSystemFeature(Context context ,String feature){
		PackageManager packageManager = context.getPackageManager();
        if (null == packageManager) {
            return false;
        }
        return  packageManager.hasSystemFeature(feature);
	}

	/**
	 * <p>
	 * 获取机型
	 * </p>
	 */
	public static String getModel() {
		String model = "0";

		if (!isEmpty(Build.MODEL))
			model = Build.MODEL.toUpperCase();
		else {
		}

		return model;
	}
	
	/**
	 * <p>
	 * 获取平台类型
	 * </p>
	 */
	public static int getPlatForm() {
		if (getHardware().equals("QCOM"))
			return STATISTICS_PLATFORM_QUALCOMM;
		else if (MTK_PATTERN.matcher(getHardware()).find()) {
			return STATISTICS_PLATFORM_MTK;
		} else {
			return 0;
		}
	}
	
	/**
	 * <p>
	 * 获得硬件制�?�商
	 * </p>
	 */
	public static String getHardware() {
		if (!isEmpty(Build.HARDWARE))
			return Build.HARDWARE.toUpperCase();
		else {
			return "0";
		}
	}

	/**
	 * <p>
	 * 获取SDK版本
	 * </p>
	 */
	public static int getSDKVersion() {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * <p>
	 * 获取系统类型
	 * </p>
	 */
	public static String getRomVersion() {
		String romVersion = "0";

		if (!isEmpty(Build.VERSION.RELEASE))
			romVersion = Build.VERSION.RELEASE.toUpperCase();
		else {
		}

		return romVersion;
	}

	/**
	 * <p>
	 * 获取IMEI�?
	 * </p>
	 */
	public static String getImei(Context context) {
		String imei = "0";
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
			if (isEmpty(imei)) {
				imei = "0";
			}
		} catch (Exception e) {
		}
		return imei;
	}

	/**
	 * <p>
	 * 获取手机号码
	 * </p>
	 */
	public static String getLocalPhoneNO(Context context) {
		String phoneNo = "0";

		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (!isEmpty(tm.getLine1Number()))
				phoneNo = tm.getLine1Number();
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}

		return phoneNo;
	}

	/**
	 * <p>
	 * 获取运营商名�?
	 * </p>
	 */
	public static String getOperator(Context context) {
		String operator = "";

		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			operator = tm.getNetworkOperatorName();
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}

		return operator;
	}

	public static int getOperatorId(Context context) {
		String operator = getOperator(context);
		operator = operator.toLowerCase();
		int id = 99;
		if (operator.equals("中国移动") || operator.equals("china mobile")
				|| operator.equals("chinamobile")) {
			id = 0;
		} else if (operator.equals("中国联�??") || operator.equals("china unicom")
				|| operator.equals("chinaunicom")) {
			id = 1;
		} else if (operator.equals("中国电信") || operator.equals("china net")
				|| operator.equals("chinanet")) {
			id = 2;
		}
		return id;
	}

	/**
	 * <p>
	 * 获取MAC地址
	 * </p>
	 */
	public static String getMacAddress(Context context) {
		String macAddress = "0";

		try {
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();

			if (!isEmpty(info.getMacAddress()))
				macAddress = info.getMacAddress();
			else {
				LogUtil.w(ConstantsUtil.LOG_TAG, "NO MAC ADDRESS.");
			}
		} catch (Exception e) {
			LogUtil.e(ConstantsUtil.LOG_TAG, e);
		}

		return macAddress;
	}

	/**
	 * Get the phone mobile.
	 * 
	 * @author dingji
	 * @param context
	 * @return
	 */
	public static String getMobile(Context context) {
		String result = "0";
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.hasIccCard()) {
			result = tm.getLine1Number();
			if (TextUtils.isEmpty(result) || result.equals("null")) {
				result = "0";
			}
		}
		return result;
	}

	private static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
}
