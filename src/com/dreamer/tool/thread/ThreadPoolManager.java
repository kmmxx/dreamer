package com.dreamer.tool.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
	private int defaultFixedSize = 3;
	private static ThreadPoolManager mThreadPoolManager;
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(defaultFixedSize);
	private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	private  ThreadPoolManager(){
		
	}
	
	public static synchronized ThreadPoolManager getInstance(){
		if(mThreadPoolManager == null){
			mThreadPoolManager = new ThreadPoolManager();
		}
		return mThreadPoolManager;
	}
	
	
}
