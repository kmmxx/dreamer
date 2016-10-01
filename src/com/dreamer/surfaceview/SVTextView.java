package com.dreamer.surfaceview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SVTextView extends SVView{

	String text = null;
	public SVTextView(){
		 super();
	}
	
	
	public void setText(String text){
		this.text = text;
	}
	
	public void setPaint(Paint mPaint){
		this.mPaint = mPaint;
	}
	
	public boolean onDraw(Canvas canvas){
		canvas.drawText("kemm", x, y, mPaint);
		return true;
	}
}
