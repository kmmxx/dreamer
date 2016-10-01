package com.oppo.launcher.graphic;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.WindowManager;
import android.view.Display;

public class GaussianBlur {
    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;
    
	private final static String TAG = "GaussianBlur11";
	private final static boolean DEBUG = true;

	private static float hRadius = 7;//5;
	private static float vRadius = 7;//5;
	private static int iterations = 5;//7;

	static {
		System.loadLibrary("gaussgraphic");
	}

	private static GaussianBlur mGaussianBlur = null;

	private GaussianBlur() {
	}

	public static GaussianBlur getInstance() {
		if(null == mGaussianBlur) {
			mGaussianBlur = new GaussianBlur();
		}
		return mGaussianBlur;
	}

    public static void setScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        mScreenWidth = width < height ? width : height;
        mScreenHeight = width < height ? height : width;
        if(DEBUG) {
        	Log.d(TAG, "Utilities:setScreenWidth   getRotation() " + display.getRotation()
        		+ ", mScreenWidth = " + mScreenWidth);
        }
    }
    
	public static Bitmap scaleBitmap(Bitmap bm) {
		final float scale = 0.25f;
		return scaleBitmap(bm, scale);
	}
	
	public static Bitmap scaleBitmap(Bitmap bm, float scale) {
	    Bitmap bmp = null;
		if(null != bm) {
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			int beginX = bm.getWidth() - mScreenWidth;
			int width = mScreenWidth;
			if(beginX < 0) {
				beginX = 0;
				width = bm.getWidth();
			}
			bmp = Bitmap.createBitmap(bm, beginX, 0, width, bm.getHeight(), matrix, true);
		}
		if(null != bmp && DEBUG) {
			Log.d(TAG, "Gaussian:captureWallpaper bm.getWidth = " + bm.getWidth()
				+ ", bmp.getWidth = " + bmp.getWidth());
		}
		return bmp;
	}
	
	public void setParameter(int h, int v, int i) {
		hRadius = h;
		vRadius = v;
		iterations = i;
	}

	public Bitmap generateGaussianBitmap(Bitmap bmp, boolean recycle) {
		final float brightness = 0.8f;
		return generateGaussianBitmap(bmp, brightness, recycle);
	}
	
	public Bitmap generateGaussianBitmap(Bitmap bmp, float brightness, boolean recycle) {
		if(null == bmp) {return null;}
		
		if(brightness < 0f) {
			brightness = 0f;
		} else if(brightness > 1f) {
			brightness = 1f;
		}
		if(DEBUG) {
			Log.v(TAG, "GaussianBlur:generateGaussianBitmap  Enter !!!!");
		}
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		Bitmap bitmap = Bitmap.createBitmap(width, height,
		Bitmap.Config.ARGB_8888);
		bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < iterations; i++) {
			blur_native(inPixels, outPixels, width, height, hRadius);
			blur_native(outPixels, inPixels, height, width, vRadius);
		}
		blurFractional_native(inPixels, outPixels, width, height, hRadius);
		blurFractional_native(outPixels, inPixels, height, width, vRadius);
		for(int i=0; i<width; i++){
			inPixels[i] = inPixels[width+i];
			inPixels[i+(height-1)*width] = inPixels[i+(height-2)*width];
		}		
		for(int j=0; j<height; j++){
			inPixels[j*width] = inPixels[j*width +1];
			inPixels[(j+1)*width-1] = inPixels[(j+1)*width -2];
		}
		
		int a = 0;
		int r = 0;
		int g = 0;
		int b = 0;
		int delta = (int)(height * 0.075f + 0.5f) + 15;
		if(DEBUG) {
			Log.d(TAG, "GaussianBlur:generateGaussianBitmap  Modify Alpha -- delta = " + delta);
		}
		float dynamicBrightness = brightness;

        for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){				
				a = (inPixels[i*width+j] >> 24) & 0xFF;
				r = (inPixels[i*width+j] >> 16) & 0xFF;
				g = (inPixels[i*width+j] >> 8) & 0xFF;
				b = inPixels[i*width+j] & 0xFF;

				//if(i < 50) {
					//inPixels[i*width+j]  = 0X00000000;
				//	dynamicBrightness = (1f - brightness) * (50 - i) / 50 + brightness;
					//a = (int)(a*0.8f);
				//} else if(i > height - delta) {
				//	dynamicBrightness = (1f - brightness) * (delta - height + i) / delta + brightness;
					//a = (int)(a * 0.2f);
					//inPixels[i*width+j] = inPixels[(height - delta)*width+ (j + 3) % width];
					//continue;
				//}else {
					dynamicBrightness = brightness;
				//}
				
				//a = (int)(a*brightness);
				r = (int)(r*dynamicBrightness);
				g = (int)(g*dynamicBrightness);
				b = (int)(b*dynamicBrightness);
				inPixels[i*width+j]  = (a << 24)+(r << 16)+(g << 8)+b;
			}
		}
		
		bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
		if(recycle) {
			bmp.recycle();
			bmp = null;
		}
		if(DEBUG) {
			Log.d(TAG, "GaussianBlur:generateGaussianBitmap  generate Complete !!!!");
		}
		return bitmap;
	}

	public native void blur_native(int[] in, int[] out, int width, int height,
		float radius);

	public native void blurFractional_native(int[] in, int[] out, int width,
		int height, float radius);
}
