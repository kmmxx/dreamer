/**
 * StockUtil.java 2009-7-29 Version 1.0
 */
package com.dreamer.tool.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kemm
 * 
 */
public final class PageUtil {

	private static String TAG = PageUtil.class.getSimpleName();

	public static List<String> getListByLine(String content, int len) {
		if (content == null)
			return null;
		List<String> list = new ArrayList<String>();
		String[] strs = content.split("\n");
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].length() > len) {
				list.addAll(getListByLength(strs[i], len));
			} else {
				list.add(strs[i]);
			}
		}
		return list;
	}

	public static List<String> getListByLength(String content, int len) {
		int size = content.length() / len
				+ (content.length() % len == 0 ? 0 : 1);
		List<String> strs = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			strs.add(content.substring(
					i * len < content.length() ? i * len : content.length() - 1,
					(i + 1) * len <= content.length() ? (i + 1) * len : content
							.length()));
			// Mlog.d(TAG,
			// "strs i:"
			// + i
			// + " :"
			// + content.substring(i * len < content.length() ? i
			// * len : content.length() - 1,
			// (i + 1) * len <= content.length() ? (i + 1)
			// * len : content.length()));
		}
		return strs;
	}

	public static List<String> getListByFilter(String content, String filter) {
		List<String> list = new ArrayList<String>();
		String[] strs1 = content.split(filter);
		for (int i = 0; i < strs1.length; i++) {
			list.add(strs1[i]);
		}
		return list;
	}

	public static <T> List<T> getSubList(List<T> list, int start, int end) {
		if (list == null)
			return null;
		int size = list.size();
		if (start > size - 1) {
			start = size - 1;
		}
		if (end > size) {
			end = size;
		}
		if (start < 0 || end < 0) {
			start = end = 0;
		}
		return list.subList(start, end);
	}

	public static <T> int getMaxPage(List<T> list, int lineInPage) {
		// TODO Auto-generated method stub
		if (list == null)
			return 0;
		int max = 0;
		if (list.size() % lineInPage == 0) {
			max = list.size() / lineInPage;
		} else {
			max = list.size() / lineInPage + 1;
		}
		return max;
	}

	public static <T> List<String> getStringListT(List<T> list) {
		// TODO Auto-generated method stub
		if (list == null)
			return null;
		List<String> strs = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			strs.add(list.get(i).toString());
		}
		return strs;
	}

}