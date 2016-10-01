package com.dreamer.tool.cache;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtils {

	public static int convertByteToInt(byte data) {

		int heightBit = (int) ((data >> 4) & 0x0F);
		int lowBit = (int) (0x0F & data);

		return heightBit * 16 + lowBit;
	}

	public static int[] convertByteToColor(byte[] data) {
		int size = data.length;
		if (size == 0) {
			return null;
		}

		int arg = 0;
		if (size % 3 != 0) {
			arg = 1;
		}

		int[] color = new int[size / 3 + arg];
		int red, green, blue;

		if (arg == 0) {
			for (int i = 0; i < color.length; ++i) {
				red = convertByteToInt(data[i * 3]);
				green = convertByteToInt(data[i * 3 + 1]);
				blue = convertByteToInt(data[i * 3 + 2]);

				color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
			}
		} else {
			for (int i = 0; i < color.length - 1; ++i) {
				red = convertByteToInt(data[i * 3]);
				green = convertByteToInt(data[i * 3 + 1]);
				blue = convertByteToInt(data[i * 3 + 2]);
				color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
			}

			color[color.length - 1] = 0xFF000000;
		}

		return color;
	}

	public static FloatBuffer createNativBuffer(float[] f) {
		ByteBuffer qbbn = ByteBuffer.allocateDirect(f.length * 4);
		qbbn.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = qbbn.asFloatBuffer();
		buffer.put(f);
		buffer.position(0);
		return buffer;
	}

	public static ShortBuffer createNativBuffer(short[] s) {
		ByteBuffer ibb = ByteBuffer.allocateDirect(s.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		ShortBuffer shortBuffer = ibb.asShortBuffer();
		shortBuffer.put(s);
		shortBuffer.position(0);
		return shortBuffer;
	}

	public static IntBuffer createNativBuffer(int[] i) {
		ByteBuffer qbbn = ByteBuffer.allocateDirect(i.length * 4);
		qbbn.order(ByteOrder.nativeOrder());
		IntBuffer buffer = qbbn.asIntBuffer();
		buffer.put(i);
		buffer.position(0);
		return buffer;
	}

	public static ByteBuffer createNativBuffer(byte[] i) {
		ByteBuffer qbbn = ByteBuffer.allocateDirect(i.length * 4);
		qbbn.order(ByteOrder.nativeOrder());
		qbbn.put(i);
		qbbn.position(0);
		return qbbn;
	}

}