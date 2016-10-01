package com.dreamer.opengles.expend;

import javax.microedition.khronos.opengles.GL10;


import com.dreamer.opengles.GlesFrameManager;
import com.dreamer.opengles.GlesLoadedObject;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesSurfaceView;
import com.dreamer.opengles.GlesView;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

public class ScissorGlesView extends GlesView {

	Context context;
	private String TAG = ScissorGlesView.class.getSimpleName();
	private int indexX = 0;
	private int indexY = 0;
	private GlesLoadedObject loadObject;
	private float anglePersonX = 0;
	private float anglePersonY = 0;
	private float anglePersonZ = 0;
	private int scissorX = 200;
	private int scissorY = 100;
	private int scissorWidth = 1000;
	private int scissorHeight = 800;
	private float red = 0;
	private float green = 0.8f;
	private float blue = 0;
	private float alpha = 1.0f;

	public ScissorGlesView(Context ctx) {
		context = ctx;
		loadObject = GlesObjectManager.getInstance().getGlesLoadedObject();
	}

	protected boolean onKeyUp(int keyCode, KeyEvent event) {
		return true;
	}

	protected boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			android.os.Process.killProcess(android.os.Process.myPid());
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_PAGE_UP:
			anglePersonZ -= 10;
			return true;
		case KeyEvent.KEYCODE_PAGE_DOWN:
			anglePersonZ += 10;
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			anglePersonY -= 10;
			indexX -= 1;
			Log.d(TAG, "indexX:" + indexX);
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Log.d(TAG, "indexX:" + indexX);
			anglePersonY += 10;
			indexX += 1;
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			anglePersonX -= 10;
			indexY -= 1;
			// GlesMatrix.setCamra(0f, 0f - 0.1f * indexY, 10f - indexY * 0.1f,
			// 0f, 0f, 0f, 0f, 1f, 0f);
			Log.d(TAG, "indexY:" + indexY);
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			anglePersonX += 10;
			// GlesMatrix.setCamra(0f, 0f + 0.1f * indexY, 10f - indexY * 0.1f,
			// 0f, 0f, 0f, 0f, 1f, 0f);
			indexY += 1;
			Log.d(TAG, "indexY:" + indexY);
			return true;
		case KeyEvent.KEYCODE_MENU:
			GlesFrameManager.getInstance().getCurrentFrame()
					.setDefaultFocusable(0);
			return true;
		}
		return false;
	}

	public boolean onDraw(GL10 gl) {
		// TODO Auto-generated method stub
		GlesMatrix.pushMatrix();
		GLES20.glEnable(GL10.GL_SCISSOR_TEST);
		GLES20.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
		GLES20.glClearColor(red, green, blue, alpha);
		GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		GlesMatrix.setProjecFrustumM(GlesSurfaceView.LEFT,
				GlesSurfaceView.RIGHT, GlesSurfaceView.BOTTOM,
				GlesSurfaceView.TOP, GlesSurfaceView.NEAR, GlesSurfaceView.FAR);
		GlesMatrix.setCamra(0f, 0f, GlesSurfaceView.NEAR, 0f, 0f, 0f, 0f, 1f,
				0f);
		if (loadObject != null) {
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(0f, 0.2f, 900f);
			GlesMatrix.rotate(anglePersonX, 1, 0, 0);
			GlesMatrix.rotate(anglePersonY, 0, 1, 0);
			GlesMatrix.rotate(anglePersonZ, 0, 0, 1);
			loadObject.onDraw();
			GlesMatrix.popMatrix();
		}
		GlesSurfaceView.setSurfaceViewColor();
		GLES20.glDisable(GL10.GL_SCISSOR_TEST);
		GlesMatrix.popMatrix();
		return true;
	}

	public int getScissorX() {
		return scissorX;
	}

	public void setScissorX(int scissorX) {
		this.scissorX = scissorX;
	}

	public int getScissorY() {
		return scissorY;
	}

	public void setScissorY(int scissorY) {
		this.scissorY = scissorY;
	}

	public int getScissorWidth() {
		return scissorWidth;
	}

	public void setScissorWidth(int scissorWidth) {
		this.scissorWidth = scissorWidth;
	}

	public int getScissorHeight() {
		return scissorHeight;
	}

	public void setScissorHeight(int scissorHeight) {
		this.scissorHeight = scissorHeight;
	}

	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	
	
}
