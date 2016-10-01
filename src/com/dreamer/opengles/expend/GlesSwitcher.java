package com.dreamer.opengles.expend;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesText;
import com.dreamer.opengles.GlesToolkit;
import com.dreamer.tool.util.PageUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.SystemClock;
import android.provider.Settings.System;

public class GlesSwitcher {

	private boolean hasAnimation = false;
	private int animY = 0;
	private String notice;
	GlesText[] texts;
	int width;
	int height;
	private Align textAlign;
	private int textColor;
	private float textSize;
	private Context context;
	private boolean fontStyleNormal;
	private int spaceY = 30;
	private int progress;
	private long switchTime = 5000;
	private long currentTime;
	private List<String> list;
	private int index = 0;
	private float alpha1 = 1.0f;
	private float alpha2 = 0f;
	private int index1 = 1;
	private float angleX = 0;
	private float animZ;
	private int maxLength = 10;

	public GlesSwitcher(Context context, int width, int height) {
		this.context = context;
		this.height = height;
		this.width = width;
		textSize = 23;
		textAlign = Align.LEFT;
		textColor = Color.GRAY;
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		texts = new GlesText[3];
		for (int i = 0; i < 3; i++) {
			texts[i] = new GlesText(context, width, height);
			texts[i].setTextAlign(textAlign);
			texts[i].setTextColor(textColor);
			texts[i].setTextSize(textSize);
			if (fontStyleNormal)
				texts[i].setFontStyleNormal(true);
		}
	}

	public long getSwitchTime() {
		return switchTime;
	}

	public void setSwitchTime(long switchTime) {
		this.switchTime = switchTime;
	}

	public int getSpaceY() {
		return spaceY;
	}

	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}

	public Align getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(Align textAlign) {
		this.textAlign = textAlign;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public void setText(String str) {
		this.notice = str;
		list = PageUtil.getListByLength(str, maxLength);
		updateView();
	}

	private void updateView() {
		// TODO Auto-generated method stub
		if (list == null || list.size() <= 0)
			return;
		texts[0].setText(list.get(index));
		if (list.size() > index1)
			texts[1].setText(list.get(index1));
		if (list.size() > index1) {
			texts[2].setText(list.get(index1));
		}
	}

	public int getAnimY() {
		return animY;
	}

	public void setAnimY(int animY) {
		this.animY = animY;
	}

	public boolean onDraw() {
		if (texts == null)
			return false;
		if (!hasAnimation || list.size() < 2) {
			texts[2].onDraw();
		} else {
			GlesMatrix.translate(GlesToolkit.translateX(0),
					GlesToolkit.translateY(animY), 0f);
			// GlesMatrix.rotate(angleX, 1, 0, 0);
//			texts[0].setAlpha(alpha1);
			texts[0].onDraw();
			GlesMatrix.translate(GlesToolkit.translateX(0),
					GlesToolkit.translateY(spaceY), 0);
//			texts[1].setAlpha(alpha2);
			texts[1].onDraw();
		}
		moveAnimation();
		return true;
	}

	private void moveAnimation() {
		// TODO Auto-generated method stub
		if (hasAnimation) {
			animY -= spaceY / 10;
			alpha1 -= 0.02f;
			alpha2 += 0.02f;
			// animZ += 0.1f;
			// angleX += 5;
			progress++;
			if (progress > 10) {
				stopAnimation();
			}
		}
		if (currentTime == 0) {
			currentTime = SystemClock.uptimeMillis();
		}
		if (SystemClock.uptimeMillis() - currentTime > switchTime) {
			currentTime = 0;
			changeIndex(1);
			stopAnimation();
			startAnimation();
		}
	}

	private void changeIndex(int i) {
		// TODO Auto-generated method stub
		if (list == null)
			return;
		index += i;
		index1 += i;
		if (index > list.size() - 1) {
			index = 0;
		}
		if (index1 > list.size() - 1) {
			index1 = 0;
		}
		updateView();
	}

	public void startAnimation() {
		hasAnimation = true;
		animY = 0;
		// animZ = 0;
		progress = 0;
		alpha1 = 1.0f;
		alpha2 = 0f;
		angleX = 0;
	}

	public void stopAnimation() {
		hasAnimation = false;
		animY = 0;
		// animZ = 1.1f;
		angleX = 0;
		progress = 0;
		alpha1 = 1.0f;
		alpha2 = 0f;
	}

}
