package com.dreamer.opengles;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public abstract class GlesObject extends Gles{
	String TAG = GlesObject.class.getSimpleName();
	protected Object mutex = new Object();

	static int objectCount = 0;
	protected int vCount = 0;
	protected FloatBuffer mVertexBuffer;
	protected FloatBuffer mColorBuffer;

	protected float UNIT_SIZE = 1.0f;

	protected FloatBuffer mTextCoorBuffer;
	protected FloatBuffer mNormalBuffer;
	protected float[] vertices;
	protected float[] colors;
	protected float[] textCoor;
	protected boolean isRecycle = true;


	public GlesObject() {
		objectCount++;
		// Mlog.d(TAG , "all objectCount:"+objectCount);
	}

	public static int getAllGlesObjectNum() {
		return objectCount;
	}


	public abstract boolean onDraw();

	public abstract void onRecyle();

}
