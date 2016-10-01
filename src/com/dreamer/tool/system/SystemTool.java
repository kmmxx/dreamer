package com.dreamer.tool.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.android.internal.app.IUsageStats;
//import com.android.internal.os.PkgUsageStats;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.Vibrator;
import android.os.PowerManager.WakeLock;
import android.os.RecoverySystem;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SystemTool {

	public static Context initClassLoader(Context context, String packName) {
		try {
			Context mContext;
			mContext = context.createPackageContext(packName,
					Context.CONTEXT_INCLUDE_CODE
							| Context.CONTEXT_IGNORE_SECURITY);
			ClassLoader classLoader = context.getClassLoader();
			Field field = ClassLoader.class.getDeclaredField("parent");
			field.setAccessible(true);
			field.set(mContext.getClassLoader(), classLoader);
			return mContext;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Assertion
	public static void throwRuntimeException(boolean b,String tip) {
		if (b) {
			throw new RuntimeException(tip);
		}
	}

	/**
	 * Returns a global setting.
	 */
	public static int getGlobalSetting(Context context, String setting) {
		ContentResolver cr = context.getContentResolver();
		return Settings.Global.getInt(cr, setting, 0);
	}

	/**
	 * Returns a system setting.
	 */
	public static int getSystemSetting(Context context, String setting) {
		ContentResolver cr = context.getContentResolver();
		return Settings.System.getInt(cr, setting, 0);
	}

	/**
	 * return dimension
	 * 
	 * @param context
	 * @param id
	 * @return int
	 */
	public static int getDimensionPixelOffset(Context context, int id) {
		return context.getResources().getDimensionPixelOffset(id);
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	public static List<String> getHomePackageNames(Context context) {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		// 属性
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}

	/**
	 * 判断当前界面是否是桌面
	 */
	public static boolean isHomeApp(Context context) {
		ActivityManager mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		return getHomePackageNames(context).contains(
				rti.get(0).topActivity.getPackageName());
	}

	public static void setStrictMode() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
	}

	public static boolean isBackground(Context context, String pakName) {

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(pakName)) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					Log.i("后台", appProcess.processName);
					return true;
				} else {
					Log.i("前台", appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}

	public static void startAdb(boolean b) {
		android.os.SystemProperties.set("ctl.start", "adbd");
	}

	public static void stopAdb(boolean b) {
		android.os.SystemProperties.set("ctl.stop", "adbd");
	}

	public static void startProcess(boolean b, String process) {
		android.os.SystemProperties.set("ctl.start", process);
	}

	public static void stopProcess(boolean b, String process) {
		android.os.SystemProperties.set("ctl.stop", process);
	}

	public static void getPropertity(Context context, String name, int def) {
		Settings.System.getInt(context.getContentResolver(), name, def);
	}

	// public static List<Map<String, Integer>> getAppLaunchCount(Context
	// context) {
	// Log.d("getAppLaunchCount", "getAppLaunchCount start");
	// PackageManager manager = context.getPackageManager();
	// Intent intent = new Intent(Intent.ACTION_MAIN);
	// List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
	// List<ComponentName> names = new ArrayList<ComponentName>();
	// List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
	// Log.d("getAppLaunchCount", "getAppLaunchCount:" + infos.size());
	// for (ResolveInfo info : infos) {
	// ComponentName cn = new ComponentName(info.activityInfo.packageName,
	// info.activityInfo.name);
	// names.add(cn);
	// }
	// Log.d("getAppLaunchCount", "names:" + names.size());
	// IUsageStats mUsageStatsService = IUsageStats.Stub
	// .asInterface(ServiceManager.getService("usagestats"));
	// for (ComponentName name : names) {
	// PkgUsageStats aStats = null;
	// try {
	// aStats = mUsageStatsService.getPkgUsageStats(name);
	// Log.d("getAppLaunchCount", "aStats" + aStats);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// if (aStats != null) {
	// int count = aStats.launchCount;
	// Log.d("getAppLaunchCount", "count:" + count);
	// Map<String, Integer> map = new HashMap<String, Integer>();
	// map.put(name.getPackageName(), count);
	// list.add(map);
	// Log.d("getAppLaunchCount", "list.add:" + name.getPackageName()
	// + "," + count);
	// } else {
	// Log.d("getAppLaunchCount", "Astas is null");
	// }
	// }
	// return list;
	// }

	public static void addBroadcast(Context context,
			BroadcastReceiver receiver, String action) {
		if (receiver != null && action != null && !"".equals(action)) {
			IntentFilter filter = new IntentFilter();
			filter.addAction(action);
			context.registerReceiver(receiver, filter);
		}

	}

	public static void removeBroadcast(Context context,
			BroadcastReceiver receiver) {
		if (receiver != null) {
			context.unregisterReceiver(receiver);
		}
	}

	// send broadcast
	public static void sendBroadcast(Context context, String action,
			Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setAction(action);
		context.sendBroadcast(intent);
	}

	public static void killPackage(Context context, String packageNmae) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		manager.killBackgroundProcesses(packageNmae);
	}

	public static void forceStopPackage(Activity activity, String pkgname) {
		// (1)关闭Activity和Service
		// 该操作使播放的音乐停止,并关闭了音乐播放器所有的Activity .
		ActivityManager mActivityManager = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
		// mActivityManager.forceStopPackage(activity.getPackageName());
	}

	public static void killServiceNotActivity(Activity activity) {
		// (2)只关闭Service,不关闭Activity
		// 操作后播放的音乐会停止,并回到桌面,下次再点击音乐播放器进入上次未关闭的界面 .
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(startMain);
		System.exit(0);
	}

	public static void forceStopPackage(ActivityManager am, String pkgname) {
		try {
			Method forceStopPackage = am.getClass().getDeclaredMethod(
					"forceStopPackage", String.class);
			if (forceStopPackage != null) {
				forceStopPackage.setAccessible(true);
				forceStopPackage.invoke(am, pkgname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void killSelf() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void setMainThreadPolicy() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
	}

	// 获取系统宽高
	public static int[] getWidthHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };
	}

	/**
	 * 安装apk文件
	 */
	public static void installApk(Context context, File apk) {
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apk.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());// 如果不加上这句的话在apk安装完成之后点击单开会崩溃
	}

	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (packInfo != null) {
			String version = packInfo.versionName;
			return version;
		} else {
			return null;
		}

	}

	public static String getPackageName(Context context) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String packageNames = info.packageName;
		return packageNames;
	}

	public static boolean queryAppIsAlive(Context context, String str) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(str)) {
				// find it, break
				return true;
			}
		}
		return false;
	}

	public static void sendBroadcast(Context context, Intent intent) {
		context.sendBroadcast(intent);
	}

	public static void keepScreenLight(Context context) {
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.FULL_WAKE_LOCK, "My Lock");
		wakeLock.acquire();
	}

	public static void keepScreenDark(Context context) {
		PowerManager powerManager = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.FULL_WAKE_LOCK, "My Lock");
		wakeLock.release();
	}

	public static void setFullWindowFeture(Activity activity) {
		// 隐藏顶部程序名称 写在setContentView(R.layout.xxxx);之前，不然报错
		activity.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public static void excute(String cmd) {
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void installPackageAfterReboot(Context context, String file) {
		synchronized (Object.class) {
			try {
				RecoverySystem.installPackage(context, new File(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static View inflateView(Context context, int resource) {
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return vi.inflate(resource, null);
	}

	public static void sleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showInputMethod(Context context, View view) {
		InputMethodManager input = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// input.showSoftInput(view, 0);// 显示虚拟键盘
		input.showSoftInput(view, InputMethodManager.SHOW_FORCED);
		// input.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void sendKeyEvent(int eventCode) {
		// long now = SystemClock.uptimeMillis();
		// KeyEvent down = new KeyEvent(now, now, KeyEvent.ACTION_DOWN,
		// eventCode,
		// 0);
		// KeyEvent up = new KeyEvent(now, now, KeyEvent.ACTION_UP, eventCode,
		// 0);
		// int sysVersion = Integer.parseInt(VERSION.SDK);
		// int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		// if (currentapiVersion < 17) {
		// try {
		// (IWindowManager.Stub.asInterface(ServiceManager
		// .getService("window"))).injectInputEventNoWait(down);
		// (IWindowManager.Stub.asInterface(ServiceManager
		// .getService("window"))).injectInputEventNoWait(up);
		// } catch (RemoteException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// } else {
		// Method mInputManager;
		// try {
		// if (mInputManager == null)
		// mInputManager = Class.forName("android.os.ServiceManager")
		// .getDeclaredMethod("getInstance", null);
		// mInputManager.invoke(null, name);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// mInputManager.injectInputEvent(down,
		// InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
		// InputManager.getInstance().injectInputEvent(up,
		// InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
		// }
	}

	public static void hideInputMethod(Context context, View view) {
		InputMethodManager input = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		input.hideSoftInputFromWindow(view.getWindowToken(), 0);// 隐藏
	}

	public static String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		System.out.println("mi.availMem;" + mi.availMem);

		return Formatter.formatFileSize(context, mi.availMem);
	}

	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);
	}

	public static boolean isMonkeyTesting() {
		return ActivityManager.isUserAMonkey();
	}

	public static String getLocaleLanguage() {
		Locale l = Locale.getDefault();
		return String.format("%s-%s", l.getLanguage(), l.getCountry());
	}

	public static void reboot(Context ctx) {
		try {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("reboot");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PowerManager pManager = null;
		pManager = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		pManager.reboot("");
		java.lang.System.exit(0);
	}

	public static String currentSystemVersion() {
		return Build.VERSION.INCREMENTAL.substring(1);
	}

	public static boolean matchIP(String ip) {
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static String[] resolutionIP(String ip) {
		return ip.split("\\.");
	}

	public static Vibrator startVibrator(Context context, long[] pattern,
			int index) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		if (pattern == null) {
			pattern = new long[] { 1500, 50, 300, 30, 1200, 50 };//  OFF/ON/OFF/ON...
		}
		vibrator.vibrate(pattern, index);// -1不重复，非-1为从pattern的指定下标开始重
		return vibrator;
	}

	public static void stopVibrator(Vibrator vibrator) {
		if (vibrator != null)
			vibrator.cancel();
	}
}
