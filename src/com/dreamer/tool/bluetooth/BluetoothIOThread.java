package com.dreamer.tool.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class BluetoothIOThread extends Thread {
	private final BluetoothSocket mSocket;
	private final InputStream mInStream;
	private final OutputStream mOutStream;
	private Handler mHandler;

	public BluetoothIOThread(BluetoothSocket socket, Handler handler) {
		mSocket = socket;

		InputStream tmpIn = null;
		OutputStream tmpOut = null;

		// Get the input and output streams, using temp objects because
		// member streams are final
		try {
			tmpIn = socket.getInputStream();
			tmpOut = socket.getOutputStream();
		} catch (IOException e) {
			
		}

		mInStream = tmpIn;
		mOutStream = tmpOut;
	}

	public void run() {
		byte[] buffer = new byte[1024]; // buffer store for the stream
		int bytes; // bytes returned from read()
		// Keep listening to the InputStream until an exception occurs
		while (true) {
			try {
				// Read from the InputStream
				bytes = mInStream.read(buffer);
				// Send the obtained bytes to the UI Activity
				mHandler.obtainMessage(BluetoothGlobal.BLUETOOTH_MSG_READ,
						bytes, -1, buffer).sendToTarget();
			} catch (IOException e) {
				cancel();
				break;
			}
		}
	}

	/* Call this from the main Activity to send data to the remote device */
	public void write(byte[] bytes) {
		try {
			mOutStream.write(bytes);
		} catch (IOException e) {
		}
	}

	/* Call this from the main Activity to shutdown the connection */
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
		}
	}

}
