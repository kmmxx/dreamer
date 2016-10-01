package com.dreamer.tool.util;

import com.nearme.statistics.rom.business.bean.MobileBean;
import com.nearme.statistics.rom.business.util.ConstUtil;
import com.nearme.statistics.rom.business.util.LogUtil;
import com.nearme.statistics.rom.business.util.PrefUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemProperties;

/**
 * This class is to record the user mobile info,include
 * IMEI,Model,os_version,rom_version, phoneNO and resolution,carrier,net
 * access,location.
 * 
 * @author dingji
 * @date 2013-02-19
 */

public class MobileHelper {
	private static final String TAG = MobileHelper.class.getSimpleName();
	public static final String EXCEPTION = "";

	/**
	 * get the mobile IMEI.
	 * 
	 * @param context
	 */
	public static String getIMEI(Context context) {
		String imei = EXCEPTION;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String str = tm.getDeviceId();
			if (null != str) {
				imei = str;
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return imei;
	}

	/**
	 * get the mobile model.
	 */
	public static String getModel() {
		return SystemProperties.get("ro.product.model", EXCEPTION);
	}

	/**
	 * get the mobile operation system version.
	 * 
	 * @return
	 */
//	public static String getOsVersion() {
//		return SystemProperties.get("ro.build.version.release", EXCEPTION);
//	}
	
	/**
	 * get the mobile rom channel.
	 * 
	 * @return
	 */
	public static String getRomChannel() {
		return SystemProperties.get("ro.build.channel", EXCEPTION);
	}
	
	/**
	 * get the mobile model.
	 */
	public static String getRegion() {
		LogUtil.d("Region====:"+SystemProperties.get("persist.sys.oppo.region", "CN"));
		return SystemProperties.get("persist.sys.oppo.region", "CN");
	}
	
	/**
	 * get the mobile rom author.
	 * 
	 * @return
	 */
	public static String getRomAuthor() {
		return SystemProperties.get("ro.build.author", EXCEPTION);
	}
	
	/**
	 * get the mobile rom version.
	 * 
	 * @return
	 */
	public static String getRomVersion() {
		return SystemProperties.get("ro.build.display.id", EXCEPTION);
	}

	private static final String OPPO_ROM_VERSION = "ro.build.version.opporom";

	public static String getOsVersion() {
		String osVersion = SystemProperties.get(OPPO_ROM_VERSION);
		if (TextUtils.isEmpty(osVersion)) {
			osVersion = "V1.0.0";
		}
		return osVersion;
	}

	/**
	 * get the mobile number.
	 * 
	 * @param context
	 * @return
	 */
	public static String getMobile(Context context) {
		String mobile = EXCEPTION;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String str = tm.getLine1Number();
			if (!TextUtils.isEmpty(str)) {
				mobile = str;
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return mobile;
	}

	/**
	 * get the mobile resolution.
	 * 
	 * @param context
	 * @return
	 */

	public static String getResolution(Context context) {
		String resolution = EXCEPTION;
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(dm);
			int displayWidth = dm.widthPixels;
			int displayHight = dm.heightPixels;
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(displayWidth);
			strBuilder.append("*");
			strBuilder.append(displayHight);
			String str = strBuilder.toString();
			resolution = str;
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return resolution;
	}

	/**
	 * get the mobile network access.
	 * 
	 * @param context
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getAccess(Context context) {
		String access = EXCEPTION;
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			int netType = networkInfo.getType();
			if (netType == ConnectivityManager.TYPE_MOBILE) {
				access = networkInfo.getExtraInfo().toUpperCase();
			} else {
				access = networkInfo.getTypeName().toUpperCase();
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return access;
	}

	/**
	 * get the mobile carrier name.
	 * 
	 * @param context
	 * @return
	 */
	public static String getCarrier(Context context) {
		String carrier = EXCEPTION;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			carrier = tm.getNetworkOperatorName();
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return carrier;
	}

	/**
	 * get the statistics sdk version.
	 * 
	 * @return
	 */
	public static String getSDKVersion(Context context) {
		return ConstUtil.getVersionName(context);
	}

	/**
	 * get the mobile location info.
	 * 
	 * @param context
	 * 
	 * @return
	 */
	public static String getLocation(Context context) {
		String result = EXCEPTION;
		try {
			Location location = null;
			LocationManager lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
			if (null == location) {
				location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (null == location) {
				location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
			if (null != location) {
				StringBuilder strBuilder = new StringBuilder();
				strBuilder.append(location.getLatitude());
				strBuilder.append(",");
				strBuilder.append(location.getLongitude());
				String str = strBuilder.toString();
				str = str.trim();
				result = str;
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		}
		return result;
	}

	/**
	 * Get location stored in the sharedpreferences.
	 */
	public static String getRecentLocation(Context context) {
		String location = getLocation(context);

		SharedPreferences sp = PrefUtil.getSharedPreferences(context);
		if (TextUtils.isEmpty(location)) {
			location = PrefUtil.getPreLocation(sp);
		} else {
			PrefUtil.setPreLocation(sp, location);
		}

		return location;
	}

	/**
	 * Get MobileInfoBean's object what stored the mobile info.
	 * 
	 */
	public static MobileBean getMobileBean(Context context) {
		String location = getRecentLocation(context);
		String access = getAccess(context);
		String carrier = getCarrier(context);
		String imei = getIMEI(context);
		String mobile = getMobile(context);
		String model = getModel();
		String resolution = getResolution(context);
		String romVersion = getRomVersion();
		String osVersion = getOsVersion();
		String sdkVersion = getSDKVersion(context);

		MobileBean mobileInfoBean = new MobileBean();
		mobileInfoBean.setAccess(access);
		mobileInfoBean.setCarrier(carrier);
		mobileInfoBean.setImei(imei);
		mobileInfoBean.setLocation(location);
		mobileInfoBean.setMobile(mobile);
		mobileInfoBean.setModel(model);
		mobileInfoBean.setOsVersion(osVersion);
		mobileInfoBean.setResolution(resolution);
		mobileInfoBean.setRomVersion(romVersion);
		mobileInfoBean.setSdkVersion(sdkVersion);
		return mobileInfoBean;
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
			LogUtil.e(TAG, e);
		}

		return operator;
	}
	
	public static String getBootMode() {
		return SystemProperties.get("ro.boot.mode");
	}
}
