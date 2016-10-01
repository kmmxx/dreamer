package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;

public class GlesLine extends GlesObject {

	protected static int mProgram;
	protected static int muMVPMatrixHandle;
	protected static int maPositionHandle;
	protected static int maColorHandle;
	protected static String mVertexShader;
	protected static String mFragmentShader;

	static int maNormalHandle;// 锟斤拷锟姐法锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	static int maLightLocationHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	static int maCameraHandle;// 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	static int maTextCoorHandle;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�?	static int maLightDirectionHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private Context context;
	private boolean update = true;
	private  boolean isFirst = true;
	float[] point1;
	float[] point2;
	float[] line;
	float[] colors;
	private String TAG = GlesLine.class.getSimpleName();

	public GlesLine(Context ctx) {
		super();
		this.context = ctx;
	}
	
	public  void initStaticValue(){
		isFirst = true;
	}

	public void setLinePoint(float[] p1, float[] p2) {
		synchronized (mutex) {
			this.point1 = p1;
			this.point2 = p2;
			vertices = new float[] { p1[0], p1[1], p1[2], p2[0], p2[1], p2[2] };
			update = true;
		}
	}

	public void setLinePoint(float[] l) {
		synchronized (mutex) {
			this.line = l;
			point1 = new float[] { l[0], l[1], l[2] };
			point2 = new float[] { l[3], l[4], l[5] };
			vertices = new float[] { l[0], l[1], l[2], l[3], l[4], l[5] };
			update = true;
		}
	}

	public void setLinePoint(int[] l) {
		float[] tmp = new float[] { GlesToolkit.translateX(l[0]),
				GlesToolkit.translateY(l[1]), GlesToolkit.translateZ(l[2]),
				GlesToolkit.translateX(l[3]), GlesToolkit.translateY(l[4]),
				GlesToolkit.translateZ(l[5]), };
		setLinePoint(tmp);
	}

	public void setLinePoint(int[] p1, int[] p2) {
		synchronized (mutex) {
			if (p1 != null && p1.length == 3) {
				this.point1 = new float[] { GlesToolkit.translateX(p1[0]),
						GlesToolkit.translateY(p1[1]),
						GlesToolkit.translateZ(p1[2]), };
			}
			if (p2 != null && p2.length == 3) {
				this.point2 = new float[] { GlesToolkit.translateX(p2[0]),
						GlesToolkit.translateY(p2[1]),
						GlesToolkit.translateZ(p2[2]), };
			}
			vertices = new float[] { point1[0], point1[1], point1[2],
					point2[0], point2[1], point2[2] };
			update = true;
		}
	}

	public void setLineColors(float[] c) {
		synchronized (mutex) {
			this.colors = c;
			update = true;
		}
	}

	public float[] getLineColors() {
		return colors;
	}

	public float[] getLinePoints() {
		return line;
	}

	public void initShader() {
		// TODO Auto-generated method stub
		if (isFirst) {
			mVertexShader = GlesToolkit.loadFromAssetsFile("vertex_color.sh",
					context.getResources());
			mFragmentShader = GlesToolkit.loadFromAssetsFile("frag_color.sh",
					context.getResources());
			mProgram = GlesToolkit.createProgram(
					GlesLine.class.getSimpleName(), mVertexShader,
					mFragmentShader);
			if (mProgram == 0) {
				mProgram = GlesToolkit.getGlesObjectProgram(context);
			}
			maPositionHandle = GLES20
					.glGetAttribLocation(mProgram, "aPosition");
			maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
			muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
					"uMVPMatrix");
			isFirst = false;
		}
	}

	private void initVertexData() {
		// TODO Auto-generated method stub
		if (vertices == null)
			vertices = new float[] { point1[0], point1[1], point1[2],
					point2[0], point2[1], point2[2] };
		vCount = vertices.length / 3;
		if (mVertexBuffer == null)
			mVertexBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		if (colors == null)
			colors = new float[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		if (mColorBuffer == null)
			mColorBuffer = GlesToolkit.createFloatBuffer(colors.length << 2);
		mColorBuffer.put(colors);
		mColorBuffer.position(0);
	}

	public boolean onDraw() {
		synchronized (mutex) {
			if (update) {
				initVertexData();
				initShader();
				update = false;
			}

			GLES20.glUseProgram(mProgram);
			if(mGlesAnimation!=null){
				mGlesAnimation.onDraw();
			}
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					GlesMatrix.getFinalMatrix(), 0);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			GLES20.glVertexAttribPointer(maColorHandle, 3, GLES20.GL_FLOAT,
					false, 4 * 4, mColorBuffer);
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			GLES20.glEnableVertexAttribArray(maColorHandle);
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vCount);
			// GlesMatrix.popMatrix();
			return true;
		}
	}

	@Override
	public void onRecyle() {
		// TODO Auto-generated method stub
		if (mVertexBuffer != null) {
			mVertexBuffer.clear();
			mVertexBuffer = null;
		}
		if (mColorBuffer != null) {
			mColorBuffer.clear();
			mColorBuffer = null;
		}
	}
}
