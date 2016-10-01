package com.dreamer.tool.system;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;
import android.util.Log;

public class SystemInfoManager {

	public static HashMap<String, Long> getAppInfo(ApplicationInfo aInfo) {
		HashMap<String, Long> maps = new HashMap<String, Long>();
		ComponentName aName = new ComponentName(aInfo.packageName, aInfo.name);
		long aLaunchCount;
		long aUseTime;
		try {
			// 获得ServiceManager类
			Class<?> ServiceManager = Class
					.forName("android.os.ServiceManager");

			// 获得ServiceManager的getService方法
			Method getService = ServiceManager.getMethod("getService",
					java.lang.String.class);

			// 调用getService获取RemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// 获得IUsageStats.Stub类
			Class<?> cStub = Class
					.forName("com.android.internal.app.IUsageStats$Stub");
			// 获得asInterface方法
			Method asInterface = cStub.getMethod("asInterface",
					android.os.IBinder.class);
			// 调用asInterface方法获取IUsageStats对象
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// 获得getPkgUsageStats(ComponentName)方法
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod(
					"getPkgUsageStats", ComponentName.class);
			// 调用getPkgUsageStats 获取PkgUsageStats对象
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);

			// 获得PkgUsageStats类
			Class<?> PkgUsageStats = Class
					.forName("com.android.internal.os.PkgUsageStats");

			aLaunchCount = PkgUsageStats.getDeclaredField("launchCount")
					.getInt(aStats);
			aUseTime = PkgUsageStats.getDeclaredField("usageTime").getLong(
					aStats);
			maps.put("count", aLaunchCount);
			maps.put("time", aUseTime);
		} catch (Exception e) {
			Log.e("###", e.toString(), e);
		}

		return maps;
	}

}
