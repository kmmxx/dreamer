package com.dreamer.opengles.expend;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesImage;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesSurfaceView;
import com.dreamer.opengles.GlesToolkit;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.util.Log;

public class GlesImageSwitcher {

	private boolean hasAnimation = false;
	private int animY = 0;
	GlesImage[] imgs;
	int width;
	int height;
	private Align textAlign;
	private Context context;
	private int spaceY = 0;
	private int progress;
	private long switchTime = 5000;
	private long currentTime;
	private List<Bitmap> list;
	private int index = 0;
	private float animZ;
	private float spaceX = 353;
	private float animX;
	private int index1 = 1;
	private int scissorX = 80;
	private int scissorY = 320;
	private int scissorWidth = 495;
	private int scissorHeight = 88;
	private boolean showScissor = true;

	public GlesImageSwitcher(Context context, int width, int height) {
		this.context = context;
		this.height = height;
		this.width = width;
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		imgs = new GlesImage[3];
		for (int i = 0; i < 3; i++) {
			imgs[i] = new GlesImage(context, width, height);
		}
	}

	public int getScissorX() {
		return scissorX;
	}

	public void setScissorX(int scissorX) {
		this.scissorX = scissorX;
	}

	public long getSwitchTime() {
		return switchTime;
	}

	public void setSwitchTime(long switchTime) {
		this.switchTime = switchTime;
	}

	public int getSpaceY() {
		return spaceY;
	}

	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}

	public Align getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(Align textAlign) {
		this.textAlign = textAlign;
	}

	public void setImageList(List<Bitmap> bitmap) {
		list = bitmap;
		updateView();
	}

	private void updateView() {
		// TODO Auto-generated method stub
		if (list == null)
			return;
		imgs[0].setBackground(list.get(index), false);
		if (imgs.length > 1)
			imgs[1].setBackground(list.get(index1), false);
		imgs[2].setBackground(list.get(index1), false);
	}

	public int getAnimY() {
		return animY;
	}

	public void setAnimY(int animY) {
		this.animY = animY;
	}

	public boolean onDraw() {
		if (imgs == null)
			return false;
		GlesMatrix.pushMatrix();
		if (showScissor) {
			GLES20.glEnable(GL10.GL_SCISSOR_TEST);
			GLES20.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
			GLES20.glClearColor(0, 0, 0, 0f);
			GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			GlesMatrix.setProjecFrustumM(GlesSurfaceView.LEFT,
					GlesSurfaceView.RIGHT, GlesSurfaceView.BOTTOM,
					GlesSurfaceView.TOP, GlesSurfaceView.NEAR,
					GlesSurfaceView.FAR);
			GlesMatrix.setCamra(0f, 0f, GlesSurfaceView.NEAR, 0f, 0f, 0f, 0f,
					1f, 0f);
		}
		GlesMatrix.pushMatrix();
		if (!hasAnimation || imgs.length < 2) {
			GlesMatrix.translate(0, GlesToolkit.translateY(28), animZ);
			imgs[2].onDraw();
		} else {
			GlesMatrix.translate(GlesToolkit.translateX(animX),
					GlesToolkit.translateY(28 + animY), animZ);
			imgs[0].onDraw();
			GlesMatrix.translate(GlesToolkit.translateX(spaceX),
					GlesToolkit.translateY(spaceY), 0);
			imgs[1].onDraw();
		}
		GlesMatrix.popMatrix();
		if (showScissor) {
			GlesSurfaceView.setSurfaceViewColor();
			GLES20.glDisable(GL10.GL_SCISSOR_TEST);
		}
		GlesMatrix.popMatrix();
		moveAnimation();
		return true;
	}

	private void moveAnimation() {
		// TODO Auto-generated method stub
		if (hasAnimation) {
			animX -= spaceX / 50;
			progress++;
			if (progress > 50) {
				stopAnimation();
			}
		}
		if (currentTime == 0) {
			currentTime = SystemClock.uptimeMillis();
		}
		if (SystemClock.uptimeMillis() - currentTime > switchTime) {
			currentTime = 0;
			changeIndex(1);
			stopAnimation();
			startAnimation();
		}
	}

	private void changeIndex(int i) {
		// TODO Auto-generated method stub
		if (list == null)
			return;
		index += i;
		if (index > list.size() - 1) {
			index = 0;
		}
		index1 += i;
		if (index1 > list.size() - 1) {
			index1 = 0;
		}
		Log.d("changeIndex", "index:" + index + " index1:" + index1);
		updateView();
	}

	public void startAnimation() {
		hasAnimation = true;
		animY = 0;
		animX = 0;
		progress = 0;
	}

	public void stopAnimation() {
		hasAnimation = false;
		animY = 0;
		animX = 0;
		progress = 0;
	}

	public void setShowScissor(boolean b) {
		// TODO Auto-generated method stub
		showScissor = b;
	}

}
