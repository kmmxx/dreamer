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
 * �㲥������װ
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

	// �ص��ӿ�
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
	 * ���ü���
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
	 * ��ʼ������ע��㲥
	 */
	public void startWatch() {
		if (mRecevier != null) {
			mContext.registerReceiver(mRecevier, mFilter);
		}
	}

	/**
	 * ֹͣ������ע���㲥
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
	 * �㲥������
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
