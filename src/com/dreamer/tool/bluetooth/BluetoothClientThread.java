package com.dreamer.tool.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothClientThread extends Thread {
	private final BluetoothSocket mSocket;
	private final BluetoothDevice mDevice;
	private BluetoothAdapter mBluetoothAdapter;
	private UUID mUUID;

	public BluetoothClientThread(BluetoothAdapter mBluetoothAdapter,BluetoothDevice device,UUID uuid) {
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
		BluetoothSocket tmp = null;
		mDevice = device;
		mUUID = uuid;
		// Get a BluetoothSocket to connect with the given BluetoothDevice
		try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmp = device.createRfcommSocketToServiceRecord(mUUID);
		} catch (IOException e) {
		}
		mSocket = tmp;
	}

	public void run() {
		// Cancel discovery because it will slow down the connection
		mBluetoothAdapter.cancelDiscovery();
		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			mSocket.connect();
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
			cancel();
			return;
		}

		// Do work to manage the connection (in a separate thread)
		manageConnectedSocket(mSocket);
	}

	private void manageConnectedSocket(BluetoothSocket mSocket2) {
		// TODO Auto-generated method stub
		
	}

	/** Will cancel an in-progress connection, and close the socket */
	public void cancel() {
		try {
			mSocket.close();
		} catch (IOException e) {
		}
	}

}
