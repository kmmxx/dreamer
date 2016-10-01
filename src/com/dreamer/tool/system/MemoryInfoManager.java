package com.dreamer.tool.system;

import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager;
import android.content.Context;

public class MemoryInfoManager {

    private static MemoryInfoManager mINSTANCE;

    private ActivityManager mActivityManager;
    private MemoryInfo mMemoryInfo;
    private long mTotalMemory;

    private MemoryInfoManager(Context context) {
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mMemoryInfo = new MemoryInfo();
        mActivityManager.getMemoryInfo(mMemoryInfo);
        mTotalMemory = mMemoryInfo.totalMem;
    }

    public static MemoryInfoManager getInstance(Context context) {
        if (mINSTANCE == null) {
            mINSTANCE = new MemoryInfoManager(context.getApplicationContext());
        }
        return mINSTANCE;
    }

    public long getTotalMemory() {
        return mTotalMemory;
    }

    public long getAvailableMemory() {
        mActivityManager.getMemoryInfo(mMemoryInfo);
        return mMemoryInfo.availMem;
    }
}
