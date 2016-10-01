package com.dreamer.tool.ntp;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.UnknownHostException;

public class NtpTimeSync {

	/**
	 * ��ȡ����ʱ�䣬ʧ�ܾͷ���һ��Ĭ��ֵ
	 * @return
	 * 
	 */
	public static long getCurrentTime() {
		int retry = 2;
		int port = 123;
		int timeout = 3000;
		String ip = "210.72.145.44";

		// get the address and NTP address request
		InetAddress ipv4Addr = null;
		try {
			ipv4Addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e1) {
			//e1.printStackTrace();
		}

		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(timeout); // will force the
			// InterruptedIOException

			for (int attempts = 0; attempts <= retry; attempts++) {
				try {
					// Send NTP request
					//
					byte[] data = new NtpMessage().toByteArray();
					DatagramPacket outgoing = new DatagramPacket(data,
							data.length, ipv4Addr, port);
					socket.send(outgoing);

					// Get NTP Response
					//
					DatagramPacket incoming = new DatagramPacket(data,
							data.length);
					socket.receive(incoming);
					
					// Validate NTP Response
					// IOException thrown if packet does not decode as expected.
					NtpMessage msg = new NtpMessage(incoming.getData());					
					double utc = msg.transmitTimestamp - (2208988800.0);	
					return (long)(utc * 1000);
				} catch (InterruptedIOException ex) {
					// Ignore, no response received.
				}
			}
		} catch (NoRouteToHostException e) {
			//e.printStackTrace();
		} catch (ConnectException e) {
			// Connection refused. Continue to retry.
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if (socket != null)
				socket.close();
		}
		
		return -1; //"2011-09-01 12:00"
	}
}
