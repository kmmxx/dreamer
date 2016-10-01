/**
 * BitmapToolkit.java
 * TODO
 */
package com.dreamer.tool.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.util.Log;

/**
 * @author kmmxxx
 */
public class FileToolkit {

	private static String kB_UNIT_NAME = "KB";
	private static String B_UNIT_NAME = "B";
	private static String MB_UNIT_NAME = "MB";

	public static boolean writeFile(Context context, String path, String content) {
		try {
			FileOutputStream outStream = context.openFileOutput(path,
					Context.MODE_WORLD_READABLE);
			outStream.write(content.getBytes());
			outStream.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static String readFile(Context context, String path) {
		try {
			FileInputStream inStream = context.openFileInput(path);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();
			return stream.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//读取文本文件中的内容
    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
            //打开文件
            File file = new File(path);
            //如果path是传递过来的参数，可以做一个非目录的判断
            if (file.isDirectory())
            {
                Log.d("TestFile", "The File doesn't not exist.");
            }
            else
            {
                try {
                    InputStream instream = new FileInputStream(file); 
                    if (instream != null) 
                    {
                        InputStreamReader inputreader = new InputStreamReader(instream);
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line;
                        //分行读取
                        while (( line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }                
                        instream.close();
                    }
                }
                catch (java.io.FileNotFoundException e) 
                {
                    Log.d("TestFile", "The File doesn't not exist.");
                } 
                catch (IOException e) 
                {
                     Log.d("TestFile", e.getMessage());
                }
            }
            return content;
    }

	public static String getSizeString(long size) {
		if (size < 1024) {
			return String.valueOf(size) + B_UNIT_NAME;
		} else {
			size = size / 1024;
		}
		if (size < 1024) {
			return String.valueOf(size) + kB_UNIT_NAME;
		} else {
			size = size * 100 / 1024;
		}
		return String.valueOf((size / 100)) + "."
				+ ((size % 100) < 10 ? "0" : "") + String.valueOf((size % 100))
				+ MB_UNIT_NAME;
	}

	public static String getMbSize(long dirSize) {
		double size = 0;
		size = (dirSize + 0.0) / (1024 * 1024);
		DecimalFormat df = new DecimalFormat("0.00");
		String filesize = df.format(size);
		return filesize;
	}

	public static String getKBSize(long dirSize) {
		double size = 0;
		size = (dirSize + 0.0) / 1024;
		DecimalFormat df = new DecimalFormat("0.00");
		String filesize = df.format(size);
		return filesize;
	}

	public static File[] getCurrentFiles(String filePath) {
		return new File(filePath).listFiles();
	}

	public static File[] getPrevDirectoryFiles(String filePath) {
		if (!filePath.equals("/")) {
			File f = new File(filePath);
			f = f.getParentFile();
			return getCurrentFiles(f.getPath());
		} else {
			return getCurrentFiles("/");
		}

	}

	public static void deleteFiles(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteFiles(childFiles[i]);
			}
			file.delete();
		}
	}

	public static String loadTextFromFile(String fileName, String codeStr) {
		String str = null;
		try {
			File file = new File(fileName);
			byte[] buffer = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(buffer);
			fis.close();
			str = new String(buffer, codeStr);
			str.replace("\\r\\n", "\n");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;

	}

	public static String loadTextFromRaw(Context ctx, int rawId, String codeStr) {
		String res = "";
		try {
			InputStream in = ctx.getResources().openRawResource(rawId);
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			byte[] buffer = out.toByteArray();
			in.close();
			out.close();
			// int length = in.available();
			// byte[] buffer = new byte[length];
			// in.read(buffer);
			// // "UTF-8" , "GBK","GBK2312", "UNICODE"
			res = EncodingUtils.getString(buffer, codeStr);
			res.replace("\\r\\n", "\n");
			// in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String loadTextFromAsset(Context ctx, String fileName,
			String codeStr) {
		String res = "";
		try {
			InputStream in = ctx.getResources().getAssets().open(fileName);
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			byte[] buffer = out.toByteArray();
			in.close();
			out.close();
			// int length = in.available();
			// byte[] buffer = new byte[length];
			// in.read(buffer);
			res = EncodingUtils.getString(buffer, codeStr);
			res.replace("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 返回自定文件或文件夹的大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSizes(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	// 递归
	public static long getFileSize(File f) throws Exception// 取得文件夹大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormatFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#0.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static long getFileNums(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileNums(flist[i]);
				size--;
			}
		}
		return size;

	}

}