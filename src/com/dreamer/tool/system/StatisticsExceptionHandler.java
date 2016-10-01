package com.dreamer.tool.system;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.oppo.statistics.agent.ExceptionAgent;
import com.oppo.statistics.data.ExceptionBean;
import com.oppo.statistics.util.ApkInfoUtil;
import com.oppo.statistics.util.ConstantsUtil;
import com.oppo.statistics.util.LogUtil;

public class StatisticsExceptionHandler implements UncaughtExceptionHandler {

    private Context mContext;
    private UncaughtExceptionHandler mHandler;

    public StatisticsExceptionHandler(Context context) {

        mContext = context.getApplicationContext();
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void setStatisticsExceptionHandler() {
        if (this == mHandler) {
            return;
        }

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String exception = getStackTrace(e);
        long currentTime = System.currentTimeMillis();
        int count = 1;
        if (!TextUtils.isEmpty(exception)) {
        }

        if (null != mHandler) {
            mHandler.uncaughtException(t, e);
        }
    }

    private String getStackTrace(Throwable throwable) {
        String result = null;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            result = sw.toString();
        } catch (Exception e) {
        } finally {
            pw.close();
        }

        return result;
    }
}
