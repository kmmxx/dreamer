package com.dreamer.opengles.expend;

import android.util.Log;

import com.dreamer.opengles.GlesImage;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesToolkit;

public class GlesFocus {

	private GlesImage foucsView;
	private boolean hasAnimation;
	private int index;
	private int indexMax = 3;
	private int spaceX = 270;
	private int spaceY = 0;
	private int animX;
	private int progress;
	private float animY;
	private int direction;
	private float tmpX = animX;
	private int indexLast;
	private boolean isVisible = true;
	private GlesImage secondFocus;
	private float alpha = 1f;
	private boolean hasAlphaAnimation = false;
	private long current = 0;
	private int progressAlpha = 0;
	private int alphaDir = -1;
	private float scaleZ;

	public GlesFocus() {

	}

	public void setSecondFocus(GlesImage view) {
		this.secondFocus = view;
	}

	public boolean onDraw() {
		if (foucsView == null)
			return false;
		GlesMatrix.pushMatrix();
		if (hasAnimation) {
			GlesMatrix.translate(
					GlesToolkit.translateX(indexLast * spaceX + animX),
					GlesToolkit.translateY(animY), 0);
			GlesMatrix.scale(scaleZ, scaleZ, 1f);
		} else {
			GlesMatrix.translate(GlesToolkit.translateX(index * spaceX),
					GlesToolkit.translateY(index * spaceY + animY), 0);
		}
		// if (hasAlphaAnimation) {
		// GlesMatrix.setAlpha(alpha);
		// }
		GlesMatrix.setAlpha(alpha);
		if (isVisible) {
			foucsView.onDraw();
			if (secondFocus != null) {
				GlesMatrix.translate(0, GlesToolkit.translateY(111), 0);
				// secondFocus.onDraw();
			}
		}
		GlesMatrix.setAlpha(1.0f);
		GlesMatrix.popMatrix();
		moveAnimation();
		moveAlphaAnimation();
		return true;
	}

	public GlesImage getFoucsView() {
		return foucsView;
	}

	public void setFoucsView(GlesImage foucsView) {
		this.foucsView = foucsView;
	}

	public int getIndex() {
		return index;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
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
				animX += 0.5f * (spaceX - animX);
			} else {
				animX -= 0.5f * (spaceX - Math.abs(animX));
			}
			if (progress < 5) {
				scaleZ -= 0.02f;
			} else {
				scaleZ += 0.04f;
			}
			progress++;
			if (progress > 10) {
				stopAnimation();
			}
		}
	}

	public void startAnimation() {
		hasAnimation = true;
		animX = 0;
		progress = 0;
		scaleZ = 1f;
	}

	public void stopAnimation() {
		hasAnimation = false;
		animX = 0;
		progress = 0;
		scaleZ = 1f;
	}

	private void startAlphaAnimation() {
		hasAlphaAnimation = true;
		progressAlpha = 0;
		// alpha = 0.5f;
	}

	private void stopAlphaAnimation() {
		hasAlphaAnimation = false;
		progressAlpha = 0;
		// alpha = 1f;
		if (alphaDir > 0) {
			alpha = 1f;
		} else {
			alpha = 0.5f;
		}
		alphaDir = -alphaDir;
		if (alphaDir > 0) {
			startAlphaAnimation();
		}
	}

	private void moveAlphaAnimation() {
		if (hasAlphaAnimation) {
			if (alphaDir > 0) {
				alpha += 0.01f;
			} else {
				alpha -= 0.01f;
			}
			progressAlpha++;
			if (progressAlpha > 50) {
				stopAlphaAnimation();
			}
		}
		if (current == 0) {
			current = System.currentTimeMillis();
		}
		if (System.currentTimeMillis() - current > 3000) {
			current = 0;
			if (!hasAnimation)
				startAlphaAnimation();
		}
	}

	public void changeIndex(int i) {
		// TODO Auto-generated method stub
		if(i==0){
			indexLast = index;
			stopAnimation();
			startAnimation();
			return;
		}
		stopAnimation();
		Log.d("GlesFocus", "changeIndex0:" + i);
		this.direction = i;
		indexLast = index;
		index += i;
		if (index < 0) {
			index = 0;
		} else if (index > indexMax) {
			index = indexMax;
		} else {
			startAnimation();
		}
	}

	public void changeIndex1(int i) {
		stopAnimation();
		this.direction = i;
		indexLast = index;
		index += i;
		if (index == indexMax && i > 0) {
			index = indexMax;
			return;
		}
		if (index < 0) {
			index = 0;
			return;
		}
		if (index < 0) {
			index = 0;
		} else if (index > indexMax) {
			index = indexMax;
			if (mUpdateViewCallback != null) {
				mUpdateViewCallback.updateView();
			}
		} else {
			startAnimation();
		}
	}

	private UpdateViewCallback mUpdateViewCallback;

	public void setUpdateViewCallback(UpdateViewCallback b) {
		this.mUpdateViewCallback = b;
	}

	public interface UpdateViewCallback {
		public void updateView();
	}

	public int getLastIndex() {
		// TODO Auto-generated method stub
		return indexLast;
	}
}
