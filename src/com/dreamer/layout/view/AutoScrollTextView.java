package com.dreamer.layout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class AutoScrollTextView extends TextView {

	private float textLength = 0f;// 文本长度
	private float viewWidth = 0f;
	private float step = 0f;// 文字的横坐标
	private float y = 0f;// 文字的纵坐标
	private float temp_view_plus_text_length = 0.0f;// 用于计算的临时变量
	private float temp_view_plus_two_text_length = 0.0f;// 用于计算的临时变量
	private Paint paint = null;// 绘图样式
	private String text = "";// 文本内容
	OnAutoScrollTextCallback mOnAutoScrollTextCallback;
	ShowThread showThread;
	private boolean isStarting = false;// 是否开始滚动
	private boolean isWait = false;// 是否开始滚动
	Object mutex = new Object();

	private int color = Color.WHITE;
	private boolean isAutoScroll = false;
	private float textStart = 10;

	/**
	 * @param context
	 *            <com.dreamer.layout.view.AutoScrollTextView
	 *            android:id="@+id/newsview" android:layout_width="600dp"
	 *            android:layout_height="wrap_content"
	 *            android:layout_marginLeft="320dp"
	 *            android:layout_marginTop="52dp" android:focusable="false"
	 *            android:gravity="center" android:textSize="22sp" />
	 * 
	 *            scrollTextView = (AutoScrollTextView)
	 *            findViewById(R.id.newsview);
	 *            scrollTextView.setTexColor(0xff3a4a5c);
	 *            scrollTextView.setText(txt);
	 *            scrollTextView.setOnAutoScrollTextCallback(mOnCallback);
	 *            scrollTextView.startScroll(); OnAutoScrollTextCallback
	 *            mOnCallback = new OnAutoScrollTextCallback() { public void
	 *            onFinish() { // TODO Auto-generated method stub
	 *            scrollTextView.startScroll(); } };
	 */
	public AutoScrollTextView(Context context) {
		super(context);
		init();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		isWait = false;
		showThread = new ShowThread(this);
		showThread.start();
	}

	public int getTextColor() {
		return color;
	}

	public void setTexColor(int color) {
		this.color = color;
	}

	public void setText(String t) {
		if (t != null && !t.equals("")) {
			this.text = t;
			paint = getPaint();
			paint.setColor(color);
			textLength = paint.measureText(text);
			viewWidth = getLayoutParams().width;
			step = textLength;
			temp_view_plus_text_length = textLength + viewWidth;
			temp_view_plus_two_text_length = textLength * 2 + viewWidth;
			y = getTextSize() + getPaddingTop();
			Log.d("AutoScrollTextView", "textLength:" + textLength
					+ ",viewWidth:" + viewWidth);
		}
	}

	public void setOnAutoScrollTextCallback(
			OnAutoScrollTextCallback mOnAutoScrollTextCallback) {
		this.mOnAutoScrollTextCallback = mOnAutoScrollTextCallback;
	}

	public void startScroll() {
		if (isWait) {
			synchronized (mutex) {
				mutex.notify();
				isWait = false;
			}
		}
		isStarting = true;
	}

	public void stopScroll() {
		isStarting = false;
	}

	public void onDraw(Canvas canvas) {
		if ("".equals(text) || text == null)
			return;

		if (!isAutoScroll) {
			if (textLength <= viewWidth) {
				canvas.drawText(text, textStart, y, paint);
				return;
			} else {
				canvas.drawText(text, textLength + textStart - step, y, paint);
			}
		} else {
			canvas.drawText(text, textLength + textStart - step, y, paint);
		}
		if (!isStarting) {
			return;
		}

		step += 2;// 0.5为文字滚动速度。
		if (step > temp_view_plus_text_length) {
			step = 0;
			isStarting = false;
			if (mOnAutoScrollTextCallback != null)
				mOnAutoScrollTextCallback.onFinish();
			return;
		}

	}

	public float getTextStart() {
		return textStart;
	}

	public void setTextStart(float textStart) {
		this.textStart = textStart;
	}

	public void setAutoScroll(boolean b) {
		this.isAutoScroll = b;
	}

	public boolean getAutoScroll() {
		return isAutoScroll;
	}

	public interface OnAutoScrollTextCallback {
		public void onFinish();
	}

	class ShowThread extends Thread {
		private AutoScrollTextView mAutoScrollTextView;
		int sleepTime = 100;

		public ShowThread(AutoScrollTextView autoScrollTextView) {
			mAutoScrollTextView = autoScrollTextView;
		}

		public void run() {
			while (true) {
				mAutoScrollTextView.postInvalidate();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				}
				if (!isStarting) {
					isWait = true;
					synchronized (mutex) {
						try {
							mutex.wait();
						} catch (InterruptedException e) {
						}
					}
				}
			}
		}
	}
}