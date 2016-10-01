package com.dreamer.surfaceview;

import android.graphics.Color;
import android.graphics.Paint;

public abstract class SVView {

	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected Paint mPaint = new Paint();
	
	public SVView(){
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(25);
	}
	
	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
		
	}
	
	public void setXY(float x,float y){
		this.x = (int) x;
		this.y = (int) y;
		
	}
	
	public void setXYWH(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void setXYWH(float x,float y,float width,float height){
		this.x = (int) x;
		this.y = (int) y;
		this.width = (int) width;
		this.height = (int) height;
	}
	
	public Paint getPaint(){
		return mPaint;
	}
}
