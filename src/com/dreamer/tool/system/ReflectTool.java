package com.dreamer.tool.system;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class ReflectTool {
	private static String TAG = ReflectTool.class.getSimpleName();
	
	public static String getSystemProperty(String key) {
		try {
			Class<?> clazz = Class.forName("android.os.SystemProperties");
			Method get = clazz.getMethod("get", String.class);
			return (String) get.invoke(null, key);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean isStreamMute(AudioManager am, int streamType){
		try {
			Method ismute = AudioManager.class.getMethod("isStreamMute", int.class);
			ismute.setAccessible(true);
			return (Boolean) ismute.invoke(am, streamType);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fallback to check stream volume
		return am.getStreamVolume(streamType) == 0;
	}

	/**
	 * 是否为基本的数据类型
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isBaseDateType(Field field) {
		Class<?> clazz = field.getType();
		return clazz.equals(String.class) || clazz.equals(Integer.class)
				|| clazz.equals(Byte.class) || clazz.equals(Long.class)
				|| clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(java.util.Date.class)
				|| clazz.equals(java.sql.Date.class) || clazz.isPrimitive();
	}

	public static String parseClass(String clazzNmae, Context context) {
		StringBuilder sb = new StringBuilder();
		Class<?> clazz = null;
		clazz = getClass(clazzNmae, context);
		sb.append("this class:");
		// 取得本类的全部属性
		Field[] field = clazz.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 权限修饰符
			int mo = field[i].getModifiers();
			String priv = Modifier.toString(mo);
			// 属性类型
			Class<?> type = field[i].getType();
			sb.append(priv + " " + type.getName() + " " + field[i].getName()
					+ ";\n");
		}
		System.out
				.println("===============实现的接口或者父类的属性========================");
		sb.append("super class or interface:");
		// 取得实现的接口或者父类的属性
		Field[] filed1 = clazz.getFields();
		for (int j = 0; j < filed1.length; j++) {
			// 权限修饰符
			int mo = filed1[j].getModifiers();
			String priv = Modifier.toString(mo);
			// 属性类型
			Class<?> type = filed1[j].getType();
			sb.append(priv + " " + type.getName() + " " + filed1[j].getName()
					+ ";\n");
		}
		return sb.toString();
	}

	public static Class<?> getClass(String clazzName, Context context) {
		Class<?> maClass = null;
		try {
			maClass = Class.forName(clazzName, true, context.getClassLoader());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maClass;
	}

	public static Object getFieldValue(String clazzName, String field,
			Context context) {
		Class<?> maClass = null;
		try {
			maClass = Class.forName(clazzName, true, context.getClassLoader());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object maObject = null;
		try {
			maObject = maClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Field f_mPm = null;
		try {
			f_mPm = maClass.getDeclaredField(field);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f_mPm.setAccessible(true);
		try {
			return f_mPm.get(maObject);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
