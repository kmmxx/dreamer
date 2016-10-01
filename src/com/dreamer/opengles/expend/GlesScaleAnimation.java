package com.dreamer.opengles.expend;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesSurfaceView;

public class GlesScaleAnimation {

	public static final int SCALE_TO_DISAPPEAR = 0x01;
	public static final int SCALE_TO_APPEAR = 0x02;
	public static final int ALPHA_TO_APPEAR = 0x03;
	public static final int ALPHA_TO_DISAPPEAR = 0x04;
	
	private boolean start = false;
	private GlesScaleAnimationCallback mGlesAnimationCallback;
	private float scaleZ ;
	private int flagAnimation = GlesScaleAnimation.SCALE_TO_DISAPPEAR;
	private float progressScaleZ = GlesSurfaceView.FAR/30;
	
	public void startAnimation(int flag){
		switch(flag){
		case SCALE_TO_DISAPPEAR:
			scaleZ = GlesSurfaceView.NEAR;
			break;
		case SCALE_TO_APPEAR:
			scaleZ = GlesSurfaceView.FAR;
			break;
		case ALPHA_TO_APPEAR:
			
			break;
		case ALPHA_TO_DISAPPEAR:
			break;
		}
	}
	
	public void startScaleAnimation(int flagAnimation){
		this.flagAnimation = flagAnimation;
		if(flagAnimation == SCALE_TO_DISAPPEAR){
			scaleZ = GlesSurfaceView.NEAR;
		}else{
			scaleZ = GlesSurfaceView.FAR;
		}
		start  = true;
	}
	
	
	public void stopScaleAnimation(int scaleToDisappear){
		if(scaleToDisappear == SCALE_TO_DISAPPEAR){
			scaleZ = GlesSurfaceView.FAR;
		}else if(scaleToDisappear == SCALE_TO_APPEAR){
			scaleZ = GlesSurfaceView.NEAR;
		}
		start = false;
	}

	public void scaleAnimation() {
		if (flagAnimation == SCALE_TO_DISAPPEAR) {
			scaleZ = scaleZ + progressScaleZ ;
			if (scaleZ >= GlesSurfaceView.FAR) {
				if (mGlesAnimationCallback != null) {
					mGlesAnimationCallback
							.onGlesScaleAnimationDown(SCALE_TO_DISAPPEAR);
				}
				stopScaleAnimation(SCALE_TO_DISAPPEAR);
			}
		} else {
			scaleZ = scaleZ - progressScaleZ;
			if (scaleZ <= GlesSurfaceView.NEAR) {
				stopScaleAnimation(SCALE_TO_APPEAR);
				if (mGlesAnimationCallback != null) {
					mGlesAnimationCallback.onGlesScaleAnimationDown(SCALE_TO_APPEAR);
				}
			}
		}
	}
	

	public float getProgressScaleZ() {
		return progressScaleZ;
	}

	public void setProgressScaleZ(float progressScaleZ) {
		this.progressScaleZ = progressScaleZ;
	}

	public void setGlesScaleAnimationCallback(GlesScaleAnimationCallback call) {
		this.mGlesAnimationCallback = call;
	}

	public interface GlesScaleAnimationCallback {

		public void onGlesScaleAnimationDown(int flag);
	}

	public void onDraw() {
		// TODO Auto-generated method stub
		if(start){
			scaleAnimation();
			GlesMatrix.setCamra(0f, 0f, scaleZ , 0f, 0f, 0f, 0f, 1f, 0f);
		}
	}

	public boolean hasAnimation() {
		// TODO Auto-generated method stub
		return start;
	}
}
