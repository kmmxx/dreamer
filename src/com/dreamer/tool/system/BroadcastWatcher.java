package com.dreamer.tool.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * 广播监听封装
 * 
 * @author mBroadcastWatcher = new BroadcastWatcher(this);
 *         mBroadcastWatcher.setOnBroadcastReceivedListener(new
 *         OnBroadcastReceivedListener() {
 * @Override public void OnBroadcastReceived() { Log.e(TAG,
 *           "OnBroadcastReceived"); }});
 * @Override mBroadcastWatcher.startWatch(); mBroadcastWatcher.stopWatch();
 * 
 * 
 */
public class BroadcastWatcher {

	static final String TAG = "HomeWatcher";
	private Context mContext;
	private IntentFilter mFilter;
	private InnerRecevier mRecevier;
	HashMap<String, OnBroadcastReceivedListener> maps;

	// 回调接口
	public interface OnBroadcastReceivedListener {
		public void OnBroadcastReceived(Context context);
	}

	public BroadcastWatcher(Context context) {
		mContext = context;
		mFilter = new IntentFilter();
		mRecevier = new InnerRecevier();
		maps = new HashMap<String, BroadcastWatcher.OnBroadcastReceivedListener>();
	}

	/**
	 * 设置监听
	 * 
	 * @param listener
	 */
	public void addBroadcastReceiver(String action,
			OnBroadcastReceivedListener listener) {
		if (!maps.containsKey(action)) {
			mFilter.addAction(action);
			maps.put(action, listener);
		}
	}

	/**
	 * 开始监听，注册广播
	 */
	public void startWatch() {
		if (mRecevier != null) {
			mContext.registerReceiver(mRecevier, mFilter);
		}
	}

	/**
	 * 停止监听，注销广播
	 */
	public void stopWatch() {
		if (mRecevier != null) {
			mContext.unregisterReceiver(mRecevier);
		}
		if (maps != null) {
			maps.clear();
			maps = null;
		}
		if (mFilter != null) {
			mFilter = null;
		}
	}

	/**
	 * 广播接收者
	 */
	class InnerRecevier extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (maps.containsKey(action)) {
				maps.get(action).OnBroadcastReceived(context);
			}
		}
	}
}
