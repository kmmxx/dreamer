package com.dreamer.surfaceview;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;

public class SVGraphics extends SVView{

	private Rect mRect ;
	private int type = 0x0;
	private float stopX;
	private float stopY;
	private Path mPath = new Path();
	private float radius;
	private RectF mRectF = new RectF();
	private ShapeDrawable mShapeDrawable;
	public static final int RECT = 0x0;
	public static final int POINT = 0x1;
	public static final int lINE = 0x2;
	public static final int PATH = 0x3;
	public static final int CIRCLE = 0x4;
	public static final int SHAPE_DRAWABLE = 0x5;
	public static final int OVAL = 0x6;
	
	public SVGraphics(int type){
		super();
		this.type = type;
	}
	
	public void setLineXY(int x,int y,int sX,int sY){
		this.x = x;
		this.y = y;
		this.stopX = sX;
		this.stopY = sY;
	}
	
	public void setPath(Path path){
		this.mPath = path;
	}
	
	public void setRect(Rect rect){
		this.mRect = rect;
	}
	
	public void setCircle(int x,int y,float radius){
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public void setOvalRect(RectF rectF){
		this.mRectF = rectF;
	}
	
	public boolean onDraw(Canvas canvas){
		switch(type){
		case RECT:
			canvas.drawRect(mRect, mPaint);
			break;
		case POINT:
			canvas.drawPoint(x, y, mPaint);
			break;
		case lINE:
			canvas.drawLine(x, y, stopX, stopY, mPaint);
			break;
		case PATH:
			canvas.drawPath(mPath, mPaint);
			break;
		case CIRCLE:
			canvas.drawCircle(x, y, radius, mPaint);
			break;
		case OVAL:
			canvas.drawOval(mRectF , mPaint);
			break;
		case SHAPE_DRAWABLE:
			mShapeDrawable = null;
			break;
		default:
			break;
		}
		return true;
	}
	
}
