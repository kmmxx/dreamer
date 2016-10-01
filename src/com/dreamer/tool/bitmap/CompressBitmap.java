package com.dreamer.tool.bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class CompressBitmap {

	private static String fromDir = "F:\\Downloads\\itime_history_today\\pic";
	private static String toDir = "F:\\Downloads\\itime_history_today2\\pic";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		compressBitmaps();
		
		System.out.println("hello");

	}

	//压缩图片
	public static void compressBitmaps() {
		File root = new File(fromDir);

		File[] files = root.listFiles();
		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i].getName());
			File childRoot = new File(root, files[i].getName());
			File[] pics = childRoot.listFiles();
			for (int j = 0; j < pics.length; j++) {
//				System.out.println(pics[j].getName());

				String filepath =fromDir+"\\"+files[i].getName()+"\\"+pics[j].getName();
				Bitmap bitmap = compressImage(filepath);
				String tofilepath =toDir+"\\"+files[i].getName()+"\\"+pics[j].getName();
				saveBitmap(new File(tofilepath), bitmap);
			}
		}

	}

	
	//将图片保存到本地
	private static void saveBitmap(File file,Bitmap bitmap){
		FileOutputStream fos =null;
		if(file.exists()){
			file.delete();
		}
		else{
			try {
				file.createNewFile();
				fos = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
				if(bitmap!=null&&!bitmap.isRecycled()){
					bitmap.recycle();
					bitmap=null;
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//压缩图片
	private static Bitmap compressImage(String filepath) {

		Options ops = new Options();
		ops.inPreferredConfig = Bitmap.Config.RGB_565;
		FileInputStream fis = null;
		Bitmap image=null;
		Bitmap bitmap=null;
		try {
			fis = new FileInputStream(filepath);
			image = BitmapFactory.decodeStream(fis, null, ops);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			// int options = 100;
			// while (baos.toByteArray().length / 1024 > 100) { //
			// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			// baos.reset();// 重置baos即清空baos
			// image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
			// 这里压缩options%，把压缩后的数据存放到baos中
			// }
			ByteArrayInputStream isBm = new ByteArrayInputStream(
					baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中

			bitmap = BitmapFactory.decodeStream(isBm, null, ops);// 把ByteArrayInputStream数据生成图片
			return bitmap;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(image!=null&&!image.isRecycled()){
				image.recycle();
				image=null;
			}
		}
		return null;
	}

}
