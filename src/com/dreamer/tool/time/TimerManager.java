package com.dreamer.tool.time;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.dreamer.tool.message.HandlerManager;

import android.app.Activity;
import android.util.Log;

/**
 * @author kmm TimerManager.getInstance().addTimerTask( "newSong", new
 *         MTimerTask() .setOnRun(new onRun() {
 * @Override public void onRun() { // TODO Auto-generated method stub
 *           HandlerManager .getInstance() .getHandler(fragment, "newSong")
 *           .sendEmptyMessage( MESSAGE_CHANGE_CURRENT_SONG); } }));
 */
public class TimerManager {

	private static TimerManager mTimerManager;
	private Timer timer;
	HashMap<String, MTimerTask> map;

	public static TimerManager getInstance() {
		if (mTimerManager == null) {
			mTimerManager = new TimerManager();
		}
		return mTimerManager;
	}

	private Activity activity;
	private String TAG = "TimerManager";

	private TimerManager() {
		map = new HashMap<String, MTimerTask>();
	}

	public void prepare(Activity activity) {
		this.activity = activity;
	}

	public MTimerTask addTimerTask(String name, MTimerTask task) {
		if (task != null && name != null && !"".equals(name)) {
			return map.put(name, task);
		} else {
			return null;
		}
	}

	public MTimerTask getTimerTask(String name) {
		if (name != null && !"".equals(name)) {
			return map.get(name);
		} else {
			return null;
		}
	}

	public void startTimerTask(final String name, long delay) {
		Log.d(TAG, "taskname:" + name);
		cancelTimerTask(name);
		map.get(name).setTimerTask(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				map.get(name).run();
			}
		});
		if (map.get(name) != null) {
			Log.d(TAG, "task is not null");
			getTimer().schedule(map.get(name).getTimerTask(), delay);
		} else {
			Log.d(TAG, "task is  null");
		}
	}

	public void startTimerTask(final String name, long delay, long peroid) {
		cancelTimerTask(name);
		map.get(name).setTimerTask(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				map.get(name).run();
			}
		});
		if (map.get(name) != null)
			getTimer().schedule(map.get(name).getTimerTask(), delay, peroid);
	}

	public void cancelTimerTask(String name) {
		MTimerTask task = map.get(name);
		if (task != null) {
			task.cancel();
		}
	}

	public void cancelAll() {
		for (Map.Entry<String, MTimerTask> entry : map.entrySet()) {
			// System.out.println("key= " + entry.getKey() + " and value= "
			// + entry.getValue());
			entry.getValue().cancel();
		}
	}

	private Timer getTimer() {
		// TODO Auto-generated method stub
		if (timer == null) {
			timer = new Timer();
		}
		return timer;
	}

	public void cancelTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

}
