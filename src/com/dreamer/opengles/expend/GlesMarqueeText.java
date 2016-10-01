package com.dreamer.opengles.expend;

import com.dreamer.opengles.GlesText;

import android.content.Context;

public class GlesMarqueeText extends GlesText {
	int speed = 500;
	long current = 0;
	private int start = -1;
	protected String marqueenText;
	private String TAG = GlesMarqueeText.class.getSimpleName();
	private boolean hasAnimation = false;
	private int moveSize = 5;
	private boolean reset;

	public GlesMarqueeText(Context ctx, int width, int height) {
		super(ctx, width, height);
	}

	public void setMarqueeText(String t) {
		synchronized (mutex) {
			marqueenText = t;
		}
		setText(t);
	}

	public int getMoveSize() {
		return moveSize;
	}

	public void setMoveSize(int moveSize) {
		this.moveSize = moveSize;
	}

	public String getMarqueenText() {
		return marqueenText;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void startAnimation() {
		hasAnimation = true;
		current = 0;
		start = 0;
	}

	public void stopAnimation() {
		hasAnimation = false;
		current = 0;
		start = 0;
		reset = true;
	}

	@Override
	public boolean onDraw() {
		super.onDraw();
		if (marqueenText == null)
			return false;
		if (hasAnimation && marqueenText.length() > moveSize) {
			if (current == 0) {
				current = System.currentTimeMillis();
			}
			if (System.currentTimeMillis() - current > speed) {
				current = 0;
				start++;
				if (start < 0) {
					start = 0;
				}
				if (start > marqueenText.length()) {
					start = 0;
				}
				setText(marqueenText.substring(start));
			}
		}
		if (reset) {
			reset = false;
			setText(marqueenText);
		}
		return true;

	}

}
