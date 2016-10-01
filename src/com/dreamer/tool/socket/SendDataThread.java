package com.dreamer.tool.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

public class SendDataThread extends Thread {

	private SocketChannel socketChannel;

	private List pool;

	private boolean interrupt = false;

	private String TAG = SendDataThread.class.getSimpleName();

	
	public SendDataThread(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		pool = new LinkedList();
	}

	public void run() {
		while (!interrupt) {
			try {
				ByteBuffer request;
				synchronized (pool) {
//					if(pool==null){
//						Log.d(TAG, "pool is null");
//						pool = new LinkedList();
//					}
					while (pool.isEmpty()) {
						Log.d(TAG, "pool is isEmpty");
						pool.wait();
					}

					if (interrupt)
						break;

					request = (ByteBuffer) pool.remove(0);

					write(request);

					if (pool.size() < SocketUtil.MAX_QUEUE)
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
	 */
	public void postData(ByteBuffer request) {
		synchronized (pool) {
			if (pool.size() >= SocketUtil.MAX_QUEUE) {
				try {
					pool.wait();
				} catch (InterruptedException e) {
				}
			}

			pool.add(pool.size(), request);
			pool.notifyAll();
		}
	}

	/**
	 * 鏁版嵁鎬婚暱搴�4)聽+聽鏂囦欢绫诲瀷(1)聽+聽甯傚満绫诲瀷(1)聽+聽鍘熷鏁版嵁闀垮害(4)聽+聽鍘嬬缉鏁版嵁(鏁版嵁鎬婚暱搴β�聽6) 
	 * 
	 * @param request
	 * @throws IOException
	 */
	private void write(ByteBuffer request) throws IOException {
		Log.d(TAG , "write:"+request.array().toString());
		while (request.hasRemaining()) {
			socketChannel.write(request);
		}
		request.clear();
		request = null;
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
