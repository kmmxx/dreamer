/**
 * AudioConfig.java 2010-3-12 Version 1.0
 */
package com.dreamer.canvas;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author wx
 * 
 * 2011-2-12 ÏÂÎç04:13:19
 */
public abstract class ViewChild{
	
	protected Paint paint;

	protected int id;

	protected int x = 0;

	protected int y = 0;

	protected int w = 1280;

	protected int h = 720;

	protected View view;

	protected boolean visible = true;

	public ViewChild(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setVisible(boolean v) {
		if (visible == v)
			return;
		visible = v;
		if (visible) {
			postInvalidate();
		} else {
			if (view == null)
				return;
			view.postInvalidate();
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public synchronized void setBound(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	public abstract void onDraw(Canvas c);

	public final void postInvalidate() {
		postInvalidate(0, 0, w, h);
	}

	public synchronized final void postInvalidate(int rx, int ry, int rw, int rh) {
		if (view == null || !visible)
			return;
		view.postInvalidate(x + rx, y + ry, x + rx + rw, y + ry + rh);
	}
}