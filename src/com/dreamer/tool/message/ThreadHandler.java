package com.dreamer.tool.message;

import android.os.Handler;
import android.os.HandlerThread;

public class ThreadHandler implements Runnable {

	private HandlerThread mLoadThread;
	private Handler mLoadThreadHandler;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public ThreadHandler(){
		 mLoadThread = new HandlerThread("Recents-TaskResourceLoader",
	             android.os.Process.THREAD_PRIORITY_BACKGROUND);
	     mLoadThread.start();
	     mLoadThreadHandler = new Handler(mLoadThread.getLooper());
	     mLoadThreadHandler.post(this);
	}
	
}
