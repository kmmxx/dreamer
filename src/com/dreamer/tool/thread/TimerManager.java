package com.dreamer.tool.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

public class TimerManager {

	private static TimerManager manager;
	private Timer mTimer;
	private HashMap<String, TimerTask> map;

	public static TimerManager getInstance() {
		if (manager == null) {
			manager = new TimerManager();
		}
		return manager;
	}

	private Context mContext;
	private boolean isPrepared = false;

	private TimerManager() {

	}

	public void prepare(Context context) {
		if (isPrepared) {
			return;
		}
		isPrepared = true;
		this.mContext = context;
	}

	public Timer getTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		return mTimer;
	}

	public void addTimerTask(String name, TimerTask task, long delay) {
		removeTimerTask(name);
		getTimer().schedule(task, delay);
		getMap().put(name, task);
	}
	
	public void addTimerTask(String name, TimerTask task, long delay,long peroid) {
		removeTimerTask(name);
		getTimer().schedule(task, delay,peroid);
		getMap().put(name, task);
	}

	public void removeTimerTask(String name) {
		if (getMap().containsKey(name)) {
			if (getMap().get(name) != null) {
				getMap().get(name).cancel();
			}
			getMap().remove(name);
		}
	}

	public HashMap<String, TimerTask> getMap() {
		if (map == null) {
			map = new HashMap<String, TimerTask>();
		}
		return map;
	}

	public void cancelAll() {
		Iterator iter = getMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			getMap().get(key).cancel();
			getMap().remove(key);
		}
	}

}
