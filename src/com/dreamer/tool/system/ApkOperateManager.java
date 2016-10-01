//package com.dreamer.tool.system;
//
//import java.io.File;
//
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.IPackageDeleteObserver;
//import android.content.pm.IPackageInstallObserver;
//import android.content.pm.IPackageMoveObserver;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.RemoteException;
//import android.util.Log;
//import com.ipanel.market.home.MarketApplication;
//import com.ipanel.market.outquery.InstalledAppInfo;
//import com.ipanel.market.utils.CustomAction;
//
//public class ApkOperateManager {
//	public static String TAG = "ApkOperateManager";
//
//	/* 安装apk */
//	public static void installApk(Context context, String fileName) {
//		Intent intent = new Intent();
//		intent.setAction(Intent.ACTION_VIEW);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.setDataAndType(Uri.parse("file://" + fileName),
//				"application/vnd.android.package-archive");
//		context.startActivity(intent);
//	}
//
//	/* 卸载apk */
//	public static void uninstallApk(Context context, String packageName) {
//		Uri uri = Uri.parse("package:" + packageName);
//		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
//		context.startActivity(intent);
//	}
//
//	/**
//	 * 静默安装
//	 * */
//	public static void installApkDefaul(Context context, String fileName,
//			String packageName, String APPName, String appId, String type_name) {
//		Log.d(TAG, "jing mo an zhuang:" + packageName + ",fileName:" + fileName
//				+ ",type_name:" + type_name);
//		File file = new File(fileName);
//		int installFlags = 0;
//		if (!file.exists())
//			return;
//		installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
//		if (hasSdcard()) {
//			installFlags |= PackageManager.INSTALL_EXTERNAL;
//		}
//		PackageManager pm = context.getPackageManager();
//		// try {
//		// if (!packageName.equals("com.ipanel.market")
//		// && pm.getPackageInfo(packageName, 0) != null) {
//		// uninstallApkDefaul(context, "UNINSTALL_BEFORE_INSTALL",
//		// packageName);
//		// }
//		// } catch (NameNotFoundException e) {
//		// e.printStackTrace();
//		// }
//		try {
//			IPackageInstallObserver observer = new MyPakcageInstallObserver(
//					context, APPName, appId, fileName, packageName, type_name);
//			Log.i(TAG, "########installFlags:" + installFlags + "packagename:"
//					+ packageName);
//			pm.installPackage(Uri.fromFile(file), observer, installFlags,
//					packageName);
//		} catch (Exception e) {
//			((MarketApplication) context).setApp_detail_status(appId,
//					MarketApplication.APP_STATUS_NOTEXIT);
//		}
//
//	}
//
//	/* 静默卸载 */
//	public static void uninstallApkDefaul(Context context, String action,
//			String packageName) {
//		PackageManager pm = context.getPackageManager();
//		IPackageDeleteObserver observer = new MyPackageDeleteObserver(context,
//				action, packageName);
//		pm.deletePackage(packageName, observer, 0);
//	}
//
//	/* 静默卸载回调 */
//	private static class MyPackageDeleteObserver extends
//			IPackageDeleteObserver.Stub {
//		Context cxt;
//		String action;
//		String pkname;
//
//		public MyPackageDeleteObserver(Context c, String action, String pkname) {
//			this.cxt = c;
//			this.action = action;
//			this.pkname = pkname;
//		}
//
//		@Override
//		public void packageDeleted(String packageName, int returnCode) {
//			Log.d(TAG, "returnCode = " + returnCode + ",action:" + action
//					+ "packageName:" + packageName + ",pkname:" + pkname);// 返回1代表卸载成功
//			if (returnCode == 1) {
//				SharedPreferences installedAPPInfo = cxt.getSharedPreferences(
//						"installedAPPInfo", Context.MODE_WORLD_READABLE);
//				if (installedAPPInfo.contains(packageName)) {
//					String appId = installedAPPInfo.getString(packageName,
//							"no this appId");
//					((MarketApplication) cxt.getApplicationContext())
//							.setApp_detail_status(appId,
//									MarketApplication.APP_STATUS_NOTEXIT);
//					installedAPPInfo.edit().remove(packageName).commit();
//					ContentResolver conResolver = cxt.getContentResolver();
//					conResolver.delete(InstalledAppInfo.CONTENT_URI,
//							InstalledAppInfo.APP_PKNAME + " = " + "'" + pkname
//									+ "'", null);
//				}
//				MarketApplication ma = ((MarketApplication) cxt
//						.getApplicationContext());
//				Log.e(TAG, "###packageDeleted###111size:"
//						+ ma.getManagerLists().size());
//				ma.removeManagerItem(pkname);
//				ma.removeUpdateItem(pkname);
//				Log.e(TAG, "##packageDeleted####22222size:"
//						+ ma.getManagerLists().size());
//			}
//			
//			
//			Intent it = new Intent();
//			it.setAction(action);
//			it.putExtra("uninstall_returnCode", returnCode);
//			cxt.sendBroadcast(it);
//		}
//	}
//
//	/* 静默安装回调 */
//	private static class MyPakcageInstallObserver extends
//			IPackageInstallObserver.Stub {
//		Context cxt;
//		String appName;
//		String appId;
//		String filename;
//		String pkname;
//		String type_name;
//
//		public MyPakcageInstallObserver(Context c, String appName,
//				String appId, String filename, String packagename,
//				String type_name) {
//			this.cxt = c;
//			this.appName = appName;
//			this.appId = appId;
//			this.filename = filename;
//			this.pkname = packagename;
//			this.type_name = type_name;
//		}
//
//		@Override
//		public void packageInstalled(String packageName, int returnCode) {
//			MarketApplication ma = ((MarketApplication) cxt
//					.getApplicationContext());
//			Log.i(TAG,
//					"returnCode = " + returnCode + ","
//							+ ma.getApp_detail_status(appId));// 返回1代表安装成功
//			Intent it = new Intent();
//			it.setAction(CustomAction.INSTALL_ACTION);
//			it.putExtra("install_returnCode", returnCode);
//			it.putExtra("install_packageName", packageName);
//			it.putExtra("install_appName", appName);
//			it.putExtra("install_appId", appId);
//			if (returnCode == 1) {
//				//ma.getAPPList();
//				//ma.setManagerLists();
//				if (ma.getApp_detail_status(appId) == MarketApplication.APP_STATUS_UPDATITNG) {
//					ma.removeUpdateItem(pkname);
//					cxt.sendBroadcast(it);
//					return;
//				}
//
//				SharedPreferences installedAPPInfo = cxt.getSharedPreferences(
//						"installedAPPInfo", Context.MODE_WORLD_READABLE);
//				installedAPPInfo.edit().putString(packageName, appId).commit();
//
//				// 保存信息到数据库
//				if (appId != null && appName != null && pkname != null
//						&& type_name != null) {
//					ContentResolver conResolver = cxt.getContentResolver();
//					ContentValues values = new ContentValues();
//					values.put(InstalledAppInfo.APP_ID, appId);
//					values.put(InstalledAppInfo.APP_NAME, appName);
//					values.put(InstalledAppInfo.APP_PKNAME, pkname);
//					values.put(InstalledAppInfo.APP_TYPENAME, type_name);
//					Uri result = conResolver.insert(
//							InstalledAppInfo.CONTENT_URI, values);
//					Log.i(TAG,
//							"#########install suscess...result:"
//									+ result.toString());
//				}
//				ma.setApp_detail_status(appId,
//						MarketApplication.APP_STATUS_INSTALLED);
//			} else {
//				ma.removeManagerItem(pkname);
//				Log.i(TAG, "#########install failed...delete file:" + filename
//						+ "packagename:" + packageName + ",pkname:" + pkname);
//
//				ma.setApp_detail_status(appId,
//						MarketApplication.APP_STATUS_NOTEXIT);
//			}
//			File f = new File(filename);
//			if (f.exists()) {
//				f.delete();
//			}
//
//			cxt.sendBroadcast(it);
//		}
//	}
//
//	/**
//	 * sd卡不存在
//	 */
//	public static final int NO_SDCARD = -1;
//
//	/**
//	 * 移动应用到SD Card
//	 * 
//	 * @param context
//	 * @param pkname
//	 * @return
//	 */
//	public static void movePackage(Context context, String pkname) {
//		PackageManager pm = context.getPackageManager();
//		MovePackageObserver mpo = new MovePackageObserver();
//		pm.movePackage(pkname, mpo, PackageManager.INSTALL_EXTERNAL);
//	}
//
//	/**
//	 * 移动应用的回�?
//	 */
//	public static class MovePackageObserver extends IPackageMoveObserver.Stub {
//
//		public MovePackageObserver() {
//		}
//
//		@Override
//		public void packageMoved(String packageName, int returnCode)
//				throws RemoteException {
//			Log.i(TAG, "packagename:" + packageName + ",returnCode:"
//					+ returnCode);
//		}
//	}
//
//	/**
//	 * 判断有无sd�?
//	 * */
//	public static boolean hasSdcard() {
//		String status = Environment.getExternalStorageState();
//		if (status.equals(Environment.MEDIA_MOUNTED)
//				|| status.equals("/mnt/sdcard")) {
//			Log.i(TAG, "has sdcard....");
//			return true;
//		} else {
//			return false;
//		}
//	}
//}
