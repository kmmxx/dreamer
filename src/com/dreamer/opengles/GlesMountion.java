package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

public class GlesMountion extends GlesObject {

	protected  int mProgram;
	protected  int muMVPMatrixHandle;
	protected  int maPositionHandle;
	protected  int muMMatrixHandle;
	protected  int maColorHandle;
	protected  String mVertexShader;
	protected  String mFragmentShader;

	 int maNormalHandle;// 锟斤拷锟姐法锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 int maLightLocationHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 int maCameraHandle;// 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 int maTextCoorHandle;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�?	static int maLightDirectionHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷

	protected Context context;
	protected boolean update = true;
	private String TAG = GlesMountion.class.getSimpleName();
	int textureId = -1;
	private int textureId1 = -1;
	private float[] vertices;
	private float[] textCoor;
	private float[] colors;
	private float[] normals;
	 boolean isFirst = true;

	private float[][] yArray;
	private float land_lowest = -2f;
	private float land_highest = 20f;
	private int rows = 0;
	private int cols = 0;
	private float sMax = 8.0f;
	private float tMax = 8.0f;
	private Bitmap landBitmap;
	private int sTextureGrassHandle;
	private int sTextureRockHandle;
	private int landStartYHandle;
	private int landYSpanHandle;
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private int rsidLandform = -1;
	private int rsid = -1;
	private int rsid1 = -1;

	public GlesMountion(Context ctx, float[] vertices) {
		super();
		this.vertices = vertices;
		this.context = ctx;
	}

	public GlesMountion(Context ctx) {
		super();
		this.context = ctx;
	}

	public void initGlesMountion(Bitmap bitmap) {
		this.landBitmap = bitmap;
		update = true;
	}

	public void initGlesMountion(Context ctx, int rsid) {
		synchronized (mutex) {
			rsidLandform = rsid;
			update = true;
		}
	}

	public float[] generateTexCoor(int bw, int bh) {
		float[] result = new float[bw * bh * 6 * 2];
		float sizew = sMax / bw;
		float sizeh = tMax / bh;
		int c = 0;
		for (int i = 0; i < bh; i++) {
			for (int j = 0; j < bw; j++) {
				float s = j * sizew;
				float t = i * sizeh;

				result[c++] = s;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s + sizew;
				result[c++] = t;

				result[c++] = s;
				result[c++] = t + sizeh;

				result[c++] = s + sizew;
				result[c++] = t + sizeh;
			}
		}
		return result;
	}

	private float[] generateVertices() {
		// TODO Auto-generated method stub
		if (landBitmap == null)
			return null;
		yArray = GlesLoadUtil.loadLandforms(landBitmap);
		rows = yArray.length - 1;
		cols = yArray[0].length - 1;
		float[] vertices = new float[cols * rows * 2 * 3 * 3];
		int count = 0;
		for (int j = 0; j < rows; j++) {
			for (int i = 0; i < cols; i++) {
				float zsx = -UNIT_SIZE * cols / 2 + i * UNIT_SIZE;
				float zsz = -UNIT_SIZE * rows / 2 + j * UNIT_SIZE;
				vertices[count++] = zsx;
				vertices[count++] = yArray[j][i];
				vertices[count++] = zsz;

				vertices[count++] = zsx;
				vertices[count++] = yArray[j + 1][i];
				vertices[count++] = zsz + UNIT_SIZE;

				vertices[count++] = zsx + UNIT_SIZE;
				vertices[count++] = yArray[j][i + 1];
				vertices[count++] = zsz;

				vertices[count++] = zsx + UNIT_SIZE;
				vertices[count++] = yArray[j][i + 1];
				vertices[count++] = zsz;

				vertices[count++] = zsx;
				vertices[count++] = yArray[j + 1][i];
				vertices[count++] = zsz + UNIT_SIZE;

				vertices[count++] = zsx + UNIT_SIZE;
				vertices[count++] = yArray[j + 1][i + 1];
				vertices[count++] = zsz + UNIT_SIZE;
			}
		}
		return vertices;
	}

	public GlesMountion(Context ctx, float[] vertices, float[] normals,
			float[] textCoors) {
		this.vertices = vertices;
		this.context = ctx;
		this.textCoor = textCoors;
		this.normals = normals;
	}

	public float getsMax() {
		return sMax;
	}

	public void setsMax(float sMax) {
		this.sMax = sMax;
	}

	public float gettMax() {
		return tMax;
	}

	public void settMax(float tMax) {
		this.tMax = tMax;
	}

	public float getUNIT_SIZE() {
		return UNIT_SIZE;
	}

	public void setUNIT_SIZE(float uNIT_SIZE) {
		UNIT_SIZE = uNIT_SIZE;
	}

	public float getLand_lowest() {
		return land_lowest;
	}

	public void setLand_lowest(float land_lowest) {
		this.land_lowest = land_lowest;
	}

	public float getLand_highest() {
		return land_highest;
	}

	public void setLand_highest(float land_highest) {
		this.land_highest = land_highest;
	}

	public void setBackground(Bitmap bitmap) {
		synchronized (mutex) {
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

	public void setBackground(Context ctx, int rsid, int rsid1) {
		synchronized (mutex) {
			this.rsid = rsid;
			this.rsid1 = rsid1;
			update = true;
		}
	}

	
	public  void initStaticValue(){
		isFirst = true;
	}
	
	private void initShader(Context context) {
		// TODO Auto-generated method stub
		vertices = generateVertices();
		textCoor = generateTexCoor(rows, cols);
		initVertexData();
		if (landBitmap != null)
			landBitmap.recycle();
		if (isFirst) {
			mVertexShader = GlesToolkit.loadFromAssetsFile("mount_vertex.sh",
					context.getResources());
			mFragmentShader = GlesToolkit.loadFromAssetsFile("mount_frag.sh",
					context.getResources());
			mProgram = GlesToolkit.createProgram(
					GlesMountion.class.getSimpleName(), mVertexShader,
					mFragmentShader);
			if (mProgram == 0) {
				mProgram = GlesToolkit.getGlesObjectProgram(context);
			}
			maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
			maLightLocationHandle = GLES20.glGetAttribLocation(mProgram,
					"uLightLocation");
			maCameraHandle = GLES20.glGetAttribLocation(mProgram, "uCamera");
			maTextCoorHandle = GLES20
					.glGetAttribLocation(mProgram, "aTextCoor");
			sTextureGrassHandle = GLES20.glGetAttribLocation(mProgram,
					"sTextureGrass");
			sTextureRockHandle = GLES20.glGetAttribLocation(mProgram,
					"sTextureRock");
			landStartYHandle = GLES20.glGetAttribLocation(mProgram,
					"landStartY");
			landYSpanHandle = GLES20.glGetAttribLocation(mProgram, "landYSpan");

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
			mVertexBuffer.put(vertices);
			mVertexBuffer.position(0);
		}

		if (mTextCoorBuffer == null) {
			mTextCoorBuffer = GlesToolkit
					.createFloatBuffer(textCoor.length << 2);
			mTextCoorBuffer.put(textCoor);
			mTextCoorBuffer.position(0);
		}

		if (mNormalBuffer == null) {
			mNormalBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
			mNormalBuffer.put(vertices);
			mNormalBuffer.position(0);
		}
	}

	public boolean onDraw() {
		if (update) {
			if (rsidLandform > 0) {
				this.landBitmap = GlesToolkit.getBitmap(context, rsidLandform);
				rsidLandform = -1;
			}
			if (rsid > 0 && rsid1 > 0) {
				this.bitmap = GlesToolkit.getBitmap(context, rsid);
				this.bitmap1 = GlesToolkit.getBitmap(context, rsid1);
				rsid = -1;
				rsid1 = -1;
			}
			if (bitmap != null && bitmap1 != null) {
				textureId = GlesToolkit.genTexture(bitmap, true,
						GLES20.GL_NEAREST, GLES20.GL_LINEAR);
				textureId1 = GlesToolkit.genTexture(bitmap1, true,
						GLES20.GL_NEAREST, GLES20.GL_LINEAR);
				// textureId = GlesToolkit.genTexture(bitmap, true,
				// GLES20.GL_LINEAR_MIPMAP_NEAREST,
				// GLES20.GL_LINEAR_MIPMAP_LINEAR);
				// textureId1 = GlesToolkit.genTexture(bitmap1, true,
				// GLES20.GL_LINEAR_MIPMAP_NEAREST,
				// GLES20.GL_LINEAR_MIPMAP_LINEAR);
				if (bitmap != null)
					bitmap.recycle();
				if (bitmap1 != null)
					bitmap1.recycle();
			}
			initShader(context);
			update = false;
		}
		if (textureId == -1 || textureId1 == -1) {
			return false;
		}
		GlesMatrix.pushMatrix();
		GlesMatrix.rotate(180, 1, 0, 0);
		GlesMatrix.translate(-GlesSurfaceView.LEFT, -GlesSurfaceView.TOP, 0);
		GLES20.glUseProgram(mProgram);
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false,
				GlesMatrix.getCurrentMatrix(), 0);
		GLES20.glUniform3fv(maCameraHandle, 1, GlesMatrix.cameraFB);
		// GLES20.glUniform3fv(maLightDirectionHandle, 1,
		// GlesMatrix.lightDirectionFB);
		GLES20.glUniform3fv(maLightLocationHandle, 1,
				GlesMatrix.lightPositionFB);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				GlesMatrix.getFinalMatrix(), 0);
		GLES20.glUniform1i(sTextureGrassHandle, 0);
		GLES20.glUniform1i(sTextureRockHandle, 1);
		GLES20.glUniform1f(landStartYHandle, 0);
		GLES20.glUniform1f(landYSpanHandle, 30);
		GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false,
				3 * 4, mNormalBuffer);
		GLES20.glVertexAttribPointer(maTextCoorHandle, 2, GLES20.GL_FLOAT,
				false, 2 * 4, mTextCoorBuffer);
		GLES20.glEnableVertexAttribArray(maLightLocationHandle);
		GLES20.glEnableVertexAttribArray(maNormalHandle);
		GLES20.glEnableVertexAttribArray(maPositionHandle);
		GLES20.glEnableVertexAttribArray(maTextCoorHandle);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId1);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
		// GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vCount);
		// GlesMatrix.translate(GlesSurfaceView.LEFT, GlesSurfaceView.TOP, 0);
		GlesMatrix.popMatrix();
		return true;
	}

	@Override
	public void onRecyle() {
		// TODO Auto-generated method stub
		if (textureId != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId }, 0);
			textureId = -1;
		}
		if (textureId1 != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId1 }, 0);
			textureId1 = -1;
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
