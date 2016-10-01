package com.dreamer.opengles;

public class Gles {
	float width = 0;
	float height = 0;
	float x = 0;
	float y = 0;
	float z = 0;
	float alpha = GlesMatrix.getAlpha();
	float angleX = 0;
	float angleY = 0;
	float angleZ = 0;
	float scaleX = 1f;
	float scaleY = 1f;
	float scaleZ = 1f;

	protected GlesAnimation mGlesAnimation;

	public boolean onDrawObject() {
		setObjectParams();
		if (x != 0 || y != 0 || z != 0)
			GlesMatrix.translate(x, y, z);
		if (angleX != 0)
			GlesMatrix.rotate(angleX, 1, 0, 0);
		if (angleY != 0)
			GlesMatrix.rotate(angleY, 0, 1, 0);
		if (angleZ != 0)
			GlesMatrix.rotate(angleZ, 0, 0, 1);
		if (scaleX != 1.0f || scaleY != 1.0f || scaleZ != 1.0f)
			GlesMatrix.scale(scaleX, scaleY, scaleZ);
		return true;
	}

	private void setObjectParams() {
		// TODO Auto-generated method stub
		if (mGlesAnimation != null) {
			x = mGlesAnimation.getX();
			y = mGlesAnimation.getY();
			z = mGlesAnimation.getZ();
			scaleX = mGlesAnimation.getScaleX();
			scaleY = mGlesAnimation.getScaleY();
			scaleZ = mGlesAnimation.getScaleZ();
			angleX = mGlesAnimation.getAngleX();
			angleY = mGlesAnimation.getAngleY();
			angleZ = mGlesAnimation.getAngleZ();
			alpha = mGlesAnimation.getAlpha();
			mGlesAnimation.onDraw();
		} else {
			alpha = GlesMatrix.getAlpha();
		}
	}

	public void setGlesAnimation(GlesAnimation g) {
		this.mGlesAnimation = g;
	}

	public GlesAnimation getGlesAnimation() {
		return mGlesAnimation;
	}

	public void startGlesAnimation() {
		if (mGlesAnimation != null) {
			mGlesAnimation.startAnimation();
		}
	}

	public void stopGlesAnimation() {
		if (mGlesAnimation != null) {
			mGlesAnimation.stopAnimation();
		}
	}

	public void setAnimationAlpha(float a) {
		if (mGlesAnimation != null) {
			mGlesAnimation.setAnimationAlpha(a);
		}
	}

	public void setAnimationTranslate(float x, float y, float z) {
		if (mGlesAnimation != null) {
			mGlesAnimation.setAnimationTranslate(x, y, z);
		}
	}

	public void setAnimationRotate(float angleX, float angleY, float angleZ) {
		if (mGlesAnimation != null) {
			mGlesAnimation.setAnimationRotate(angleX, angleY, angleZ);
		}
	}

	public void setAnimationScale(float x, float y, float z) {
		if (mGlesAnimation != null) {
			mGlesAnimation.setAnimationScale(x, y, z);
		}
	}

	public void clearGlesAnimation() {
		if (mGlesAnimation != null) {
			mGlesAnimation = null;
		}
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAngleX() {
		return angleX;
	}

	public void setAngleX(float angleX) {
		this.angleX = angleX;
	}

	public float getAngleY() {
		return angleY;
	}

	public void setAngleY(float angleY) {
		this.angleY = angleY;
	}

	public float getAngleZ() {
		return angleZ;
	}

	public void setAngleZ(float angleZ) {
		this.angleZ = angleZ;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}
}
