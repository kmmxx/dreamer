package com.dreamer.opengles;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;

public class GlesPoint extends GlesObject {

	protected int mProgram;
	protected int muMVPMatrixHandle;
	protected int maPositionHandle;
	protected int maColorHandle;
	protected String mVertexShader;
	protected String mFragmentShader;

	int maNormalHandle;// 锟斤拷锟姐法锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	int maLightLocationHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	int maCameraHandle;// 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	int maTextCoorHandle;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�? static
							// int maLightDirectionHandle; //
							// 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private Context context;
	private boolean update = true;
	private int vCount;
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mColorBuffer;
	private float[] point;
	private String TAG = GlesPoint.class.getSimpleName();
	static boolean isFirst = true;

	public GlesPoint(Context ctx) {
		super();
		this.context = ctx;
	}

	public void initStaticValue() {
		isFirst = true;
	}

	public void setPoint(float[] p) {
		synchronized (mutex) {
			this.point = p;
			vertices = p;
			update = true;
		}
	}

	public void setPoints(float[] ps) {
		synchronized (mutex) {
			vertices = ps;
			update = true;
		}
	}

	public void setPoint(int[] p) {
		float[] tmp = new float[] { GlesToolkit.translateX(p[0]),
				GlesToolkit.translateY(p[1]), GlesToolkit.translateZ(p[2]) };
		setPoint(tmp);
	}

	public void setPoints(int[] ps) {
		int size = ps.length;
		float[] tmp = new float[size];
		for (int i = 0; i < size; i++) {
			if (i % 3 == 0) {
				tmp[i] = GlesToolkit.translateX(ps[i]);
			} else if (i % 2 == 0) {
				tmp[i] = GlesToolkit.translateY(ps[i]);
			} else {
				tmp[i] = GlesToolkit.translateZ(ps[i]);
			}
		}
		setPoint(tmp);
	}

	public float[] getPoint() {
		return point;
	}

	public void setColors(float[] c) {
		synchronized (mutex) {
			this.colors = c;
		}
	}

	public float[] getColors() {
		return colors;
	}

	public void initShader() {
		// TODO Auto-generated method stub
		if (isFirst) {
			mVertexShader = GlesToolkit.loadFromAssetsFile("vertex_color.sh",
					context.getResources());
			mFragmentShader = GlesToolkit.loadFromAssetsFile("frag_color.sh",
					context.getResources());
			mProgram = GlesToolkit.createProgram(
					GlesPoint.class.getSimpleName(), mVertexShader,
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
			vertices = new float[] { point[0], point[1], point[2] };
		vCount = vertices.length / 3;
		if (mVertexBuffer == null)
			mVertexBuffer = GlesToolkit.createFloatBuffer(vertices.length << 2);
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		if (colors == null)
			colors = new float[] { 1, 1, 1, 1 };
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
			GLES20.glDrawArrays(GLES20.GL_POINTS, 0, vCount);
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
