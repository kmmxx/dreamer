package com.dreamer.tool.util;

import java.util.Random;

public class MathUtil {

	public static int getRandom(int start, int end) {
		return (int) ((end - start) * Math.random() + start);
	}
	
	/** 从指定整数list的得到一个随机数且得到的该数不能与指定数字相等 */
	public static int getRandomNextInt(int item, int max) {
		int next = -1;
		do {
			next = new Random().nextInt(max);
		} while (next == item);
		return next;
	}
}
