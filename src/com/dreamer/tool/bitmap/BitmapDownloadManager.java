/**
 * Seachange-vod
 */
package com.dreamer.tool.bitmap;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

/**
 * @author Zengk 2012-10-24
 */
public class BitmapDownloadManager {
	final static  String Tag="BitmapDownloadManager";
	static BitmapDownloadManager instance;

	public synchronized static BitmapDownloadManager getInstance() {
		if (instance == null)
			instance = new BitmapDownloadManager();
		return instance;
	}

	ThreadPoolExecutor mThreadPool;

	BitmapDownloadManager() {
		mThreadPool = new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(100));
	}

	public synchronized void addDownloadTask(BitmapDownloadTask task) {
		if (task == null)return;
		try{	
			mThreadPool.execute(task.getRunnable());
		}catch(Exception e){
			Log.e(Tag, "»°Õº∆¨≥¨ ±¡À");
		}
	}
	public  synchronized void execute(final Runnable runnable) {
		mThreadPool.execute(runnable);
	}
}