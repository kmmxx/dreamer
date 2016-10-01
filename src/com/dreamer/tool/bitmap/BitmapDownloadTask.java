/**
 * Seachange-vod
 */
package com.dreamer.tool.bitmap;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author Zengk 2012-10-24
 */
public class BitmapDownloadTask {

	String mKey;
	String mDownloadUrl;
	BitMapCallback mCallback;
	Runnable mRunnable;
	int indexpos;// ¸Ä³É 0, main £¬ 1 secound  2 RecommendViewMagage
	public BitmapDownloadTask(String key, String downloadUrl,int indexpos) {
		mKey = key;
		mDownloadUrl = downloadUrl;
		mRunnable = new Download();
	    this.indexpos=indexpos;
	}

	public void setCallback(BitMapCallback callback) {
		mCallback = callback;
	}

	protected Runnable getRunnable() {
		return mRunnable;
	}

	class Download implements Runnable {
		public void run() {
			boolean success = false;
			Bitmap bitmap = null;
			try {
				if(mDownloadUrl==null){
					
				}else{
					System.out.println("-----------Download-----------start---");
					System.out.println("-----------Download-----------start---mDownloadUrl="+mDownloadUrl);
					/*if(mDownloadUrl.indexOf("172.30.64.99:9090")!=-1){
						mDownloadUrl="http://119.44.223.40:61111"+mDownloadUrl.split("172.30.64.99:9090")[1];
					}*/
					URL url = new URL(mDownloadUrl);
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.connect();
					if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
						InputStream is = http.getInputStream();
						bitmap = BitmapFactory.decodeStream(is);
						is.close();
						
						success=true;
					}else{
						success=false;
					}
					System.out.println("-----------Download-----------end---");
				}
				
				
			} catch (Exception e) {
			} finally {
				if (mCallback == null)
					return;
				mCallback.notifyCallback(BitmapDownloadTask.this, success, bitmap,mKey,indexpos);
				Thread.currentThread().interrupt(); 
			}
		}
	}
  
	public interface BitMapCallback {
		public void notifyCallback(BitmapDownloadTask task, boolean success,
				Bitmap bitmap,String mKey,int index);
	}
}