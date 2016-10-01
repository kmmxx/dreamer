package com.dreamer.tool.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import android.os.RemoteException;
import android.util.Log;

public class LinklistThread<T> extends Thread {


	private List pool;

	private boolean interrupt = false;

	private String TAG = LinklistThread.class.getSimpleName();

	private int MAX_QUEUE = 200;

	
	public LinklistThread() {
		pool = new LinkedList<T>();
	}

	public  void run() {
		while (!interrupt) {
			try {
				T t;
				synchronized (pool) {
//					if(pool==null){
//						Mlog.d(TAG, "pool is null");
//						pool = new LinkedList();
//					}
					while (pool.isEmpty()) {
						Log.d(TAG, "pool is isEmpty");
						pool.wait();
					}

					if (interrupt)
						break;

					t = (T) pool.remove(0);

					write(t);

					if (pool.size() < MAX_QUEUE )
						pool.notifyAll();
				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public boolean getStatus(){
		return interrupt;
	}

	/**
	 * add to send pool
	 * @param <T>
	 */
	public <T> void postData(T t) {
		synchronized (pool) {
			if (pool.size() >= MAX_QUEUE) {
				try {
					pool.wait();
				} catch (InterruptedException e) {
				}
			}

			pool.add(pool.size(), t);
			pool.notifyAll();
		}
	}

	/**
	 * 閺佺増宓侀幀濠氭毐鎼达拷4)鑱�鑱介弬鍥︽缁鐎�1)鑱�鑱界敮鍌氭簚缁鐎�1)鑱�鑱介崢鐔奉瀶閺佺増宓侀梹鍨(4)鑱�鑱介崢瀣級閺佺増宓�閺佺増宓侀幀濠氭毐鎼次诧拷鑱�) 
	 * @param <T>
	 * 
	 * @param request
	 * @throws IOException
	 */
	private <T> void write(T t) throws IOException {
		Log.d(TAG , "write:"+t.toString());
//		CjmxViewManager.getInstance().sendData((CjmxCode) t);
	}

	public void dispose() {
		interrupt = true;

		if (pool != null) {
			synchronized (pool) {
				pool.notifyAll();
			}
			pool = null;
		}

	}


}
