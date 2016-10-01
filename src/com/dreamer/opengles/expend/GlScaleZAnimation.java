package com.dreamer.opengles.expend;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesView;

public class GlScaleZAnimation {

	private GlesView view;
	private float scaleZ = 100f;
	private int scaleDir = -1;
	private float scaleProgress = 10f;
	private boolean hasScaleAnimation = true;
	GlScacelZAnimationCallback mGlScacelZAnimationCallback;

	public GlScaleZAnimation(GlesView view) {
		this.view = view;
	}

	public void onDraw(GL10 gl) {
		GlesMatrix.translate(0, 0, scaleZ);
		changeScaleZ(scaleDir);
	}

	public void setGlScacelZAnimationCallback(
			GlScacelZAnimationCallback mGlScacelZAnimationCallback) {
		this.mGlScacelZAnimationCallback = mGlScacelZAnimationCallback;
	}

	private void changeScaleZ(int i) {
		// TODO Auto-generated method stub
		if (i == -1) {
			if (hasScaleAnimation) {
				scaleZ -= scaleProgress;
				if (scaleZ <= 0) {
					scaleZ = 0f;
					hasScaleAnimation = false;
				}
			}
		}
		if (i == 1) {
			if (hasScaleAnimation) {
				scaleZ += scaleProgress;
				if (scaleZ >= 100f) {
					scaleZ = 100f;
					scaleDir = -1;
					hasScaleAnimation = true;
					view.setVisible(false);
					if (mGlScacelZAnimationCallback != null)
						mGlScacelZAnimationCallback.doActionWhenDisappeared();
				}
			}
		}

	}

	public float getScaleProgress() {
		return scaleProgress;
	}

	public void setScaleProgress(float scaleProgress) {
		this.scaleProgress = scaleProgress;
	}

	public void viewScaleToDisappear() {
		scaleDir = 1;
		scaleZ = 0f;
		hasScaleAnimation = true;
	}

	public void viewScaleToAppear() {
		scaleZ = 100f;
		scaleDir = -1;
		hasScaleAnimation = true;
	}

	public interface GlScacelZAnimationCallback {
		public void doActionWhenDisappeared();
	}

	public GlesView getView() {
		return view;
	}

	public void setView(GlesView view) {
		this.view = view;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}

	public int getScaleDir() {
		return scaleDir;
	}

	public void setScaleDir(int scaleDir) {
		this.scaleDir = scaleDir;
	}

}
