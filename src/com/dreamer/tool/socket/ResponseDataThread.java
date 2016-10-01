package com.dreamer.tool.socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.zip.InflaterInputStream;

import android.util.Log;

public class ResponseDataThread extends Thread {

	private int offset;

	private int length;

	private int totalSize;

	private boolean ishead = true;

	private SocketChannel socketChannel;

	private ByteBuffer b = ByteBuffer.allocate(4);

	private byte[] cache = new byte[SocketUtil.MSG_BUFFER_SIZE];

	private boolean interrupt;

	private ByteBuffer buffer = ByteBuffer.allocate(SocketUtil.MSG_BUFFER_SIZE);

	private String TAG = ResponseDataThread.class.getSimpleName();

	public ResponseDataThread(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		b.order(ByteOrder.LITTLE_ENDIAN);
	}

	public void run() {

		int count = 0;

		try {
			buffer.clear();
			Log.d(TAG, "start to propress data...");
			while (!interrupt && socketChannel != null
					&& (count = socketChannel.read(buffer)) > 0) {
				buffer.flip();

				if ((offset + count) > cache.length) {
					cache = expand(cache, SocketUtil.MSG_BUFFER_SIZE * 2);
				}
				byte[] buf = buffer.array();

				System.arraycopy(buf, 0, cache, offset, count);
				offset += count;

				Log.d(TAG, "start to propress decode ...");
				while (decode())
					;
				buffer.clear();

			}
		} catch (Exception e) {
			e.printStackTrace();
			SocketManager.getInstance().connectStatus(false);
		}

	}

	private boolean decode() throws Exception {
		if (ishead && offset >= 4) {
			b.clear();
			b.put(cache, 0, 4);
			b.flip();
			length = b.getInt();
			totalSize = 4 + length;
			ishead = false;
		}

		if (offset >= totalSize) {

			System.out.println("received data.length=" + totalSize);

			ByteBuffer head = ByteBuffer.allocate(6);
			head.order(ByteOrder.LITTLE_ENDIAN);
			head.put(cache, 4, 6);
			head.flip();

			ResponseData response = new ResponseData();
			response.setFileType(head.get());
			response.setMarketId(head.get());
			int dataLength = head.getInt();
			Log.d(TAG, "dataLength=" + dataLength+" length:"+length);
			if (dataLength <= 0 || length <= 6) {
				Log.d(TAG, "dataLength<=0");
				cache = reduce(cache, totalSize);
				offset -= totalSize;
				ishead = true;
				// SocketManager.getInstance().connectStatus(false);
				return false;
			}

			byte[] data = new byte[length - 6];
			System.arraycopy(cache, 10, data, 0, data.length);

			data = inflate(data);

			if (data.length == dataLength) {
				ByteBuffer buf = ByteBuffer.wrap(data);
				buf.order(ByteOrder.LITTLE_ENDIAN);
				response.setData(buf);
			}

			SocketManager.getInstance().notifyReceivedData(response);

			cache = reduce(cache, totalSize);
			offset -= totalSize;
			ishead = true;
		}
		return ishead && (offset >= 4);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param src
	 *            byte[] 源锟斤拷锟斤拷锟斤拷锟�
	 * @param size
	 *            int 锟斤拷锟捷碉拷锟斤拷锟斤拷锟斤拷
	 * @return byte[] 锟斤拷锟捷猴拷锟斤拷锟斤拷锟�
	 */
	public byte[] expand(byte[] src, int size) {
		byte[] buffer = new byte[src.length + size];
		System.arraycopy(src, 0, buffer, 0, src.length);
		return buffer;
	}

	/**
	 * 
	 * @param src
	 * @param off
	 * @return
	 */
	public byte[] reduce(byte[] src, int off) {
		byte[] buffer = new byte[src.length - off];
		System.arraycopy(src, off, buffer, 0, buffer.length);
		return buffer;
	}

	/**
	 * 
	 * @param zipBuff
	 * @return
	 * @throws IOException
	 */
	public byte[] inflate(byte[] zipBuff) throws IOException {
		if (zipBuff == null || zipBuff.length <= 0)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(zipBuff);
		InflaterInputStream iis = new InflaterInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c;
		while ((c = iis.read()) != -1) {
			baos.write(c);
		}
		return baos.toByteArray();
	}

	public void dispose() {
		interrupt = true;
	}

}
