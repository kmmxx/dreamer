package com.dreamer.surfaceview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AlphaAnimation;

public class SVTools {
	private Matrix mMatrix;
	private Activity mActivity;
	static SVTools mUtilTools;
	private long currentTime = 0l;
	private long endTime = 0l;
	private int fps = 0;
	private SVTools(){
		mMatrix = new Matrix();
	}
	
	
	public static SVTools getInstance(){
		if(mUtilTools==null){
			mUtilTools = new SVTools();
		}
		return mUtilTools;
	}
	
	public void prepare(Activity mActivity){
		this.mActivity = mActivity;
	}
	
	public static void startAlphaAnimation(float startAlpha,float endAlpha,long duration){
		AlphaAnimation mAnimationAlpha = new AlphaAnimation(startAlpha,endAlpha);
		mAnimationAlpha.setDuration(duration);
		mAnimationAlpha.start();
	}
	
	
	
	//Ëõ·ÅÍ¼Æ¬
	public Bitmap scaleImage(Bitmap bitmap, float scaleX, float scaleY) {
		mMatrix.reset();
		mMatrix.postScale(scaleX, scaleY);
		Bitmap mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), mMatrix, true);
		return mBitmap;
	};
	
	//»­Í¼Æ¬
	public  void drawImage(Canvas mCanvas, Bitmap bitmap, int x, int y,
			int w, int h, int bx, int by) {
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
		mCanvas.drawBitmap(bitmap, src, dst, null);
	}

	public  void drawIamge(Canvas mCanvas, Bitmap bitmap, int x, int y) {
		mCanvas.drawBitmap(bitmap, x, y, null);
	}
	//Í¼Æ¬×ª»»
	public Bitmap decodeBitmap(int rid) {
		Options opts = new Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inScaled = false;
		opts.inSampleSize = 1;
		opts.inScreenDensity = DisplayMetrics.DENSITY_HIGH;
		return BitmapFactory.decodeResource(mActivity.getResources(), rid, opts);
	}
}
