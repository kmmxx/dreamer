package com.dreamer.opengles;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GlesSurfaceView extends GLSurfaceView {

	public static final String TAG = "GLES2SurfaceView";
	private final float TOUCH_SCALE_FACTOR = 180.0f / 360;
	private static float red = 0;
	private static float green = 1;
	private static float blue = 0;
	private static float alpha = 0f;

	public static float RADIO = 1.7777778f; // 透锟斤拷锟斤拷锟斤拷冶呓锟�
	public static float LEFT = -RADIO; // 透锟斤拷锟斤拷锟斤拷冶呓锟�
	public static float RIGHT = RADIO; // 透锟斤拷锟斤拷锟斤拷冶呓锟�
	public static final float BOTTOM = -1.0f; // 透锟斤拷锟斤拷锟斤拷卤呓锟�
	public static final float TOP = 1.0f; // 透锟斤拷锟斤拷锟斤拷媳呓锟�
	public static final float NEAR = 10.0f; // 透锟斤拷锟斤拷慕锟竭斤拷
	public static final float FAR = 1000.0f; // 透锟斤拷锟斤拷锟皆讹拷呓锟�

	protected static boolean BUSY = false; // 系统锟斤拷忙
	protected static final int SLEEP = 10; // 锟斤拷染锟接筹拷

	protected static int FULL_SCREEN_WIDTH = Global.TV_WIDTH; // 锟接口的匡拷
	protected static int FULL_SCREEN_HEIGHT = Global.TV_HEIGHT; // 锟接口的革拷
	private static boolean isVisible = true;

	protected boolean isFirst = true;

	GlesTriangle mTriangle;
	public long startTime = -1;
	public int fps = 0;
	private GLES2SurfaceViewCallback mGLES2SurfaceViewCallback = null;
	public GlesFrame mGLES2Frame = null;
	private Object mutex = new Object();
	public boolean mInitFinish = false;
	public float sunX = 40;
	public float sunY = 5;
	public float sunZ = 20;
	public int srcFator = GLES20.GL_SRC_ALPHA;
	public int dstFator = GLES20.GL_ONE_MINUS_SRC_ALPHA;
	GlesSurfaceView mGlesSurfaceView;
	private GlesFrame frontFrame;
	private GlesFrame bgFrame;

	public GlesSurfaceView(Context context) {
		super(context);
		mGlesSurfaceView = this;
		this.setEGLContextClientVersion(2);
		this.setRenderer(new GLES2Renderer());
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		getHolder().setFormat(PixelFormat.RGBA_8888);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		setKeepScreenOn(true);
	}

	public static boolean isVisible() {
		return isVisible;
	}

	public static void setVisible(boolean b) {
		isVisible = b;
	}

	public void onDestroy() {
		if (mGlesSurfaceView != null) {
			// GlesLine.initStaticValue();
			// GlesLoadedObject.initStaticValue();
			// GlesMountion.initStaticValue();
			// GlesPoint.initStaticValue();
			// GlesTriangle.initStaticValue();
			// GlesVerticeObject.initStaticValue();
			GlesRectangle.initStaticValue();
			mGlesSurfaceView = null;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		queueEvent(new KeyEventRunnable(event));
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		queueEvent(new KeyEventRunnable(event));
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		queueEvent(new TouchEventRunnable(event));
		return true;
	}

	protected class TouchEventRunnable implements Runnable {

		MotionEvent mMotionEvent;

		TouchEventRunnable(MotionEvent motionEvent) {
			mMotionEvent = motionEvent;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (mGLES2Frame == null) {
				return;
			}
			mGLES2Frame.mGlesSurfaceView = GlesSurfaceView.this;
			mGLES2Frame.dispatchTouchEvent(mMotionEvent);
		}

	}

	protected class KeyEventRunnable implements Runnable {

		KeyEvent mKeyEvent;

		KeyEventRunnable(KeyEvent event) {
			mKeyEvent = event;
		}

		public void run() {
			if (mGLES2Frame == null)
				return;
			mGLES2Frame.mGlesSurfaceView = GlesSurfaceView.this;
			mGLES2Frame.dispatchKeyEvent(mKeyEvent);
		}
	}

	public static void setRGBA(float red, float green, float blue, float alpha) {
		GlesSurfaceView.red = red;
		GlesSurfaceView.green = green;
		GlesSurfaceView.blue = blue;
		GlesSurfaceView.alpha = alpha;
	}

	public void setGLES2Frame(GlesFrame frame) {
		synchronized (mutex) {
			if (!mInitFinish) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
				}
			}
			if (mGLES2Frame != null) {
				mGLES2Frame.mGlesSurfaceView = null;
				mGLES2Frame = null;
			}
			mGLES2Frame = frame;
			mGLES2Frame.setVisible(true);
			mGLES2Frame.mGlesSurfaceView = this;
		}
	}

	public GlesFrame getGLES2Frame() {
		return mGLES2Frame;
	}

	public void setGles2BgFrame(GlesFrame frame) {
		this.bgFrame = frame;
	}

	public void setGles2FrontFrame(GlesFrame frame) {
		this.frontFrame = frame;
	}

	private boolean onDrawFrames(GL10 gl, GlesFrame frame) {
		synchronized (mutex) {
			if (isVisible) {
				if (bgFrame != null) {
					bgFrame.onDraw();
				}
				if (frame != null) {
					frame.onDraw();
				}
				if (frontFrame != null) {
					frontFrame.onDraw();
				}
			}
		}
		return true;
	}

	private class GLES2Renderer implements GLSurfaceView.Renderer {

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub

			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			}
			fps++;
			if (BUSY) {
				try {
					Thread.sleep(SLEEP);
				} catch (InterruptedException e) {
				}
			}
			GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT
					| GLES20.GL_COLOR_BUFFER_BIT);
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(LEFT, TOP, 0);
			GlesMatrix.rotate(180, 1, 0, 0);

			onDrawFrames(gl, mGLES2Frame);
			GlesMatrix.popMatrix();
			if (System.currentTimeMillis() - startTime >= 1000) {
				mGLES2SurfaceViewCallback.surfaceDrawFrameFPS(fps);
				fps = 0;
				startTime = -1;
			}

		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			FULL_SCREEN_WIDTH = width;
			FULL_SCREEN_HEIGHT = height;
			Global.setGlobalWH(FULL_SCREEN_WIDTH, FULL_SCREEN_HEIGHT);
			GLES20.glViewport(0, 0, FULL_SCREEN_WIDTH, FULL_SCREEN_HEIGHT);
			RADIO = (float) width / height;
			LEFT = -RADIO;
			RIGHT = RADIO;
			Log.d(TAG, "onSurfaceChanged:" + width + ",height:" + height
					+ ",radio:" + RADIO + ",left:" + LEFT + ",right:" + RIGHT);
			// sunX=(float)(Math.cos(Math.toRadians(TOUCH_SCALE_FACTOR))*100);
			// //
			// sunY=(float)(Math.cos(Math.toRadians(TOUCH_SCALE_FACTOR))*100);
			// sunZ=-(float)(Math.sin(Math.toRadians(TOUCH_SCALE_FACTOR))*100);
			GlesMatrix.setLightLocation(sunX, sunY, sunZ);
			GlesMatrix.setProjecFrustumM(LEFT, RIGHT, BOTTOM, TOP, NEAR, FAR);
			GlesMatrix.setCamra(0f, 0f, NEAR, 0f, 0f, 0f, 0f, 1f, 0f);
			GlesMatrix.initCurrentMatrix();
			synchronized (mutex) {
				mInitFinish = true;
				mutex.notifyAll();
			}
			if (isFirst) {
				mGLES2SurfaceViewCallback.initGlSurfaceWindow();
				isFirst = false;
			}
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			GLES20.glClearColor(red, green, blue, alpha);
			// GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			// GLES20.glEnable(GLES20.GL_CULL_FACE);
			GLES20.glDisable(GLES20.GL_CULL_FACE);

			GLES20.glEnable(GLES20.GL_BLEND);
			GLES20.glBlendFunc(srcFator, dstFator);
			// GLES20.glBlendFunc(GLES20.GL_SRC_COLOR,
			// GLES20.GL_ONE_MINUS_SRC_COLOR);
		}

	}

	public void setLightPosition(float x, float y, float z) {
		this.sunX = x;
		this.sunY = y;
		this.sunZ = z;
	}

	public void setBlendFunc(int sfator, int dfator) {
		srcFator = sfator;
		dstFator = dfator;
	}

	public static void setSurfaceViewColor() {
		GLES20.glClearColor(red, green, blue, alpha);
	}

	public void setGLES2SurfaceViewCallback(
			GLES2SurfaceViewCallback mGlSurfaceWindowCallback) {
		// TODO Auto-generated method stub
		this.mGLES2SurfaceViewCallback = mGlSurfaceWindowCallback;
	}

	public interface GLES2SurfaceViewCallback {

		public void initGlSurfaceWindow();

		public void surfaceDrawFrameFPS(int fps);
	}

}
