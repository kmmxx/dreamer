package com.dreamer.opengles.expend;

/**
 * Launcher.java
 * TODO
 */

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesText;
import com.dreamer.opengles.GlesToolkit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;

public class MarqueeView {

	private int color = Color.WHITE; // 字的颜色
	private int size = 40; // 字的大小
	private int len = 65; // 每屏字的个数
	List<GlesText> texts;
	private int wordLen = 50; // 每个字的长度
	private int width = len * wordLen;
	private int height = 150;
	private float paddingX = 0;
	private float paddingY = 0;
	private Paint.Align align = Align.LEFT;
	private int showMaxLine = 1;
	private int showScreen = 0;
	private int maxScreen;
	private String TAG = MarqueeView.class.getSimpleName();
	private float srcX = 0f;
	private float srcY = 0f;
	private float dstX = srcX;
	private float dstY = srcY;
	private float tmpX = srcX;
	private float tmpY = srcY;
	private int indexX = 0;
	private int indexY = 0;
	private boolean isFocusMove = false;
	private float moveProgress = 0.5f;
	private float marqueeWidth = 3.4f;
	private long currentTime = -1;
	private MarqueeViewCallback mMarqueeViewCallback;
	private boolean startMoveFlag = false;
	private Context context;
	private int loopTime = width * 5;

	public MarqueeView(Context context) {
		this.context = context;
		texts = new ArrayList<GlesText>();
	}

	public void setTextWH(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWordLen() {
		return wordLen;
	}

	public void setWordLen(int wordLen) {
		this.wordLen = wordLen;
		this.width = len * wordLen;
		this.paddingX = GlesToolkit.translateWidth(width / 5 * 4 +15);
		setLoopTime(width * 5);
	}

	public void setTextLine(int line) {
		showScreen = 0;
		setShowMaxLine(line);
		setMaxScreen(line / showMaxLine);
		if (texts.size() < line) {
			int size1 = line - texts.size();
			Log.d(TAG, "kemm--line:" + line + ",size1:" + size1);
			for (int i = 0; i < size1; i++) {
				GlesText text = new GlesText(context, width, height);
				text.setText("");
				text.setTextSize(size);
				text.setTextColor(color);
				text.setTextAlign(align);
				texts.add(text);
			}
		}
	}

	public void showNextScreen() {
		showScreen++;
		if (showScreen > maxScreen) {
			showScreen = maxScreen;
		}
	}

	public void showPrevScreen() {
		showScreen--;
		if (showScreen < 0) {
			showScreen = 0;
		}
	}

	public int getCurrentScreen() {
		return showScreen;
	}

	public void setMaxScreen(int maxScreen) {
		this.maxScreen = maxScreen;
	}

	public int getMaxScreen() {
		return maxScreen;
	}

	public void initTextList(String str) {
		if (texts != null) {
			int size = texts.size();
			for (int i = 0; i < size; i++) {
				texts.get(i).setText("");
			}
		}
		if (str == null) {
		} else {
		}
		int strlen = GlesText.String_length(str) / len + 1;
		setTextLine(strlen);
		String chinese = "[\u0391-\uffe5]";
		String bigletter = "[A-Z]";
		int endChar = 0;
		int startChar = 0;
		for (int i = 0; i < texts.size() && i < strlen; i++) {
			int tmpLen = 0;
			boolean isOne = true;
			for (int j = endChar; j < str.length(); j++) {
				if (tmpLen < len * 2) {
					String tmp = str.substring(j,
							(j + 1) < str.length() ? (j + 1) : str.length());
					if (tmp.matches(chinese)) {
						tmpLen += 2;
					} else if (tmp.matches(bigletter)) {
						if (isOne) {
							tmpLen += 1;
							isOne = false;
						} else {
							tmpLen += 1;
							isOne = true;
						}
					} else {
						tmpLen += 1;
					}
					endChar += 1;
					Log.d(TAG, "initTextList->j:" + j + ",tmpLen:" + tmpLen);
				} else {
					// endChar = j;
					Log.d(TAG, "initTextList->j:" + j + ",lastChar:" + endChar);
					break;
				}
			}
			Log.d(TAG, "initTextList->i:" + i + ",lastChar:" + endChar);
			texts.get(i).setText(
					str.substring(startChar,
							endChar > str.length() ? str.length() : endChar));
			startChar = endChar;
		}
		setStartMoveFlag(true);
	}

	public boolean isStartMoveFlag() {
		return startMoveFlag;
	}

	public void setStartMoveFlag(boolean startMoveFlag) {
		this.startMoveFlag = startMoveFlag;
	}

	public void setShowMaxLine(int maxLine) {
		this.showMaxLine = maxLine;
	}

	public void setTextAlign(Paint.Align align) {
		this.align = align;
	}

	public void setTextSize(int size) {
		this.size = size;
	}

	public void setTextColor(int color) {
		this.color = color;
	}

	public void setTextLength(int len) {
		this.len = len;
		width = len * wordLen;
		this.paddingX = GlesToolkit.translateWidth(width / 5 * 4+15);
		setLoopTime(width * 5);
	}

	public void setLoopTime(int time){
		this.loopTime = time;
	}
	
	public boolean onDraw() {
		GlesMatrix.pushMatrix();
		GlesMatrix.translate(tmpX, tmpY, 0f);
		int size = texts.size();
		for (int i = 0; i < size; i++) {
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(paddingX * i, paddingY, 0);
			if (texts.get(i) != null) {
				texts.get(i).onDraw();
			}
			GlesMatrix.popMatrix();
		}
		GlesMatrix.popMatrix();
		marqueeFocus();

		return true;
	}

	// ------------------------- 属性设置 -------------------------
	public void setDirX(float srcX, float srcY, float paddingX, int indexX) {
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingX(paddingX);
		setIndexX(indexX);
	}

	public void setDirY(float srcX, float srcY, float paddingY, int indexY) {
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingY(paddingY);
		setIndexY(indexY);
	}

	public void setDirXY(float srcX, float srcY, float paddingX,
			float paddingY, int indexX, int indexY) {
		setSrcX(srcX);
		setSrcY(srcY);
		setPaddingX(paddingX);
		setPaddingY(paddingY);
		setIndexX(indexX);
		setIndexY(indexY);
	}

	public void setMarqueeWidth(float marqueeWidth) {
		this.marqueeWidth = marqueeWidth;
	}

	private void marqueeFocus() {
		dstX = srcX - paddingX * indexX;
		dstY = srcY - paddingY * indexY;
		if (tmpX != dstX || tmpY != dstY)
			isFocusMove = true;
		if (isFocusMove) {
			// tmpProgress += 0.0005f;
			tmpX -= 0.002f * marqueeWidth;
			if (currentTime == -1) {
				currentTime = System.currentTimeMillis();
			}
//			if (System.currentTimeMillis() - currentTime > loopTime
//					* texts.size()) {
			if (tmpX < -texts.size()*marqueeWidth) {
				currentTime = -1;
				tmpX = srcX;
				indexX = 1;
				indexY = 1;
				isFocusMove = false;
				if (mMarqueeViewCallback != null && startMoveFlag) {
					mMarqueeViewCallback.onMarqueeViewScroolOverCallback();
				}
				setStartMoveFlag(false);
			}
			// if (tmpProgress > 1.0f) {
			// tmpProgress = 0f;
			// tmpX = srcX;
			// indexX = 1;
			// indexY = 1;
			// isFocusMove = false;
			// }
		}

	}

	public float getSrcX() {
		return srcX;
	}

	public void setSrcX(float srcX) {
		this.srcX = srcX;
		tmpX = srcX;
	}

	public float getSrcY() {
		return srcY;
	}

	public void setSrcY(float srcY) {
		this.srcY = srcY;
		tmpY = srcY;
	}

	public float getDstX() {
		return dstX;
	}

	public void setDstX(float dstX) {
		this.dstX = dstX;
	}

	public float getDstY() {
		return dstY;
	}

	public void setDstY(float dstY) {
		this.dstY = dstY;
	}

	public float getPaddingX() {
		return paddingX;
	}

	public void setPaddingX(float paddingX) {
		this.paddingX = paddingX;
	}

	public float getPaddingY() {
		return paddingY;
	}

	public void setPaddingY(float paddingY) {
		this.paddingY = paddingY;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public float getMoveProgress() {
		return moveProgress;
	}

	public void setMoveProgress(float moveProgress) {
		this.moveProgress = moveProgress;
	}

	public void setIndexXY(int indexX2, int indexY2) {
		// TODO Auto-generated method stub
		this.indexX = indexX2;
		this.indexY = indexY2;
	}

	public interface MarqueeViewCallback {
		public void onMarqueeViewScroolOverCallback();
	}

	public void setMarqueeViewCallback(MarqueeViewCallback mMarqueeViewCallback) {
		this.mMarqueeViewCallback = mMarqueeViewCallback;
	}

}