package com.dreamer.tool.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.util.Log;

public class ZipUtil {

	/** 读取压缩包中的软件版本号 **/
	public static List<InputStream> unzip(String filepath,
			InputStream zipFileName) {
		ZipInputStream in = null;
		InputStream filein = null;
		List<InputStream> list = new ArrayList<InputStream>();
		try {
			Log.v("ZipUtil", "----1--");
			ZipFile zipfile = new ZipFile(filepath);
			in = new ZipInputStream(zipFileName);
			Log.v("ZipUtil", "----2--");
			// 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
			// 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
			// 输入流读取完成；
			ZipEntry entry = in.getNextEntry();
			Log.v("ZipUtil", "----3--entry=" + entry);
			while (entry != null) {
				String name = entry.getName();
				Log.v("ZipUtil", "----------name-1--" + name);
				filein = zipfile.getInputStream(entry);
				list.add(filein);
				entry = in.getNextEntry();
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {

			try {
				if (in != null) {
					in.close();
				}
				if (filein != null) {
					filein.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return list;
	}
	
	private static final int IO_BUF_SIZE = 1024;

	public static byte[] compress(String str) {
		if (null == str || str.length() == 0) {
			return "".getBytes();
		}

		byte[] bytes = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
		GZIPOutputStream gzipOut = null;

		try {
			gzipOut = new GZIPOutputStream(out);
			int size = 0;
			byte[] buf = new byte[IO_BUF_SIZE];
			while ((size = in.read(buf)) > 0) {
				gzipOut.write(buf, 0, size);
				gzipOut.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				gzipOut.close();
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		bytes = out.toByteArray();
		return bytes;
	}

	public static String uncompress(byte[] bytes) {
		if (null == bytes || bytes.length == 0) {
			return "";
		}
		
		String result = "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		GZIPInputStream gzip = null;
		try {
			gzip = new GZIPInputStream(in);
			byte[] buf = new byte[IO_BUF_SIZE];
			int size = 0;
			while ((size = gzip.read(buf)) > 0) {
				out.write(buf, 0, size);
				out.flush();
			}
		} catch (IOException e) {
			LogUtil.e(e);
		} finally {
			try {
				in.close();
				gzip.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result = out.toString();
		return result;
	}

}
