/**
 * TianyiEHome
 */
package com.dreamer.tool.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Dreamer
 * 2012-11-7
 */
public class ThreadPool {

	static ThreadPoolExecutor mExecutor;
	static Timer mTimer;
	static Map<Runnable, TimerTask> mTaskMaps;
	static {
		mExecutor = new ThreadPoolExecutor(3, 5, 30 * 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>());
		mTimer = new Timer();
		mTaskMaps = new HashMap<Runnable, TimerTask>();
	}

	public static synchronized void execute(final Runnable runnable) {
		mExecutor.execute(runnable);
	}

	public static synchronized void executeDealay(final Runnable runnable, long delay) {
		TimerTask task = new TimerTask() {
			public void run() {
				mExecutor.execute(runnable);
			}
		};
		mTaskMaps.put(runnable, task);
		mTimer.schedule(task, delay);
	}

	public static synchronized void cancel(final Runnable runnable) {
		TimerTask task = mTaskMaps.get(runnable);
		if (task != null) {
			task.cancel();
			mTaskMaps.remove(runnable);
		}
		mExecutor.remove(runnable);
	}
}