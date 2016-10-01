package com.dreamer.tool.bitmap;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dreamer.tool.bitmap.BitmapDownloadTask.BitMapCallback;




import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class MemoryCache {
  
	private static final String TAG = "MemoryCache";
	static MemoryCache instance;
	public synchronized static MemoryCache getInstance() {
		
		if (instance == null)
			instance = new MemoryCache();
		return instance;
	 }
	
	// 放入缓存是个同步操作
	
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	public Object mute=new Object();
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	
	private long size = 0;// current allocated size
	
	// 缓存只能占用的最大堆内存
	
	private long limit = 1000000;// max memory in bytes
	
	public MemoryCache() {
		// use 25% of available heap size
		
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}
	
	public void setLimit(long new_limit) { 
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		   
		   return cache.get(id);
			
	}
	public boolean isContainsKey(String id) {
		   if(cache.containsKey(id)){
			   if(cache.get(id)!=null){
				   if(getSizeInBytes(cache.get(id))>0){
					   return true;
				   }
			   }
		   }
		   return false;	
	}
	

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 * 
	 */
	private synchronized void checkSize() {// 一次最多删除两张
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {//
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				
				iter.remove();
				size -= getSizeInBytes(entry.getValue());
				Log.i(TAG, "Clean cacheremove= " + entry.getKey());
				entry.getValue().recycle();
				//iter.remove();
				break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
		if (size > limit){
			checkSize();
		}
	}

	public void clear() {
		Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Bitmap> entry = iter.next();
			
			iter.remove();
			
			entry.getValue().recycle();
			//iter.remove();
			break;
		};
		cache.clear();
	}
    
	
	public void addImageFromAssetsFile(String url,Context mContext){  
		
		Log.v("addImageFromAssetsFile", "url="+url);
		if(isContainsKey(url)){
			myBitmapBack.setImageViewBiMap(url,get(url),true);
			
		}else{
			Bitmap image = null;  
			AssetManager am = mContext.getAssets();  
			try {  
				InputStream is = am.open(url);  
				image = BitmapFactory.decodeStream(is);  
				is.close();  
			} catch (IOException e){ 
				Log.v("addImageFromAssetsFile", "---------exception------------");
			}  
			if(image!=null){
				myBitmapBack.setImageViewBiMap(url,image,true);
				checkSize();
			}else{
				myBitmapBack.setImageViewBiMap(url,null,false);
			}
		}
	}
	
	
	/**
	 * 图片占用的内存
	 * 
	 * @param bitmap
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
	
	
	public void addBitMap(String url){
		if(isContainsKey(url)){
			myBitmapBack.setImageViewBiMap(url,get(url),true);
			
		}else{
			BitmapDownloadTask testBitmapDownloadTask=new BitmapDownloadTask(url,url,0);
			testBitmapDownloadTask.setCallback(new BitMapCallback(){
	
				@Override
				public void notifyCallback(BitmapDownloadTask task,
						boolean success, Bitmap bitmap, String mKey, int index) {
					// TODO Auto-generated method stub
					if(success){
						if(bitmap!=null){
							cache.put(mKey, bitmap);
							
							myBitmapBack.setImageViewBiMap(mKey,bitmap,true);
							checkSize();
						}else{
							if(myBitmapBack!=null){
								myBitmapBack.setImageViewBiMap(mKey,null,false);
							}
						}
					}else{
						if(myBitmapBack!=null){
							myBitmapBack.setImageViewBiMap(mKey,null,false);
						}
					}
				}
				
			});
			BitmapDownloadManager.getInstance().addDownloadTask(testBitmapDownloadTask);	
		}
	}
	BitmapBack myBitmapBack;
	public BitmapBack getMyBitmapBack() {
		return myBitmapBack;
	}

	public void setMyBitmapBack(BitmapBack myBitmapBack) {
		this.myBitmapBack = myBitmapBack;
	}
	public  interface BitmapBack{
	  	  public void setImageViewBiMap(String url,Bitmap bitmapm,boolean success); 
	}
	
}
