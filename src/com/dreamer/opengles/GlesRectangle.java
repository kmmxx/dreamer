package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GlesRectangle extends GlesObject {

	protected static int mProgram;
	protected static int muMVPMatrixHandle;
	protected static int maPositionHandle;
	protected static int maColorHandle;
	protected static String mVertexShader;
	protected static String mFragmentShader;

	protected static int maNormalHandle;// 閿熸枻鎷烽敓濮愭硶閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
	protected static int maLightLocationHandle; // 閿熸枻鎷锋簮浣嶉敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
	protected static int maCameraHandle;// 閿熸枻鎷烽敓鏂ゆ嫹閿熻娇浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
	protected static int maTextCoorHandle;// 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓锟�	protected static int maLightDirectionHandle; // 閿熸枻鎷锋簮浣嶉敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
											// 閿熸枻鎷锋簮浣嶉敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
	protected float[] vertices;
	protected static float[] colors;
	protected static float[] textCoor = new float[] { 0.f, 0.f, 0.f, 1.f, 1.f,
			0.f, 1.f, 1.f };
	protected static boolean isFirst = true;

	protected Context context;
	protected boolean update = true;
	protected boolean fontStyleNormal;
	private int alphaHandle;
	private String TAG = GlesRectangle.class.getSimpleName();

	protected int textureId = -1;

	Bitmap bitmap;
	private int rsid = -1;
	private static float alpha = 1.0f;

	public GlesRectangle(Context ctx, float width, float height) {
		super();
		this.width = width;
		this.height = height;
		this.context = ctx;
	}
	
	public static void initStaticValue(){
		isFirst = true;
	}
	
	
	public void setFontStyleNormal(boolean b){
		this.fontStyleNormal = b;
	}

	public GlesRectangle(Context ctx, int width, int height) {
		super();
		this.width = GlesToolkit.translateWidth(width);
		this.height = GlesToolkit.translateHeight(height);
		this.context = ctx;
	}

	public void setWidthHeight(int width, int height, int rsid) {
		synchronized (mutex) {
			this.rsid = rsid;
			this.width = GlesToolkit.translateWidth(width);
			this.height = GlesToolkit.translateHeight(height);
			update = true;
		}
	}
	
	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public void setFloatWidthHeight(float width, float height, int rsid) {
		synchronized (mutex) {
			this.rsid = rsid;
			this.width = GlesToolkit.translateWidth(width);
			this.height = GlesToolkit.translateHeight(height);
			update = true;
		}
	}

	public void setWidthHeight(int width, int height) {
		synchronized (mutex) {
			this.width = GlesToolkit.translateWidth(width);
			this.height = GlesToolkit.translateHeight(height);
			update = true;
		}
	}

	public void setWidthHeight(float width, float height, int rsid) {
		synchronized (mutex) {
			this.rsid = rsid;
			this.width = width;
			this.height = height;
			update = true;
		}
	}

	public void setBackground(Bitmap bitmap) {
		synchronized (mutex) {
			this.bitmap = bitmap;
			update = true;
		}
	}
	
	public void setFloatWidthHeight(float width, float height, Bitmap bitmap) {
		synchronized (mutex) {
			this.bitmap = bitmap;
			this.width = GlesToolkit.translateWidth(width);
			this.height = GlesToolkit.translateHeight(height);
			update = true;
		}
	}
	

	public void setBackground(Context ctx, int rsid) {
		synchronized (mutex) {
			this.rsid = rsid;
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

	public void setBackground(Context ctx, int rsid, boolean b) {
		synchronized (mutex) {
			this.isRecycle = b;
			this.rsid = rsid;
			update = true;
		}
	}

	private void initShader() {
		// TODO Auto-generated method stub
		initVertexData();
		if (isFirst) {
			mVertexShader = GlesToolkit.loadFromAssetsFile("rect_vertex.sh",
					context.getResources());
			mFragmentShader = GlesToolkit.loadFromAssetsFile("rect_frag.sh",
					context.getResources());
			mProgram = GlesToolkit.createProgram(
					GlesRectangle.class.getSimpleName(), mVertexShader,
					mFragmentShader);
			if (mProgram == 0) {
				mProgram = GlesToolkit.getGlesObjectProgram(context);
			}
			// maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			// maLightLocationHandle = GLES20.glGetAttribLocation(mProgram,
			// "uLightLocation");
			// maLightDirectionHandle = GLES20.glGetAttribLocation(mProgram,
			// "uLightDirection");
			alphaHandle = GLES20.glGetUniformLocation(mProgram, "alpha");
			maTextCoorHandle = GLES20
					.glGetAttribLocation(mProgram, "aTextCoor");
			maCameraHandle = GLES20.glGetAttribLocation(mProgram, "uCamera");
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			// maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			isFirst = false;
		}

	}

	protected void initVertexData() {
		vertices = new float[] { 0.f, 0.f, 0.f, 0.f, height, 0.f, width, 0.f,
				0.f, width, height, 0.f };
		vCount = vertices.length / 3;
		if (mVertexBuffer == null)
			mVertexBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		if (mTextCoorBuffer == null)
			mTextCoorBuffer = GlesToolkit
					.createFloatBuffer(textCoor.length << 2);
		mTextCoorBuffer.put(textCoor);
		mTextCoorBuffer.position(0);
	}

	public boolean onDraw() {
		synchronized (mutex) {
			if (update) {
				releaseTexture();
				if (rsid > 0) {
					this.bitmap = GlesToolkit.getBitmap(context, rsid);
					rsid = -1;
				}
				if (bitmap != null) {
					textureId = GlesToolkit.genTexture(bitmap, true,
							GLES20.GL_NEAREST, GLES20.GL_LINEAR, isRecycle);
					if (bitmap != null && isRecycle)
						bitmap.recycle();
				}
				if (textureId == -1) {
					update = false;
					return false;
				}
				initShader();
				update = false;
			}
			if (textureId == -1) {
				return false;
			}
			GLES20.glDisable(GLES20.GL_DEPTH_TEST);
			GLES20.glUseProgram(mProgram);
			if(mGlesAnimation!=null){
				mGlesAnimation.onDraw();
			}
			// GlesMatrix.pushMatrix();
			// GlesMatrix.initCurrentMatrix();
			// GlesMatrix.rotate(180, 1, 0, 0);
			// GlesMatrix.translate(-GlesSurfaceView.LEFT, -GlesSurfaceView.TOP,
			// 0);
			// GLES20.glUniform3fv(maLightDirectionHandle, 1,
			// GlesMatrix.lightDirectionFB);
			// GLES20.glUniform3fv(maCameraHandle, 1, GlesMatrix.cameraFB);
			// GLES20.glUniform3fv(maLightLocationHandle, 1,
			// GlesMatrix.lightPositionFB);
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					GlesMatrix.getFinalMatrix(), 0);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			// GLES20.glVertexAttribPointer(maColorHandle, 3, GLES20.GL_FLOAT,
			// false,
			// 4 * 4, mColorBuffer);
			GLES20.glVertexAttribPointer(maTextCoorHandle, 2, GLES20.GL_FLOAT,
					false, 2 * 4, mTextCoorBuffer);
			GLES20.glUniform1f(alphaHandle, GlesMatrix.getAlpha());
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			// GLES20.glEnableVertexAttribArray(maColorHandle);
			// GLES20.glEnableVertexAttribArray(maLightLocationHandle);
			GLES20.glEnableVertexAttribArray(maTextCoorHandle);
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			// GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount);
			GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			// GlesMatrix.popMatrix();
			return true;
		}
	}
	
	public void releaseTexture(){
		if (textureId != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId }, 0);
			GlesToolkit.removeTextureId(textureId);
			textureId = -1;
		}
	}

	@Override
	public void onRecyle() {
		// TODO Auto-generated method stub
		if (textureId != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId }, 0);
			GlesToolkit.removeTextureId(textureId);
			textureId = -1;
		}
		if (mVertexBuffer != null) {
			mVertexBuffer.clear();
			mVertexBuffer = null;
		}
	}

}
