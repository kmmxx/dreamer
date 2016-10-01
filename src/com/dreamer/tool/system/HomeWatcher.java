package com.dreamer.tool.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Home��������װ
 * 
 * @author mHomeWatcher = new HomeWatcher(this);
 *         mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
 * @Override public void onHomePressed() { Log.e(TAG, "onHomePressed");
 *           isHomePress = true; }
 * @Override public void onHomeLongPressed() { Log.e(TAG, "onHomeLongPressed");
 *           isHomePress = true; } }); 
 *           mHomeWatcher.startWatch();
 *           
 *           mHomeWatcher.stopWatch();
 *  
 * 
 */
public class HomeWatcher {

	static final String TAG = "HomeWatcher";
	private Context mContext;
	private IntentFilter mFilter;
	private OnHomePressedListener mListener;
	private InnerRecevier mRecevier;

	// �ص��ӿ�
	public interface OnHomePressedListener {
		public void onHomePressed();

		public void onHomeLongPressed();
	}

	public HomeWatcher(Context context) {
		mContext = context;
		mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	/**
	 * ���ü���
	 * 
	 * @param listener
	 */
	public void setOnHomePressedListener(OnHomePressedListener listener) {
		mListener = listener;
		mRecevier = new InnerRecevier();
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
	}

	/**
	 * �㲥������
	 */
	class InnerRecevier extends BroadcastReceiver {
		final String SYSTEM_DIALOG_REASON_KEY = "reason";
		final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
		final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
		final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
				if (reason != null) {
					Log.e(TAG, "action:" + action + ",reason:" + reason);
					if (mListener != null) {
						if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
							// �̰�home��
							if (mListener != null)
								mListener.onHomePressed();
						} else if (reason
								.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
							// ����home��
							if (mListener != null)
								mListener.onHomeLongPressed();
						}
					}
				}
			}
		}
	}
}
