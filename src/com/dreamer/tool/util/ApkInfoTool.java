package com.dreamer.tool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * 获取apk信息
 * @author zhangzhp
 * 
 */
public class ApkInfoTool {
	private static final String tag = null;

	/**
	 * 
	 * @param context
	 *            Context上下�?
	 * @param apkPath
	 *            apk在SD中的路径
	 * @return
	 */
	public static Drawable getApkIcon(Context context, String apkPath) {

		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			return appInfo.loadIcon(pm);
		}
		return null;
	}


	/**
	 * 获取apk的VersionCode
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {

		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(),
					0);
			return packinfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 备份data/app目录下本程序的apk安装文件到SD卡根目录�?
	 * @param packageName
	 * @param mActivity
	 * @throws IOException
	 */
	public static void backupApp(String packageName, Activity mActivity)
			throws IOException {
		// 存放位置
		String newFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		String oldFile = null;
		try {
			// 原始位置
			oldFile = mActivity.getPackageManager().getApplicationInfo(
					packageName, 0).sourceDir;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(newFile);
		System.out.println(oldFile);

		File in = new File(oldFile);
		File out = new File(newFile + packageName + ".apk");
		if (!out.exists()) {
			out.createNewFile();
			Log.i(tag, "文件备份成功�?" + "存放�?" + newFile + "目录�?");
		} else {
			Log.i(tag, "文件备份成功�?" + "存放�?" + newFile + "目录�?");
		}

		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);

		int count;
		// 文件太大的话，我觉得�?要修�?
		byte[] buffer = new byte[256 * 1024];
		while ((count = fis.read(buffer)) > 0) {
			fos.write(buffer, 0, count);
		}

		fis.close();
		fos.flush();
		fos.close();
	}
	
	/**
	 * <p>
	 * Get current application package name.
	 * </p>
	 */
	public static String getPackageName(Context context) {
		String packageName = "0";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			packageName = info.packageName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return packageName;
	}

	/**
	 * <p>
	 * Get current application version name.
	 * </p>
	 */
	public static String getVersionName(Context context) {
		String versionName = "0";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			versionName = info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}


	/**
	 * If client don't set the appCode, it will return already knows appCode
	 * throw package name.
	 * 
	 * @param context
	 * @return appCode
	 */
	public static int getAppCode(Context context) {
		int appCode = 0;

		String pkgName = getPackageName(context);
		ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(pkgName,
					PackageManager.GET_META_DATA);
			appCode = appInfo.metaData.getInt("AppCode");

			if (appCode == 0) {
				Toast.makeText(
						context,
						"AppCode not set. please read the document of NearMeStatistics SDK.",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return appCode;
	}

}
