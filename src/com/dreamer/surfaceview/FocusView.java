package com.dreamer.surfaceview;

/**
 * Launcher.java
 * TODO
 */

import javax.microedition.khronos.opengles.GL10;


import android.app.Activity;
import android.graphics.Canvas;

public class FocusView{

	private Activity mActivity;
	private String TAG = "FocusView";
	private float srcX = 0f;
	private float srcY = 0f;
	private float dstX = srcX;
	private float dstY = srcY;
	private float paddingX = 0f;
	private float paddingY = 0f;
	private float tmpX = srcX;
	private float tmpY = srcY;
	private int indexX = 0;
	private int indexY = 0;
	private float tmpProgress = 0f;
	private boolean isFocusMove = false;
	private float moveProgress = 0.5f;
	SVImageView focusView ;
	SetSrcCallback mSetSrcCallback;

	public FocusView(SVImageView imageView) {
		focusView = imageView;
	}
	
	// ------------------------- 属性设置 ------------------------- 
	public void setDirX(float srcX,float srcY,float paddingX, int indexX){
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingX(paddingX);
		setIndexX(indexX);
	}
	
	public void setDirY(float srcX,float srcY,float paddingY, int indexY){
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingY(paddingY);
		setIndexY(indexY);
	}
	
	public void setDirXY(float srcX,float srcY,float paddingX,float paddingY, int indexX,int indexY){
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingX(paddingX);
		setPaddingY(paddingY);
		setIndexX(indexX);
		setIndexY(indexY);
	}

	private void moveFocus() {
		// TODO Auto-generated method stub
		dstX = srcX + paddingX * indexX;
		dstY = srcY + paddingY * indexY;
		if (tmpX != dstX||tmpY!=dstY)
			isFocusMove = true;
		if (isFocusMove) {
			tmpProgress += 0.1f;
			tmpX += moveProgress  * (dstX - tmpX);
			tmpY += moveProgress * (dstY - tmpY);
			if (tmpProgress > 1.0f) {
				tmpProgress = 0f;
				tmpX = dstX;
				isFocusMove = false;
			}
		}
	}

	// ------------------------- 绘制过程 ------------------------- //
	public boolean onDraw(Canvas canvas) {
		focusView.setXY(tmpX, tmpY);
		focusView.onDraw(canvas);
		moveFocus();
		return true;
	}

	public float getSrcX() {
		return srcX;
	}

	public void setSrcX(float srcX) {
		this.srcX = srcX;
		tmpX = srcX;
	}

	public float getSrcY() {
		return srcY;
	}

	public void setSrcY(float srcY) {
		this.srcY = srcY;
		tmpY = srcY;
	}

	public float getDstX() {
		return dstX;
	}

	public void setDstX(float dstX) {
		this.dstX = dstX;
	}

	public float getDstY() {
		return dstY;
	}

	public void setDstY(float dstY) {
		this.dstY = dstY;
	}

	public float getPaddingX() {
		return paddingX;
	}

	public void setPaddingX(float paddingX) {
		this.paddingX = paddingX;
	}

	public float getPaddingY() {
		return paddingY;
	}

	public void setPaddingY(float paddingY) {
		this.paddingY = paddingY;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public float getMoveProgress() {
		return moveProgress;
	}

	public void setMoveProgress(float moveProgress) {
		this.moveProgress = moveProgress;
	}

	public void setIndexXY(int indexX2, int indexY2) {
		// TODO Auto-generated method stub
		this.indexX = indexX2;
		this.indexY = indexY2;
	}

	public void setCallback(SetSrcCallback mSetSrcCallback){
		this.mSetSrcCallback = mSetSrcCallback;
	}
	
	public interface SetSrcCallback{
		public void setSrcXY(float x,float y);
	}

}