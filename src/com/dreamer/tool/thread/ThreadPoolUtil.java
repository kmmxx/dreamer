package com.dreamer.tool.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

	private static ExecutorService mFixedThreadExecutor = Executors.newFixedThreadPool(3);

	private static ExecutorService mReadDBThreadExecutor = Executors.newFixedThreadPool(3);
	
	private static ExecutorService mWriteDBThreadExecutor = Executors.newSingleThreadExecutor();

	public static void execute(Runnable runnable) {
		mFixedThreadExecutor.execute(runnable);
	}

	public static void executeReadDB(Runnable runnable) {
		mReadDBThreadExecutor.execute(runnable);
	}
	
	public static void executeWriteDB(Runnable runnable) {
		mWriteDBThreadExecutor.execute(runnable);
	}
}
