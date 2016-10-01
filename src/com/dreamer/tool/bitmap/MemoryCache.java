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
	
	// ���뻺���Ǹ�ͬ������
	
	// LinkedHashMap���췽�������һ������true�������map���Ԫ�ؽ��������ʹ�ô������ٵ������У���LRU
	
	// �����ĺô������Ҫ�������е�Ԫ���滻�����ȱ������������ʹ�õ�Ԫ�����滻�����Ч��
	public Object mute=new Object();
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	
	// ������ͼƬ��ռ�õ��ֽڣ���ʼ0����ͨ���˱����ϸ���ƻ�����ռ�õĶ��ڴ�
	
	private long size = 0;// current allocated size
	
	// ����ֻ��ռ�õ������ڴ�
	
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
	 * �ϸ���ƶ��ڴ棬��������������滻�������ʹ�õ��Ǹ�ͼƬ����
	 * 
	 */
	private synchronized void checkSize() {// һ�����ɾ������
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {//
			// �ȱ����������ʹ�õ�Ԫ��
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
	 * ͼƬռ�õ��ڴ�
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
