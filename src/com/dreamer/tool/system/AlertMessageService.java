package com.dreamer.tool.system;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class AlertMessageService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
//		return new IAlertMessageService.Stub();
		return null;
	}

	@Override
	public void onDestroy() {
		dismissOverlay();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null){
			String msg = intent.getStringExtra("message");
			dismissOverlay();
			View view = null;
			showOverlay(msg,view);
		}
		return START_NOT_STICKY;
	}
	
	private View mOverlay;
	
	private void dismissOverlay(){
		if(mOverlay != null){
			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			wm.removeView(mOverlay);
			mOverlay = null;
		}
	}

	private void showOverlay(String msg,View view) {
		LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				0,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.CENTER;
		
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(view, params);
		mOverlay = view;
	}

}
