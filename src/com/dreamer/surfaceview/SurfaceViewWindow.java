package com.dreamer.surfaceview;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceViewWindow extends SurfaceView implements SurfaceHolder.Callback,Runnable {

	private boolean mLoop = false;
	SurfaceHolder mSurfaceHolder = null;
	private long sleepTime = 0;
	protected Object mutex = new Object();
	private long currentTime;
	private int fps = 0;
	private String TAG;
	
	public static int SV_WIDTH = 1280;
	public static int SV_HEIGHT = 720;
	protected Paint mPaint = new Paint();
	private Canvas mCanvas;
	public SVFrame mSVFrame;
	public SurfaceViewWindowCallback mSurfaceViewWindowCallback;
	private Context mContext;
	private SurfaceViewWindow mSurfaceViewWindow;
	
	public SurfaceViewWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mSurfaceViewWindow = this;
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		setFocusable(true);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.WHITE);
		mLoop = true;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(mLoop){
			try {
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				// TODO: handle exception
			}
			synchronized (mSurfaceHolder) {
				DrawSurfaceView();
			}
		}
	}

	private void DrawSurfaceView() {
		// TODO Auto-generated method stub
		//锁定画布
		if(mSurfaceHolder==null)
			return;
		mCanvas = mSurfaceHolder.lockCanvas();
		if(mCanvas == null)
			return;
		//每秒帧数
		drawFPS();
		//清屏
		mCanvas.drawRect(0, 0, SV_WIDTH, SV_HEIGHT, mPaint);
		//画所有SVViews
		OnDraw(mCanvas);
		mSurfaceHolder.unlockCanvasAndPost(mCanvas);
		
	}

	private void OnDraw(Canvas mCanvas) {
		// TODO Auto-generated method stub
		if(mSVFrame==null)
			return;
		mSVFrame.onDraw(mCanvas);
	}

	private void drawFPS() {
		// TODO Auto-generated method stub
		if(currentTime == 0){
			currentTime = System.currentTimeMillis();
		}
		fps  ++;
		if(System.currentTimeMillis()-currentTime>1000){
			Log.i(TAG, "SurfaceViewWindow draw fps:"+fps);
			fps = 0;
			currentTime = 0;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		new Thread(this).start();
		mSurfaceViewWindowCallback.notifySurfaceViewWindowCallback();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mLoop = false;
		mCanvas = null;
		mSurfaceHolder = null;
		fps = 0;
		currentTime = 0;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e("SurfaceViewWindow", "onKeyDown");
		queueEvent(event);
		return true;
	}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.e("SurfaceViewWindow", "onKeyUp");
		queueEvent(event);
		return true;
	}

	private void queueEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (mSVFrame == null)
			return;
		mSVFrame.dispatchKeyEvent(event);
	}

	public void setFrame(SVFrame frame) {
		// TODO Auto-generated method stub
		if (mSVFrame != null) {
			mSVFrame.mSurfaceViewWindow = null;
			mSVFrame = null;
		}
		mSVFrame = frame;
		mSVFrame.mSurfaceViewWindow = this;
	}
	
	public Paint getPaint(){
		return mPaint;
	}
	
	public interface SurfaceViewWindowCallback {
		
		public void notifySurfaceViewWindowCallback();
	}

	public void setSurfaceViewWindowCallback(
			SurfaceViewWindowCallback surfaceViewWindowCallback) {
		// TODO Auto-generated method stub
		this.mSurfaceViewWindowCallback = surfaceViewWindowCallback;
	}


	public void finish() {
		// TODO Auto-generated method stub
		if (mContext == null)
			return;
		mSurfaceViewWindow = null;
		((Activity)mContext).finish();
	}

}
