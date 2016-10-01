package com.dreamer.opengles.expend;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesFrameAnimation;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesToolkit;

public class GlesFocusView {

	private GlesFrameAnimation foucsView;
	private boolean hasAnimation;
	private int index;
	private int indexMax = 3;
	private int spaceX = 270;
	private int animX;
	private int progress;
	private float animY;
	private int direction;
	private float tmpX = animX;
	private int indexLast;

	public GlesFocusView() {

	}

	public boolean onDraw() {
		if (foucsView == null)
			return false;
		GlesMatrix.pushMatrix();
		if (hasAnimation) {
			GlesMatrix.translate(
					GlesToolkit.translateX(indexLast * 270 + animX),
					GlesToolkit.translateY(animY), 0);
		} else {
			GlesMatrix.translate(GlesToolkit.translateX(index * 270),
					GlesToolkit.translateY(animY), 0);
		}
		foucsView.onDraw();
		GlesMatrix.popMatrix();
		moveAnimation();
		return true;
	}

	public GlesFrameAnimation getFoucsView() {
		return foucsView;
	}

	public void setFoucsView(GlesFrameAnimation foucsView) {
		this.foucsView = foucsView;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndexMax() {
		return indexMax;
	}

	public void setIndexMax(int indexMax) {
		this.indexMax = indexMax;
	}

	public int getSpaceX() {
		return spaceX;
	}

	public void setSpaceX(int spaceX) {
		this.spaceX = spaceX;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	private void moveAnimation() {
		// TODO Auto-generated method stub
		if (hasAnimation) {
			if (direction > 0) {
				animX += spaceX / 10;
			} else {
				animX -= spaceX / 10;
			}
			progress++;
			if (progress > 10) {
				stopAnimation();
			}
		}
	}

	public void changeIndex(int i) {
		// TODO Auto-generated method stub
		stopAnimation();
		this.direction = i;
		indexLast = index;
		index += i;
		if (index == indexMax && i > 0) {
			index = indexMax;
//			view.changeScreen(-1);
			return;
		}
		if (index < 1) {
			index = 0;
//			view.changeScreen(1);
			return;
		}
		if (index < 0) {
			index = 0;
		} else if (index > indexMax) {
			index = indexMax;
		} else {
			startAnimation();
		}
	}

	public void startAnimation() {
		hasAnimation = true;
		animX = 0;
		progress = 0;
	}

	public void stopAnimation() {
		hasAnimation = false;
		animX = 0;
		progress = 0;
	}
}
