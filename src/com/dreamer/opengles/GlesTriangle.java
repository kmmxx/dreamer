package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;

public class GlesTriangle extends GlesObject {

	protected  int mProgram;
	protected  int muMVPMatrixHandle;
	protected  int maPositionHandle;
	protected  int maColorHandle;
	protected  String mVertexShader;
	protected  String mFragmentShader;

	 int maNormalHandle;// 锟斤拷锟姐法锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 int maLightLocationHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	 int maCameraHandle;// 锟斤拷锟斤拷锟轿伙拷锟斤拷锟斤拷锟斤拷锟斤拷锟�
	 int maTextCoorHandle;// 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟�?	static int maLightDirectionHandle; // 锟斤拷源位锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷
	private Context context;
	boolean update = true;
	private  boolean isFirst = true;
	private String TAG= GlesTriangle.class.getSimpleName();

	public GlesTriangle(Context ctx) {
		super();
		this.context = ctx;
	}
	
	public  void initStaticValue(){
		isFirst = true;
	}
	
	public void setVertexs(float[] v){
		synchronized (mutex) {
			this.vertices = v;
			update = true;
		}
	}
	
	public float[] getVertexs(){
		return vertices;
	}
	
	public void setColors(float[] v){
		synchronized (mutex) {
			this.colors = v;
			update = true;
		}
	}
	
	public float[] getColors(){
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
					GlesTriangle.class.getSimpleName(), mVertexShader,
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
			vertices = new float[] { 0, 0, 0, 0.1f, 0, 0,0 ,- 0.1f, 0 };
		vCount = vertices.length / 3;
		if (mVertexBuffer == null)
			mVertexBuffer = GlesToolkit.createFloatBuffer(vertices.length <<2);
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		if (colors == null)
			colors = new float[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		if (mColorBuffer == null)
			mColorBuffer = GlesToolkit.createFloatBuffer(colors.length <<2);
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
			GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
					GlesMatrix.getFinalMatrix(), 0);
			GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
					false, 3 * 4, mVertexBuffer);
			GLES20.glVertexAttribPointer(maColorHandle, 3, GLES20.GL_FLOAT,
					false, 4 * 4, mColorBuffer);
			GLES20.glEnableVertexAttribArray(maPositionHandle);
			GLES20.glEnableVertexAttribArray(maColorHandle);
			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
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
