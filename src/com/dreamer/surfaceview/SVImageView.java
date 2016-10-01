package com.dreamer.surfaceview;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class SVImageView extends SVView{

	private Bitmap mBitmap;
	private int alpha = 255;
	
	public SVImageView(){
		
	}
	
	public SVImageView(Bitmap bitmap){
		this.mBitmap = bitmap;
	}
	
	
	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
		this.width = mBitmap.getWidth();
		this.height = mBitmap.getHeight();
	}
	
	public void setXY(float x,float y){
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public void setBackground(Bitmap bitmap) {
		// TODO Auto-generated method stub
		this.mBitmap = bitmap;
	}
	
	public void setBackground(Context ctx, int rsid) {
		// TODO Auto-generated method stub
		this.mBitmap = getBitmap(ctx, rsid);
	}
	
	
	public  boolean onDraw(Canvas canvas){
		if(mBitmap == null)
			return false;
		mPaint.setAlpha(alpha);
//		alpha --;
//		if(alpha<0)
//			alpha = 255;
//		Log.d("kemm", "alpha:"+alpha);
		drawImage(canvas,mBitmap,x,y,width,height,0,0);
//		canvas.drawBitmap(mBitmap, x, y, mPaint);
		return true;
	}
	
	
	public  void drawImage(Canvas canvas,Bitmap blt,int x,int y,int w,int h,int bx,int by){
		Rect src = new Rect();
		Rect dst = new Rect();
		
		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;
		
		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;
		
		canvas.drawBitmap(blt, src, dst, null);
		src = null;
		dst = null;
	}
	
	public static void drawImage(Canvas canvas,Bitmap blt,int x,int y){
		canvas.drawBitmap(blt, x, y, null);
	}
	
	
	/**
	 * 加载图片
	 * 
	 * @param filepath
	 * @return
	 */
	public static final Bitmap getBitmap(Context context, int bitAdress) {
		return BitmapFactory.decodeResource(context.getResources(), bitAdress);
	}
	
	/**
	 * 根据路径创建位图图片
	 * 
	 * @param assetManager
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static final Bitmap creatBitmap(AssetManager assetManager, String filePath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();  
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
//			return BitmapFactory.decodeStream(assetManager.open(filePath), null, opts);
			return BitmapFactory.decodeStream(assetManager.open(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据路径批量创建位图图片
	 * 
	 * @param assetManager
	 * @param filepath 
	 *            文件路径
	 * @return
	 */
	public static final Bitmap[] creatBitmaps(AssetManager assetManager, String[] filepath) {
		BitmapFactory.Options opts = new BitmapFactory.Options();  
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		
		int length = filepath.length;
		Bitmap[] temp = new Bitmap[length];
		for(int i=0;i<length;i++) {
			try {
				temp[i] = BitmapFactory.decodeStream(assetManager.open(filepath[i]));
//				temp[i] = BitmapFactory.decodeStream(assetManager.open(filepath[i]), null, opts);
			} catch (IOException e) {
				System.out.println("[JAVA] load image error, the path = " + filepath[i]);
			}
		}
		return temp;
	}
	
	/**
	 * 释放图片资源
	 * @param bitmap
	 **/
	public static void recycleBitmap(Bitmap bitmap){
		if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
	}
	
	/**
	 * 批量释放图片资源
	 * @param bitmaps
	 **/
	public static void recycleBitmaps(Bitmap[] bitmaps){
		for(int i=0;i<bitmaps.length;i++){
			if(bitmaps[i] != null && !bitmaps[i].isRecycled()){
				bitmaps[i].recycle();
				bitmaps[i] = null;
	        }
		}
	}
	
}
