package com.dreamer.tool.system;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

public class ActivityTool {

	public static void startActivity(Context context, String packageName,
			String className) {
		// TODO Auto-generated method stub
		try {
			Intent intent = new Intent();
			intent.setClassName(packageName, className);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startActivity(Context packageContext, Class<?> cls) {
		Intent intent = new Intent(packageContext, cls);
		packageContext.startActivity(intent);
	}

	public static void startActivity(Context context, String packageName,
			String className, String mParams) {
		// TODO Auto-generated method stub
		try {
			Intent intent = new Intent();
			intent.setClassName(packageName, className);
			if (mParams != null && !"".equals(mParams)) {
				Bundle bundle = new Bundle();
				bundle.putString(mParams.split("=")[0], mParams.split("=")[1]);
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean startService(Context context, Intent intent) {
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfos = pm
				.queryIntentServices(intent, 0);

		// Is somebody else trying to intercept our IAP call?
		if (resolveInfos == null || resolveInfos.size() != 1) {
			return false;
		}

		ResolveInfo serviceInfo = resolveInfos.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		Intent explicIntent = new Intent(intent);
		explicIntent.setComponent(component);
		context.getApplicationContext().startService(intent);
		return true;
	}

	public static void startActivity(Context context, String packageName,
			String className, Bundle bundle) {
		// TODO Auto-generated method stub
		try {
			Intent intent = new Intent();
			intent.setClassName(packageName, className);
			if (bundle != null) {
				intent.putExtras(bundle);
			}
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startLauncherActivity(Context context, String packageName) {
		// TODO Auto-generated method stub
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent == null || intent.getAction().equals(""))
			return;
		context.startActivity(intent);
	}

	public static void startLauncherActivity(Context context,
			String packageName, String mParams) {
		// TODO Auto-generated method stub
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent == null || intent.getAction().equals(""))
			return;
		try {
			if (mParams != null && !"".equals(mParams)) {
				Bundle bundle = new Bundle();
				bundle.putString(mParams.split("=")[0], mParams.split("=")[1]);
				intent.putExtras(bundle);
			}
		} catch (Exception e) {

		}
		context.startActivity(intent);
	}

	public static void startLauncherActivity(Context context,
			String packageName, Bundle bundle) {
		// TODO Auto-generated method stub
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent == null || intent.getAction().equals(""))
			return;
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		context.startActivity(intent);
	}
}
