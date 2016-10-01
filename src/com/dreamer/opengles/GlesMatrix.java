package com.dreamer.opengles;

import java.nio.FloatBuffer;

import android.opengl.Matrix;

public class GlesMatrix {

	private static float[] mProjMatrix = new float[16]; // 投影
	private static float[] mVMatrix = new float[16]; // 锟斤拷锟斤拷锟�
	private static float[] mMVPMatrix; // 锟斤拷锟秸变换
	private static float[] currentMatrix;// 锟斤拷前锟戒换

	public static float[] lightLocation = new float[] { 0, 0, 0 };// 锟斤拷源位锟斤拷
	public static FloatBuffer lightPositionFB;// 锟斤拷源锟斤拷锟斤拷
	public static float[] cameraLocation = new float[3];// 锟斤拷锟斤拷锟轿伙拷锟�
	public static FloatBuffer cameraFB;// 锟斤拷锟斤拷锟斤拷
	public static float[] lightDirection = new float[] { 0, 0, 1 };
	public static FloatBuffer lightDirectionFB;

	private static int maxStack = 10;
	static float[][] mStack = new float[maxStack][16];
	static int stackTop = -1;
	private static float alpha = 1f;

	public static void initCurrentMatrix() {
		currentMatrix = new float[16];
		Matrix.setRotateM(currentMatrix, 0, 0, 1, 0, 0);
	}

	public static float[] getCurrentMatrix() {
		return currentMatrix;
	}

	public static void setLightLocation(float x, float y, float z) {
		lightLocation[0] = x;
		lightLocation[1] = y;
		lightLocation[2] = z;
		lightPositionFB = GlesToolkit
				.createFloatBuffer(lightLocation.length * 4);
		lightPositionFB.put(lightLocation);
		lightPositionFB.position(0);
	}

	public static void setDirectLightLocation(float x, float y, float z) {
		lightDirection[0] = x;
		lightDirection[1] = y;
		lightDirection[2] = z;
		lightDirectionFB = GlesToolkit
				.createFloatBuffer(lightDirection.length * 4);
		lightDirectionFB.put(lightDirection);
		lightDirectionFB.position(0);
	}

	public static void pushMatrix() {
		stackTop++;
		if (stackTop > maxStack - 1) {
			maxStack = maxStack * 2;
			float[][] tmpStack = new float[maxStack][16];
			for (int i = 0; i < mStack.length; i++) {
				for (int j = 0; j < 16; j++) {
					tmpStack[i][j] = mStack[i][j];
				}
			}
			mStack = null;
			mStack = tmpStack;
		}
		for (int i = 0; i < 16; i++) {
			mStack[stackTop][i] = currentMatrix[i];
		}
	}

	public static void popMatrix() {
		if (stackTop < 0)
			return;
		for (int i = 0; i < 16; i++) {
			currentMatrix[i] = mStack[stackTop][i];
		}
		stackTop--;
	}

	public static void translate(float x, float y, float z) {
		Matrix.translateM(currentMatrix, 0, x, y, z);
	}

	public static void rotate(float angle, float x, float y, float z) {
		Matrix.rotateM(currentMatrix, 0, angle, x, y, z);
	}

	public static void scale(float x, float y, float z) {
		Matrix.scaleM(currentMatrix, 0, x, y, z);
	}

	public static float[] getFinalMatrix() {
		mMVPMatrix = new float[16];
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currentMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}

	public static float[] getViewProjMatrix() {
		float[] mx = new float[16];
		Matrix.multiplyMM(mx, 0, mProjMatrix, 0, mVMatrix, 0);
		return mx;
	}

	public static void setProjectOrtho(float left, float right, float bottom,
			float top, float near, float far) {
		Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	public static void setProjecFrustumM(float left, float right, float bottom,
			float top, float near, float far) {
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}

	public static void setCamra(float cx, float cy, float cz, float tx,
			float ty, float tz, float upx, float upy, float upz) {
		Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
		cameraLocation[0] = cx;
		cameraLocation[1] = cy;
		cameraLocation[2] = cz;
		cameraFB = GlesToolkit.createFloatBuffer(cameraLocation.length * 4);
		cameraFB.put(cameraLocation);
		cameraFB.position(0);
	}

	public static void setAlpha(float a) {
		// TODO Auto-generated method stub
		alpha  = a;
	}
	
	public static float getAlpha(){
		return alpha;
	}

}
