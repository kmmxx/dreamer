package com.dreamer.tool.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.List;

import com.dreamer.tool.thread.ThreadPool;

import android.content.Context;
import android.util.Log;

/**
 * @author kemm
 * 
 */
public class SocketManager {

	private static SocketManager mSocketManager;
	private Context mContext;
	private SocketChannel socketChannel;
	private ResponseDataThread responseThread;
	private SendDataThread sendThread;
	private String TAG = SocketManager.class.getSimpleName();
	private boolean connectStatus = false;
	private int connectTimes;

	public static SocketManager getInstance() {
		if (mSocketManager == null) {
			mSocketManager = new SocketManager();
		}
		return mSocketManager;
	}

	private SocketManager() {

	}

	public void prepare(Context ctx) {
		this.mContext = ctx;
	}

	public void connectServer(final String ip, final int port) {
		ThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Log.d(TAG, "connectServer 0...");
					InetSocketAddress addr = new InetSocketAddress(ip, port);
					Log.d(TAG, "connectServer 1...");
					socketChannel = SocketChannel.open();
					Log.d(TAG, "connectServer 2...");
					socketChannel.connect(addr);
					Log.d(TAG, "connectServer 3...");
					while (!socketChannel.finishConnect()) {
						Log.d(TAG, "connectServer 4...");
						try {
							Thread.sleep(100);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (connectTimes++ > 3) {
							connectTimes = 0;
							break;
						}
					}
					Log.d(TAG, "connectServer 5...");

					responseThread = new ResponseDataThread(socketChannel);
					responseThread.start();

					sendThread = new SendDataThread(socketChannel);
					sendThread.start();

					System.out.println("Connected to Server..");
					if (mSocketCallback != null) {
						mSocketCallback.noticeConnectServerStatus(true);
					}
					connectStatus(true);
				} catch (IOException e) {
					e.printStackTrace();
					Log.d(TAG, "connectServer 6...IOException");
				} catch (Exception e) {
					e.printStackTrace();
					Log.d(TAG, "connectServer 7...Exception");
					connectStatus(false);
				}
			}
		});

	}

	public boolean getConnectStatus() {
		return connectStatus;
	}

	public void connectStatus(boolean b) {
		// TODO Auto-generated method stub
		if (mSocketCallback != null) {
			mSocketCallback.noticeConnectServerStatus(false);
		}
		connectStatus = b;
		if (!b)
			closeSocket();
	}

	private void closeSocket() {
		// TODO Auto-generated method stub
		if (responseThread != null) {
			responseThread.dispose();
			responseThread = null;
		}

		if (sendThread != null) {
			sendThread.dispose();
			sendThread = null;
		}

		if (socketChannel != null)
			try {
				socketChannel.close();
				socketChannel = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public interface SocketCallback {
		public void noticeConnectServerStatus(boolean b);
	}

	private SocketCallback mSocketCallback;

	public void setSocketCallback(SocketCallback c) {
		this.mSocketCallback = c;
	}

	public void notifyReceivedData(ResponseData response) {
		// TODO Auto-generated method stub

	}

}
