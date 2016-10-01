package com.dreamer.tool.thread;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;

public class RunnableManager {

	private static RunnableManager manager;
	private Handler handler;
	private HashMap<String, Runnable> map;

	public static RunnableManager getInstance() {
		if (manager == null) {
			manager = new RunnableManager();
		}
		return manager;
	}

	private Context mContext;
	private boolean isPrepared = false;

	private RunnableManager() {

	}

	public void prepare(Context context, Handler handler) {
		if (isPrepared) {
			return;
		}
		isPrepared = true;
		this.mContext = context;
		this.handler = handler;
	}

	public void addRunable(String rName, Runnable runnable) {
		if (getMap().containsKey(rName)) {
			if (handler != null) {
				handler.removeCallbacks(getMap().get(rName));
			}
			getMap().remove(rName);
		}
		if (handler != null) {
			handler.post(runnable);
		}
		getMap().put(rName, runnable);
	}

	public void addRunableDelay(String rName, Runnable runnable,long delay) {
		if (getMap().containsKey(rName)) {
			if (handler != null) {
				handler.removeCallbacks(getMap().get(rName));
			}
			getMap().remove(rName);
		}
		if (handler != null) {
			handler.postDelayed(runnable, delay);
		}
		getMap().put(rName, runnable);
	}

	public void addRunableAtFrontOfQueue(String rName, Runnable runnable) {
		if (getMap().containsKey(rName)) {
			if (handler != null) {
				handler.removeCallbacks(getMap().get(rName));
			}
			getMap().remove(rName);
		}
		if (handler != null) {
			handler.postAtFrontOfQueue(runnable);
		}
		getMap().put(rName, runnable);
	}

	public Runnable getRunnable(String key) {
		return getMap().get(key);
	}

	public void removeRunable(String rName) {
		if (getMap().containsKey(rName)) {
			if (handler != null) {
				handler.removeCallbacks(getMap().get(rName));
			}
			getMap().remove(rName);
		}
	}

	public void removeAllRunable() {
		Iterator iter = getMap().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			if (handler != null) {
				handler.removeCallbacks(getMap().get(key));
			}
			getMap().remove(key);
		}
	}

	public HashMap<String, Runnable> getMap() {
		if (map == null) {
			map = new HashMap<String, Runnable>();
		}
		return map;
	}
}
