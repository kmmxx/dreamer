package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

public class GlesFrameAnimation {

	GlesObject[] rects;
	private int frame = 0;
	private long currentTime;
	private int size = 0;
	private long frameTime = 50;
	private boolean hasAnimatoin = false;
	private GlesObject defaultFrame;
	private String TAG = GlesFrameAnimation.class.getSimpleName();

	public GlesFrameAnimation() {

	}

	public GlesFrameAnimation(GlesObject[] rects) {
		this.rects = rects;
		if(rects!=null&&rects.length>0){
			setDefaultFrame(rects[0]);
		}
		size = rects.length;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public GlesObject getDefaultFrame() {
		return defaultFrame;
	}

	public void setDefaultFrame(GlesObject defaultFrame) {
		this.defaultFrame = defaultFrame;
	}

	public void onRecyle() {
		for (int i = 0; i < size; i++) {
			rects[i].onRecyle();
		}
		rects = null;
		size = 0;
		frame = 0;
		currentTime = 0;
		frameTime = 50;
	}

	public void startAnimation() {
		hasAnimatoin = true;
		frame = 0;
		currentTime = 0;
	}

	public void stopAnimation() {
		hasAnimatoin = false;
		frame = 0;
		currentTime = 0;
	}

	public long getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(long frameTime) {
		this.frameTime = frameTime;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public GlesObject[] getRects() {
		return rects;
	}

	public void setRects(GlesObject[] rects) {
		this.rects = rects;
		size = rects.length;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public boolean onDraw() {
		if (rects == null || rects.length == 0) {
			return false;
		}
		if (hasAnimatoin) {
			GlesMatrix.pushMatrix();
			if (frame > size - 1) {
				frame = 0;
			}
			rects[frame].onDraw();
			GlesMatrix.popMatrix();
			if (currentTime == 0) {
				currentTime = SystemClock.uptimeMillis();
			}
			if (SystemClock.uptimeMillis() - currentTime > frameTime) {
				currentTime = 0;
				frame++;
			}
		}else{
			if(defaultFrame!=null){
				defaultFrame.onDraw();
			}
		}
		return true;
	}
}
