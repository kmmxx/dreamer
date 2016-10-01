package com.dreamer.tool.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * @author ipanel
 * @
 */
public class UtilTools {
	
	public static Object getFieldValue(Class<?> fieldClass, String fieldName) {
		try {
			Field[] fields = fieldClass.getDeclaredFields();
			for (int i = 0, len = fields.length; i < len; i++) {
				String varName = fields[i].getName();
				if (varName.equals(fieldName)) {
					Object value = fields[i].get(fieldClass);
					return value;
				}
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static int getResoureId(Class<?> fieldClass, String fieldName) {
		try {
			Field[] fields = fieldClass.getDeclaredFields();
			for (int i = 0, len = fields.length; i < len; i++) {
				String varName = fields[i].getName();
				if (varName.equals(fieldName)) {
					Object value = fields[i].get(fieldClass);
					return ((Integer) value).intValue();
				}
			}
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		}
		return -1;
	}

	private final static int DEFAULT_BUFFER_SIZE = 1024;

	public static String sizeToM(long size) {
		if (size / 1024 / 1024 >= 1) {
			return size / 1024 / 1024
					+ (size / 1024 % 1024 / 1024.0 + "").substring(1, 3) + "MB";
		} else {
			return size / 1024 + (size % 1024 / 1024.0 + "").substring(1, 3)
					+ "KB";
		}
	}

	public static void intentForward(Context context, Class<?> forwardClass) {
		Intent intent = new Intent();
		intent.setClass(context, forwardClass);
		context.startActivity(intent);
	}

	public static boolean string2File(String res, String filePath) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath);
			if (!distFile.getParentFile().exists())
				distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024];
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	public static String file2String(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file),
						encoding);
			} else {
				reader = new InputStreamReader(new FileInputStream(file));
			}

			char[] buffer = new char[DEFAULT_BUFFER_SIZE];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		if (writer != null)
			return writer.toString();
		else
			return null;
	}

	public static String currentSystemVersion() {
		return Build.VERSION.INCREMENTAL.substring(1);
	}

	public static boolean matchIP(String ip) {
		String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public static String[] resolutionIP(String ip) {
		return ip.split("\\.");
	}

	public static Bitmap byte2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		System.out.println("mi.availMem;" + mi.availMem);

		return Formatter.formatFileSize(context, mi.availMem);
	}

	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);
	}

	public static boolean isMonkeyTesting() {
		return ActivityManager.isUserAMonkey();
	}

	public static String getLocaleLanguage() {
		Locale l = Locale.getDefault();
		return String.format("%s-%s", l.getLanguage(), l.getCountry());
	}
	
	public static void reboot(Context ctx){
		PowerManager pManager= null;
		pManager= (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
		pManager.reboot("");
		java.lang.System.exit(0);
	}

}
