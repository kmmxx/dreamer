//package com.dreamer.opengles.expend;
//
//import java.util.List;
//
//import javax.microedition.khronos.opengles.GL10;
//
//import com.dreamer.opengles.GlesImage;
//import com.dreamer.opengles.GlesMatrix;
//import com.dreamer.opengles.GlesToolkit;
//
//import android.opengl.GLES20;
//
//
//public class GlesList {
//
//	private List<GlesImage> imgList;
//	int spaceX = 200;
//	int size = 0;
//	private boolean hasAnimation = false;
//	private int animX;
//	private int progress;
//	private int direction;
//	private int index;
//	private int indexMax = 4;
//
//	private float alpha = 1f;
//	private float animY;
//	private int updateDirection;
//	private String TAG;
//
//	public GlesList(int size) {
//		this.size = size;
//	}
//
//	public int getSpaceX() {
//		return spaceX;
//	}
//
//	public void setSpaceX(int spaceX) {
//		this.spaceX = spaceX;
//	}
//
//	public int getSize() {
//		return size;
//	}
//
//	public void setSize(int size) {
//		this.size = size;
//	}
//
//	public void setImageList(List<GlesImage> list) {
//		this.imgList = list;
//	}
//
//	public void setTextList(List<GlesAreaText> list) {
//	}
//
//	public void changeIndex(int i, boolean b) {
//		// TODO Auto-generated method stub
//		updateDirection = 0;
//		stopAnimation();
//		index += i;
//		Mlog.d(TAG, "sub-index:" + index);
//		if (index < 0) {
//			index = 0;
//			direction = 1;
//			updateDirection = -1;
//		} else if (index > indexMax) {
//			index = indexMax;
//			direction = -1;
//			updateDirection = 1;
//		} else {
//			updateDirection = 0;
//			direction = 0;
//		}
//		if (b)
//			startAnimation();
//	}
//
//	int d = 1;
//	int d1 = -1;
//	private int animY1;
//	private float scaleZ = 0f;
//	private float scaleLastZ;
//	private float alphaLast;
//	private boolean isFocus = false;
//	private boolean lostFocusAnimation = false;
//	private int lostFocusProgress;
//	private float lostAlpha = 1.0f;
//	private int lostDir = 1;
//
//	private void moveAnimation() {
//		// TODO Auto-generated method stub
//		if (hasAnimation) {
//			scaleZ += 0.02f;
//			scaleLastZ -= 0.02f;
//			alpha += 0.02f;
//			if (direction > 0) {
//				animX += spaceX / 10;
//				progress += 1;
//				if (progress > 10) {
//					stopAnimation();
//				}
//			} else if (direction < 0) {
//				animX -= spaceX / 10;
//				progress += 1;
//				if (progress > 10) {
//					stopAnimation();
//				}
//			} else {
//				animX = 0;
//				if (animY < -10) {
//					d = 1;
//				}
//				if (animY > 10) {
//					d = -1;
//				}
//				animY += d * Math.abs((30 - animY)) / 4;
//				if (animY1 < -10) {
//					d1 = 1;
//				}
//				if (animY1 > 10) {
//					d1 = -1;
//				}
//				animY1 += d1 * Math.abs((30 - animY1)) / 4;
//				progress += 1;
//				if (progress > 10) {
//					stopAnimation();
//				}
//			}
//		}
//		if (lostFocusAnimation) {
//			if (lostDir > 0) {
//				lostAlpha += 0.01f;
//			} else {
//				lostAlpha -= 0.01;
//			}
//			lostFocusProgress++;
//			if (lostFocusProgress > 5) {
//				stopLostFocusAnimation();
//			}
//		}
//	}
//
//	public void startAnimation() {
//		hasAnimation = true;
//		animX = 0;
//		animY = 0;
//		animY1 = 0;
//		scaleZ = 1f;
//		scaleLastZ = 1.2f;
//		alpha = 1f;
//		alphaLast = 1.1f;
//		progress = 0;
//		d = 1;
//		d = -1;
//	}
//
//	public void stopAnimation() {
//		if (mUpdateViewCallback != null) {
//			mUpdateViewCallback.updateMainView(updateDirection);
//		}
//		hasAnimation = false;
//		animX = 0;
//		animY = 0;
//		animY1 = 0;
//		scaleZ = 1.2f;
//		scaleLastZ = 1.0f;
//		progress = 0;
//		alpha = 1.2f;
//		alphaLast = 1f;
//		d = 1;
//		d1 = -1;
//	}
//
//	private void startLostFocusAnimation() {
//		lostFocusAnimation = true;
//		lostFocusProgress = 0;
//		if (lostDir > 0) {
//			lostAlpha = 1.0f;
//		} else {
//			lostAlpha = 1.05f;
//		}
//	}
//
//	private void stopLostFocusAnimation() {
//		lostFocusAnimation = false;
//		lostFocusProgress = 0;
//		if (lostDir > 0) {
//			lostAlpha = 1.05f;
//		} else {
//			lostAlpha = 1.0f;
//		}
//	}
//
//	public boolean onDraw() {
//		GlesMatrix.pushMatrix();
//		GlesMatrix.translate(GlesToolkit.translateX(-15),
//				GlesToolkit.translateY(-13), 0);
//		GlesObjectManager.main_bot.onDraw();
//		GlesMatrix.popMatrix();
//
//		for (int i = 0; i < size; i++) {
//			GlesMatrix.pushMatrix();
//			if (lostFocusAnimation
//					&& GlesObjectManager.main_bot.getIndex() == i) {
//				GlesMatrix.scale(lostAlpha, lostAlpha, 1f);
//				GlesMatrix.setAlpha(lostAlpha);
//			}
//			if (!hasAnimation) {
//				GlesMatrix.translate(GlesToolkit.translateX(i * spaceX),
//						GlesToolkit.translateY(0), 0);
//				if (imgList.get(i) != null) {
//					if (GlesObjectManager.main_bot.getIndex() == i && isFocus
//							&& !lostFocusAnimation) {
//						GlesMatrix.scale(1.1f, 1.1f, 1f);
//						GlesMatrix.setAlpha(1.2f);
//					}
//					imgList.get(i).onDraw();
//					if (imgList.get(i).getTextureId() == -1) {
//						GlesObjectManager.main_bot_def.onDraw();
//					}
//				}
//				GlesMatrix.setAlpha(1.0f);
//			} else {
//				GlesMatrix.translate(
//						GlesToolkit.translateX(i * spaceX + animX),
//						GlesToolkit.translateY(0), 0);
//				if (GlesObjectManager.main_bot.getIndex() == i
//						&& !lostFocusAnimation) {
//					// GlesMatrix.translate(0, GlesToolkit.translateY(animY),
//					// 0);
//					GlesMatrix.setAlpha(alpha);
//					GlesMatrix.scale(scaleZ, scaleZ, 1f);
//				} else if (GlesObjectManager.main_bot.getLastIndex() == i
//						&& !lostFocusAnimation) {
//					GlesMatrix.setAlpha(alphaLast);
//					GlesMatrix.scale(scaleLastZ, scaleLastZ, 1f);
//				} else {
//					GlesMatrix.setAlpha(1f);
//				}
//				if (imgList.get(i) != null) {
//					imgList.get(i).onDraw();
//					if (imgList.get(i).getTextureId() == -1) {
//						GlesObjectManager.main_bot_def.onDraw();
//					}
//				}
//			}
//			GlesMatrix.setAlpha(1.0f);
//			GlesMatrix.popMatrix();
//		}
//		moveAnimation();
//		return true;
//	}
//
//	private UpdateViewCallback mUpdateViewCallback;
//
//	public void setUpdateViewCallback(UpdateViewCallback b) {
//		this.mUpdateViewCallback = b;
//	}
//
//	public interface UpdateViewCallback {
//		public void updateMainView(int i);
//	}
//
//	public int getIndex() {
//		// TODO Auto-generated method stub
//		return index;
//	}
//
//	public boolean isFocus() {
//		return isFocus;
//	}
//
//	public void setFocus(boolean isFocus) {
//		this.isFocus = isFocus;
//		if (isFocus)
//			lostDir = 1;
//		else {
//			lostDir = -1;
//		}
//		startLostFocusAnimation();
//	}
//
//}
