package com.dreamer.opengles;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.opengl.GLES20;

public class GlesText extends GlesImage {

	protected String mText;
	protected Paint mPaint;
	protected Bitmap mBitmap;
	protected Bitmap backgroundImage;

	private Paint borderPaint;

	private int bgAlpha = -1;
	private int bgRed = -1;
	private int bgBlue = -1;
	private int bgGreen = -1;

	private int pixelW;
	private int pixelH;
	private int textX;
	private int textY;

	private int wrap = 0;
	private int wrapSpaceY = 0;
	private int wrapCol = 0;

	private int textColor = Color.WHITE;

	public GlesText(Context ctx, int width, int height) {
		super(ctx, width, height);
		// TODO Auto-generated constructor stub
		pixelW = width;
		pixelH = height;

		textX = 70;
		textY = 80;

		mPaint = new Paint();
		mPaint.setTextSize(32);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setAntiAlias(true);
	}

	public void setInitial(int x, int y) {
		textX = x;
		textY = y;
	}

	public void setWrap(int wrap, int wrapSpaceY, int wrapCol) {
		this.wrap = wrap;
		this.wrapSpaceY = wrapSpaceY;
		this.wrapCol = wrapCol;
	}

	public void setText(String text) {
		synchronized (mutex) {
			mText = text;
			update = true;
		}
	}

	public String getText() {
		return mText;
	}

	public void setTextSize(float size) {
		synchronized (mutex) {
			mPaint.setTextSize(size);
			update = true;
		}
	}

	public void setTypeface(Typeface typeface) {
		synchronized (mutex) {
			mPaint.setTypeface(typeface);
			update = true;
		}
	}

	public void setFakeBoldText(boolean isBold) {
		synchronized (mutex) {
			mPaint.setFakeBoldText(isBold);
			update = true;
		}
	}

	public void setBorder(int borderColor, Style borderStyle) {
		synchronized (mutex) {
			borderPaint = new Paint();
			borderPaint.setColor(borderColor);
			borderPaint.setStyle(borderStyle);
			update = true;
		}
	}

	public void setBgColor(int bgAlpha, int bgRed, int bgGreen, int bgBlue) {
		synchronized (mutex) {
			this.bgAlpha = bgAlpha;
			this.bgRed = bgRed;
			this.bgGreen = bgGreen;
			this.bgBlue = bgBlue;
			update = true;
		}
	}

	public void setBackgroundImage(Bitmap bitmap) {
		synchronized (mutex) {
			backgroundImage = bitmap;
			update = true;
		}
	}

	public Bitmap getBackgroundImage() {
		return backgroundImage;
	}

	public float getTextSize() {
		return mPaint.getTextSize();
	}

	public void setTextColor(int color) {
		synchronized (mutex) {
			textColor = color;
			update = true;
		}
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextAlign(Align align) {
		synchronized (mutex) {
			mPaint.setTextAlign(align);
			update = true;
		}
	}

	public Align getTextAlign() {
		return mPaint.getTextAlign();
	}

	@Override
	public boolean onDraw() {
		// TODO Auto-generated method stub
		if (mText == null)
			return false;
		if (update) {
			releaseTexture();
			makeText();
		}
		return super.onDraw();
	}

	@Override
	public void onRecyle() {
		super.onRecyle();
		mText = null;
	}

	public void releaseTexture() {
		if (textureId != -1) {
			GLES20.glDeleteTextures(1, new int[] { textureId }, 0);
			GlesToolkit.removeTextureId(textureId);
			textureId = -1;
		}
	}

	private void drawStrokeText(Canvas canvas, String text, float x, float y,
			Paint paint) {
		if (!fontStyleNormal) {
			mPaint.setColor(0xff111111);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(5);
			canvas.drawText(mText, textX, textY, mPaint);
			mPaint.setStyle(Style.FILL);
			mPaint.setColor(Color.GRAY);
			mPaint.setAlpha(255);
			canvas.drawText(mText, textX, textY, mPaint);
			mPaint.setStyle(Style.FILL);
			mPaint.setColor(textColor);
			canvas.drawText(mText, textX, textY, mPaint);
		}
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(textColor);
		canvas.drawText(mText, textX, textY, mPaint);
	}

	private void makeText() {

		Bitmap mBitmap = Bitmap.createBitmap(pixelW, pixelH, Config.ARGB_8888);
		Canvas canvas = new Canvas(mBitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		if (backgroundImage != null)
			canvas.drawBitmap(backgroundImage, 0, 0, mPaint);
		if (bgAlpha != -1) {
			canvas.drawColor(Color.argb(bgAlpha, bgRed, bgGreen, bgBlue));
		}
		if (borderPaint != null) {
			canvas.drawRect(0, 0, pixelW - 1, pixelH - 1, borderPaint);
		}
		if (wrap == 0) {
			drawStrokeText(canvas, mText, textX, textY, mPaint);
			// canvas.drawText(mText, textX, textY, mPaint);
		} else {
			int wrapTime = String_length(mText) / wrap
					+ ((String_length(mText) % wrap > 0) ? 1 : 0);
			int wrapStart = 0;
			int wrapEnd = 0;

			if (wrapTime > wrapCol) {
				mText = mText.substring(0, getResLen(mText, 0, wrap * wrapCol)
						- wrapCol)
						+ "...";
			}

			for (int i = 0; i < wrapTime; i++) {
				wrapEnd = getResLen(mText, wrapStart, wrap);
				if (i == (wrapTime - 1) || i == wrapCol) {
					// canvas.drawText(mText.substring(wrapStart), textX, textY
					// + i * wrapSpaceY, mPaint);
					drawStrokeText(canvas, mText.substring(wrapStart), textX,
							textY + i * wrapSpaceY, mPaint);
					break;
				} else {
					// canvas.drawText(mText.substring(wrapStart, wrapStart +
					// wrapEnd),
					// textX, textY + i * wrapSpaceY, mPaint);
					drawStrokeText(canvas,
							mText.substring(wrapStart, wrapStart + wrapEnd),
							textX, textY + i * wrapSpaceY, mPaint);
				}
				wrapStart = wrapStart + wrapEnd;
			}
		}
		setBackground(mBitmap);
	}

	public static int getResLen(String value, int startResLen, int wrap) {
		if (value == null)
			return 0;
		int endLen = 0;
		int sum = 0;
		String chinese = "[\u0391-\uffe5]";
		String bigletter = "[A-Z]";
		boolean isOne = true;
		for (int i = startResLen; i < value.length(); i++) {
			if (sum < wrap - 1) {
				String temp = value.substring(i, i + 1);
				if (temp.matches(chinese)) {
					sum += 2;
				} else if (temp.matches(bigletter)) {
					if (isOne) {
						sum += 2;
						isOne = false;
					} else {
						sum += 1;
						isOne = true;
					}
				} else {
					sum += 1;
				}
				endLen = endLen + 1;
			} else {
				break;
			}
		}
		return endLen;
	}

	public static int String_length(String value) {
		if (value == null)
			return 0;
		int valueLength = 0;
		String chinese = "[\u0391-\uffe5]";
		String bigletter = "[A-Z]";
		boolean isOne = true;
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else if (temp.matches(bigletter)) {
				if (isOne) {
					valueLength += 1;
					isOne = false;
				} else {
					valueLength += 1;
					isOne = true;
				}
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

}
