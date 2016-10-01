package com.dreamer.tool.network;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager extends BroadcastReceiver {

	private static NetworkManager mNetworkManager;
	private Context mContext;
	public final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	private boolean isNetworkAvailable = false;
	private List<NetworkObserver> mNetworkObserver;
	private NetworkType networkType;

	private NetworkManager() {
	}

	public static NetworkManager getInstance() {
		if (mNetworkManager == null) {
			mNetworkManager = new NetworkManager();
		}
		return mNetworkManager;
	}

	public void prepare(Context context) {
		this.mContext = context;
	}

	public void checkNetworkState(Context mContext) {
		Intent intent = new Intent();
		intent.setAction(ANDROID_NET_CHANGE_ACTION);
		mContext.sendBroadcast(intent);
	}

	public void registerNetworkReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mContext.registerReceiver(this, filter);
	}

	public void unRegisterNetworkReceiver() {
		if (mContext == null)
			return;
		mContext.unregisterReceiver(this);
		for (NetworkObserver n : mNetworkObserver) {
			n = null;
		}
		mNetworkObserver.clear();
		mNetworkObserver = null;
	}

	public boolean isConnected() {
		return isNetworkAvailable;
	}

	public void setConnected(boolean isConnected) {
		this.isNetworkAvailable = isConnected;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean b = isNetworkAvailable(mContext);
		if (b) {
			networkType = getNetworkType(mContext);
			notifyNetworkObservers(true);
		} else {
			notifyNetworkObservers(false);
		}
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public NetworkType getNetworkType(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return NetworkType.noneNet;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				return NetworkType.CMNET;
			} else {
				return NetworkType.CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			return NetworkType.wifi;
		}
		return NetworkType.noneNet;

	}

	public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	private void notifyNetworkObservers(boolean b) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mNetworkObserver.size(); i++) {
			NetworkObserver observer = mNetworkObserver.get(i);
			if (observer != null) {
				if (b) {
					observer.onConnect(networkType);
				} else {
					observer.onDisconnect();
				}
			}
		}
	}

	public void regeistNetworkObserver(NetworkObserver n) {
		if (mNetworkObserver == null) {
			mNetworkObserver = new ArrayList<NetworkManager.NetworkObserver>();
		}
		mNetworkObserver.add(n);
	}

	public void unRegeistNetworkObserver(NetworkObserver n) {
		if (mNetworkObserver != null) {
			mNetworkObserver.remove(n);
		}
	}

	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static enum NetworkType {
		wifi, CMNET, CMWAP, noneNet
	}

	public interface NetworkObserver {

		public void onConnect(NetworkType networkType);

		public void onDisconnect();
	}

}
