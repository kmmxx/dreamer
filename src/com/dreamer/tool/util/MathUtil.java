package com.dreamer.tool.util;

import java.util.Random;

public class MathUtil {

	public static int getRandom(int start, int end) {
		return (int) ((end - start) * Math.random() + start);
	}
	
	/** ��ָ������list�ĵõ�һ��������ҵõ��ĸ���������ָ��������� */
	public static int getRandomNextInt(int item, int max) {
		int next = -1;
		do {
			next = new Random().nextInt(max);
		} while (next == item);
		return next;
	}
}
