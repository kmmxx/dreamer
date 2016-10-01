package com.dreamer.tool.system;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;

import com.coloros.recents.featureoption.FeatureOption;

import android.os.ServiceManager;
import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;

public class WallpaperGlobals extends IWallpaperManagerCallback.Stub{
    private final static String TAG = "RecentTasks_WallpaperGlobals";
    private final static boolean DEBUG = false; // FeatureOption.DEBUG;

    private IWallpaperManager mService;

    private Bitmap optimizeWallpaper(Bitmap bm, int desiredWidth, int desiredHeight) {
        final int bitmapWidth = bm.getWidth();
        final int bitmapHeight = bm.getHeight();
        if (bitmapWidth == desiredWidth && bitmapHeight == desiredHeight ) {
            return bm;
        }
        int targetWidth = 0;
        int targetHeight = 0;
        if (bitmapWidth >= desiredWidth && bitmapHeight >= desiredHeight) {//bitmap is big enough
            targetWidth = desiredWidth;
            targetHeight = desiredHeight;
        }
        else if (bitmapWidth * desiredHeight >= bitmapHeight * desiredWidth) {//height is too small
            targetHeight = bitmapHeight;
            targetWidth = (int)(1.0 * bitmapHeight * desiredWidth / desiredHeight);
        }
        else {//width is too small
            targetWidth = bitmapWidth;
            targetHeight = (int)(1.0 * bitmapWidth * desiredHeight / desiredWidth);
        }
        if (DEBUG) Log.d(TAG, "bitmapWidth : " + bitmapWidth + ",bitmapHeight : " + bitmapHeight
                    +",desiredWidth : " + desiredWidth + ",desiredHeight : " + desiredHeight
                    +",targetWidth : " + targetWidth + "targetHeight : " + targetHeight);
        if (bitmapWidth == targetWidth && bitmapHeight == targetHeight) {//need not change
            return bm;
        }
        Bitmap optimizedBitmap = Bitmap.createBitmap(bm, 0, 0, targetWidth, targetHeight);
        bm.recycle();
        return optimizedBitmap;
    }

    public Bitmap getCurrentWallpaperLocked(Context context, BitmapFactory.Options options) {
         IBinder b = ServiceManager.getService(Context.WALLPAPER_SERVICE);
         mService = IWallpaperManager.Stub.asInterface(b);
        try {
            Bundle params = new Bundle();
            ParcelFileDescriptor fd = mService.getWallpaper(this, params);
            Bitmap bm = null;
            if (fd != null) {
                try {
                    bm = BitmapFactory.decodeFileDescriptor(
                            fd.getFileDescriptor(), null, options);
                    if (bm == null) {
                        if (DEBUG) Log.v(TAG, "getCurrentWallpaperLocked ---decodeFileDescriptor null --- need getDefaultWallpaper");
                        bm = getDefaultWallpaper(context, options);
                    }
                    if (bm != null) {
                        bm = optimizeWallpaper(bm, GaussianWallpaper.mScreenWidth/4, GaussianWallpaper.mScreenHeight/4);
                    }
                    return bm;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fd.close();
                        fd = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (DEBUG) Log.w(TAG, "getCurrentWallpaperLocked---- fd is null,  need getDefaultWallpaper");
                bm = getDefaultWallpaper(context, options);
                return bm;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void onWallpaperChanged() {
    	
    }

    private Bitmap getDefaultWallpaper(Context context, BitmapFactory.Options options) {
         try {
             InputStream is = context.getResources().openRawResource(
                                oppo.R.drawable.oppo_default_wallpaper);
            if (is != null) {
                try {
                    Bitmap bm = BitmapFactory.decodeStream(is, null, options);
                    return bm;
                } catch (OutOfMemoryError e) {
                    if (DEBUG) Log.w(TAG, "Can't decode stream", e);
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;
    }
}


