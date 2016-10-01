package com.dreamer.tool.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.util.UUID;

public class BluetoothServerAcceptThread extends Thread {

	private final BluetoothServerSocket mServerSocket;
	private BluetoothAdapter mBluetoothAdapter;
	private String mName;
	private UUID mUUID;

	public BluetoothServerAcceptThread(BluetoothAdapter mBluetoothAdapter,
			String name, UUID uuid) {
		this.mBluetoothAdapter = mBluetoothAdapter;
		this.mName = name;
		this.mUUID = uuid;
		BluetoothServerSocket tmp = null;
		try {
			// MY_UUID is the app's UUID string, also used by the client code
			tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(mName,
					mUUID);
		} catch (IOException e) {
		}
		mServerSocket = tmp;
	}

	public void run() {
		BluetoothSocket socket = null;
		// Keep listening until exception occurs or a socket is returned
		while (true) {
			try {
				socket = mServerSocket.accept();
			} catch (IOException e) {
				break;
			}
			// If a connection was accepted
			if (socket != null) {
				// Do work to manage the connection (in a separate thread)
				manageConnectedSocket(socket);
				cancel();
				break;
			}
		}
	}

	private void manageConnectedSocket(BluetoothSocket socket) {
		// TODO Auto-generated method stub

	}

	/** Will cancel the listening socket, and cause the thread to finish */
	public void cancel() {
		try {
			mServerSocket.close();
		} catch (IOException e) {
		}
	}

}
