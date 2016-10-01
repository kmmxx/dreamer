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
			// ���ServiceManager��
			Class<?> ServiceManager = Class
					.forName("android.os.ServiceManager");

			// ���ServiceManager��getService����
			Method getService = ServiceManager.getMethod("getService",
					java.lang.String.class);

			// ����getService��ȡRemoteService
			Object oRemoteService = getService.invoke(null, "usagestats");

			// ���IUsageStats.Stub��
			Class<?> cStub = Class
					.forName("com.android.internal.app.IUsageStats$Stub");
			// ���asInterface����
			Method asInterface = cStub.getMethod("asInterface",
					android.os.IBinder.class);
			// ����asInterface������ȡIUsageStats����
			Object oIUsageStats = asInterface.invoke(null, oRemoteService);
			// ���getPkgUsageStats(ComponentName)����
			Method getPkgUsageStats = oIUsageStats.getClass().getMethod(
					"getPkgUsageStats", ComponentName.class);
			// ����getPkgUsageStats ��ȡPkgUsageStats����
			Object aStats = getPkgUsageStats.invoke(oIUsageStats, aName);

			// ���PkgUsageStats��
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
