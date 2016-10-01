package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GlesVerticeObject extends GlesObject {

	protected int mProgram;
	protected int muMVPMatrixHandle;
	protected int maPositionHandle;
	protected int muMMatrixHandle;
	protected int maColorHandle;
	protected String mVertexShader;
	protected String mFragmentShader;

	int maNormalHandle;// 锟斤拷锟姐法锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	int maLightLocationHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	int maCameraHandle;// 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	int maTextCoorHandle;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�? static
							// int maLightDirectionHandle; //
							// 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷

	protected Context context;
	protected boolean update = true;
	private String TAG = GlesVerticeObject.class.getSimpleName();
	int textureId = -1;
	private float[] vertices;
	private float[] textCoor;
	private float[] colors;
	private float[] normals;
	Bitmap bitmap;
	private int rsid = -1;
	boolean isFirst = true;

	public GlesVerticeObject(Context ctx, float[] vertices) {
		super();
		this.vertices = vertices;
		this.context = ctx;
	}

	public void initStaticValue() {
		isFirst = true;
	}

	public GlesVerticeObject(Context ctx, float[] vertices, float[] normals,
			float[] textCoors) {
		super();
		this.vertices = vertices;
		this.context = ctx;
		this.textCoor = textCoors;
		this.normals = normals;
	}

	public void setBackground(Bitmap bitmap) {
		synchronized (mutex) {
			this.bitmap = bitmap;
			update = true;
		}
	}

	public void setBackground(Bitmap bitmap, boolean b) {
		synchronized (mutex) {
			this.isRecycle = b;
			this.bitmap = bitmap;
			update = true;
		}
	}

	public void setBackground(Context ctx, int rsid) {
		synchronized (mutex) {
			this.rsid = rsid;
			update = true;
		}
	}

	private void initShader(Context context) {
		// TODO Auto-generated method stub
		initVertexData();
		if (isFirst) {
			mVertexShader = GlesToolkit.loadFromAssetsFile("vertex_color.sh",
					context.getResources());
			mFragmentShader = GlesToolkit.loadFromAssetsFile("frag_color.sh",
					context.getResources());
			mProgram = GlesToolkit.createProgram(
					GlesVerticeObject.class.getSimpleName(), mVertexShader,
					mFragmentShader);
			if (mProgram == 0) {
				mProgram = GlesToolkit.getGlesObjectProgram(context);
			}
			// maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// maLightLocationHandle = GLES20.glGetAttribLocation(mProgram,
			// "uLightLocation");
			// maCameraHandle = GLES20.glGetAttribLocation(mProgram, "uCamera");
			// maTextCoorHandle = GLES20
			// .glGetAttribLocation(mProgram, "aTextCoor");
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			// maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
			muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
			isFirst = false;
		}

	}

	protected void initVertexData() {
		// TODO Auto-generated method stub
		vCount = vertices.length / 3;
		if (mVertexBuffer == null) {
			mVertexBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
		}
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		if (textCoor == null) {
			textCoor = new float[] { 0.f, 0.f, 0.f, 1.f, 1.f, 0.f, 1.f, 1.f };
		}
		if (mTextCoorBuffer == null) {
			mTextCoorBuffer = GlesToolkit
					.createFloatBuffer(textCoor.length << 2);
		}
		mTextCoorBuffer.put(textCoor);
		mTextCoorBuffer.position(0);
		if (mNormalBuffer == null) {
			mNormalBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
		}
		mNormalBuffer.put(vertices);
		mNormalBuffer.position(0);
	}

	public boolean onDraw() {
		synchronized (mutex) {
			if (update) {
				if (rsid > 0) {
					this.bitmap = GlesToolkit.getBitmap(context, rsid);
					rsid = -1;
				}
				if (bitmap != null) {
					textureId = GlesToolkit.genTexture(bitmap, true,
							GLES20.GL_NEAREST, GLES20.GL_LINEAR);
					if (bitmap != null)
						bitmap.recycle();
				}
				initShader(context);
				update = false;
			}
			if (textureId == -1) {
				return false;
			}
			GlesMatrix.pushMatrix();
			GlesMatrix.rotate(180, 1, 0, 0);
			GlesMatrix
					.translate(-GlesSurfaceView.LEFT, -GlesSurfaceView.TOP, 0);
			GLES20.glUseProgram(mProgram);
			GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
					GlesMatrix.getCurrentMatrix(), 0);
			// GLES20.glUniform3fv(maCameraHandle, 1, GlesMatrix.cameraFB);
			// GLES20.glUniform3fv(maLightDirectionHandle, 1,
			// GlesMatrix.lightDirectionFB);
			// GLES20.glUniform3fv(maLightLocationHandle, 1,
			// GlesMatrix.lightPositionFB);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					GlesMatrix.getFinalMatrix(), 0);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			// GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT,
			// false, 3 * 4, mNormalBuffer);
			// GLES20.glVertexAttribPointer(maTextCoorHandle, 2,
			// GLES20.GL_FLOAT,
			// false, 2 * 4, mTextCoorBuffer);
			// GLES20.glEnableVertexAttribArray(maLightLocationHandle);
			// GLES20.glEnableVertexAttribArray(maNormalHandle);
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			// GLES20.glEnableVertexAttribArray(maTextCoorHandle);
			// GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			// GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
			// GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount);
			GlesMatrix.popMatrix();
			return true;
		}
	}

	@Override
	public void onRecyle() {
		// TODO Auto-generated method stub
		if (textureId != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId }, 0);
			textureId = -1;
		}
		if (mVertexBuffer != null) {
			mVertexBuffer.clear();
			mVertexBuffer = null;
		}
		if (mTextCoorBuffer != null) {
			mTextCoorBuffer.clear();
			mTextCoorBuffer = null;
		}
		if (mNormalBuffer != null) {
			mNormalBuffer.clear();
			mNormalBuffer = null;
		}
	}

}
