/**
 * 
 */
package com.dreamer.canvas;

import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.View;

/**
 * @author wx
 * 
 */
public class BasicView extends View implements Runnable {
	
	Object mutex = new Object();

	public static final int TopLeft = 0x10 | 0x4;

	protected Paint paint;

	Bitmap backgrondImg;

	Vector<ViewChild> childs;


	public BasicView(Context context) {
		super(context);
		paint = new Paint();
		childs = new Vector<ViewChild>();
		initView(context);
	}

	public void add(ViewChild child) {
		if (child == null)
			return;
		child.setPaint(paint);
		childs.removeElement(child);
		childs.addElement(child);
		child.view = this;
	}

	public void remove(ViewChild child) {
		if (child == null || !childs.contains(child))
			return;
		childs.removeElement(child);
		child.view = null;
	}

	public void removeAll() {
		childs.removeAllElements();
	}

	/**
	 * ³õÊ¼»¯View
	 * 
	 */
	protected void initView(Context context) {
	}

	/**
	 * Ïú»ÙView
	 * 
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public void setBackgroundImg(Bitmap img) {
		backgrondImg = null;
		backgrondImg = img;
	}

	public Bitmap getBackgroundImg() {
		return backgrondImg;
	}

	protected void drawBackground(Canvas c) {
		if (backgrondImg == null) {
			paint.setColor(0x000000);
			c.drawRect(0, 0, getWidth(), getHeight(), paint);
		} else {
			c.drawBitmap(backgrondImg, 0, 0, paint);
		}
	}

	protected void onDraw(Canvas c) {
		drawBackground(c);
		paintAllView(c);
	}

	protected void paintAllView(Canvas c) {
		int size = childs.size();
		for (int i = 0; i < size; i++) {
			ViewChild child = childs.elementAt(i);
			if (child == null)
				continue;
			child.setPaint(paint);
			child.onDraw(c);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	public void run() {

	}
}