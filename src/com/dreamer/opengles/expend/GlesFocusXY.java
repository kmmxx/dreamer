package com.dreamer.opengles.expend;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesImage;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesToolkit;
import com.dreamer.opengles.GlesView;

public class GlesFocusXY {

	private GlesImage foucsView;
	private boolean hasAnimation;
	private int indexX;
	private int indexY;
	private int indexXMax = 3;
	private int spaceX = 270;
	private int spaceY = 270;
	private int animX;
	private int progress;
	private float animY;
	final int DIRECTION_LEFT = 1;
	final int DIRECTION_RIGHT = 2;
	final int DIRECTION_UP = 3;
	final int DIRECTION_DOWN = 4;
	private int indexXLast;
	private int indexYLast;
	private int indexYMax = 1;
	private int directionX;
	private int directionY;
	public GlesFocusXY() {

	}

	public boolean onDraw() {
		if (foucsView == null)
			return false;
		GlesMatrix.pushMatrix();
		if (hasAnimation) {
			GlesMatrix.translate(
					GlesToolkit.translateX(indexXLast * spaceX + animX),
					GlesToolkit.translateY(indexYLast * spaceY + animY), 0);
		} else {
			GlesMatrix.translate(GlesToolkit.translateX(indexX * spaceX),
					GlesToolkit.translateY(indexY * spaceY), 0);
		}
		foucsView.onDraw();
		GlesMatrix.popMatrix();
		moveAnimation();
		return true;
	}

	public void setView(GlesView view) {
	}

	public GlesImage getFoucsView() {
		return foucsView;
	}

	public void setFoucsView(GlesImage foucsView) {
		this.foucsView = foucsView;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int index) {
		this.indexX = index;
	}

	public int getIndexMax() {
		return indexXMax;
	}

	public void setIndexMax(int indexMax) {
		this.indexXMax = indexMax;
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
			if (directionX == DIRECTION_RIGHT) {
				animX += spaceX / 10;
			} else if (directionX == DIRECTION_LEFT) {
				animX -= spaceX / 10;
			} else if (directionX == 0) {
				animX = 0;
			}
			if (directionY == DIRECTION_DOWN) {
				animY += spaceY / 10;
			} else if (directionY == DIRECTION_UP) {
				animY -= spaceY / 10;
			} else if (directionY == 0) {
				animY = 0;
			}
			progress++;
			if (progress > 10) {
				stopAnimation();
			}
		}
	}

	public void changeIndexX(int i) {
		// TODO Auto-generated method stub
		stopAnimation();
		if (i > 0) {
			directionX = DIRECTION_RIGHT;
		} else if (i < 0) {
			directionX = DIRECTION_LEFT;
		} else {
			directionX = 0;
		}
		indexXLast = indexX;
		indexX += i;
		if (indexX < 0) {
			// indexX = 0;
			// ((MyappView) view).changeScreen(1);
		} else if (indexX > indexXMax) {
			// indexX = indexXMax;
			// ((MyappView) view).changeScreen(-1);
			directionX = 0;
		} else {
			startAnimation();
		}
	}

	public void changeIndexY(int i) {
		// TODO Auto-generated method stub
		stopAnimation();
		if (i > 0) {
			directionY = DIRECTION_DOWN;
		} else if (i < 0) {
			directionY = DIRECTION_UP;
		} else {
			directionY = 0;
		}
		indexYLast = indexY;
		indexY += i;
		if (indexY < 0) {
			indexY = 0;
		} else if (indexY > indexYMax) {
			indexY = indexYMax;
		} else {
			startAnimation();
		}
	}

	public void startAnimation() {
		hasAnimation = true;
		animX = 0;
		animY = 0;
		progress = 0;
	}

	public void stopAnimation() {
		hasAnimation = false;
		animX = 0;
		animY = 0;
		progress = 0;
	}

	public void setSpaceY(int spaceY2) {
		// TODO Auto-generated method stub
		this.spaceY = spaceY2;
	}

	public int getIndexY() {
		// TODO Auto-generated method stub
		return indexY;
	}

	public void setIndexY(int i) {
		// TODO Auto-generated method stub
		indexY = 0;
	}
}
