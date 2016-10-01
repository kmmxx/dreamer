package com.dreamer.pattern;

import android.content.Context;

public class Singleton {

	private static Singleton manager;

	public static synchronized Singleton getInstance() {
		if (manager == null) {
			manager = new Singleton();
		}
		return manager;
	}

	private Context mContext;
	private boolean isPrepared = false;

	private Singleton() {

	}

	public void prepare(Context context) {
		if (isPrepared) {
			return;
		}
		isPrepared = true;
		this.mContext = context;
	}
}
