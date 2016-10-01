package com.dreamer.opengles;

public class GlesAnimation {

	private float x = 0f;
	private float y = 0f;
	private float z = 0f;
	private float srcX = 0f;
	private float srcY = 0f;
	private float srcZ = 0f;
	private float dstX = 0f;
	private float dstY = 0f;
	private float dstZ = 0f;
	private float angleX = 0;
	private float angleY = 0;
	private float angleZ = 0;
	private float srcAngleX = 0;
	private float srcAngleY = 0;
	private float srcAngleZ = 0;
	private float dstAngleX = 0;
	private float dstAngleY = 0;
	private float dstAngleZ = 0;
	private float scaleX = 1f;
	private float scaleY = 1f;
	private float scaleZ = 1f;
	private float srcScaleX = 1f;
	private float srcScaleY = 1f;
	private float srcScaleZ = 1f;
	private float dstScaleX = 1f;
	private float dstScaleY = 1f;
	private float dstScaleZ = 1f;
	private float alpha = GlesMatrix.getAlpha();
	private float srcAlpha = 1.0f;
	private float dstAlpha = 1.0f;
	private boolean isAnimation = false;
	private Animation animation;
	private int process;
	private int maxProcess = 20;
	private AnimationCallback animationCallback;
	private float step = 0.05f;

	public GlesAnimation() {
		animation = new Animation();
	}

	public void startAnimation() {
		isAnimation = true;
		pushData();
		if (animationCallback != null) {
			animationCallback.onAnimationStart();
		}
	}

	private void pushData() {
		// srcAlpha = alpha;
		// dstAlpha = alpha + animation.alpha;
		srcAlpha = animation.srcAlpha;
		dstAlpha = animation.dstAlpha;
		alpha = srcAlpha;
		srcX = animation.srcX;
		srcY = animation.srcY;
		srcZ = animation.srcZ;
		x = srcX;
		y = srcY;
		z = srcZ;
		dstX = animation.dstX;
		dstY = animation.dstY;
		dstZ = animation.dstZ;

		srcAngleX = animation.srcAngleX;
		srcAngleY = animation.srcAngleY;
		srcAngleZ = animation.srcAngleZ;
		angleX = srcAngleX;
		angleY = srcAngleY;
		angleZ = srcAngleZ;
		dstAngleX = animation.dstAngleX;
		dstAngleY = animation.dstAngleY;
		dstAngleZ = animation.dstAngleZ;

		srcScaleX = animation.srcScaleX;
		srcScaleY = animation.srcScaleY;
		srcScaleZ = animation.srcScaleZ;
		scaleX = srcScaleX;
		scaleY = srcScaleY;
		scaleZ = srcScaleZ;
		dstScaleX = animation.dstScaleX;
		dstScaleY = animation.dstScaleY;
		dstScaleZ = animation.dstScaleZ;

	}

	private void popData() {
		if (getRealAnimation()) {
			x = dstX;
			y = dstY;
			z = dstZ;
			angleX = dstAngleX;
			angleY = dstAngleY;
			angleZ = dstAngleZ;
			scaleX = dstScaleX;
			scaleY = dstScaleY;
			scaleZ = dstScaleZ;
			alpha = dstAlpha;
		} else {
			x = srcX;
			y = srcY;
			z = srcZ;
			angleX = srcAngleX;
			angleY = srcAngleY;
			angleZ = srcAngleZ;
			scaleX = srcScaleX;
			scaleY = srcScaleY;
			scaleZ = srcScaleZ;
			alpha = srcAlpha;
		}
	}

	public void stopAnimation() {
		isAnimation = false;
		process = 0;
		popData();
		animation.clearAnimation();
		if (animationCallback != null) {
			animationCallback.onAnimationStop();
		}
	}

	public boolean isAnimation() {
		return isAnimation;
	}

	public void setAnimation(Animation a) {
		this.animation = a;
	}

	public Animation getAnimation() {
		return animation;
	}

	public int getProcess() {
		return process;
	}

	public void setProcess(int process) {
		this.process = process;
	}

	public int getMaxProcess() {
		return maxProcess;
	}

	public void setMaxProcess(int maxProcess) {
		this.maxProcess = maxProcess;
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

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public class Animation {
		public static final int ANIMATION_TYPE_UNIFORM = 0x01;
		public static final int ANIMATION_TYPE_ACCELERATE = 0x02;
		private float angleX = 0;
		private float srcAngleX = GlesAnimation.this.angleX;
		private float dstAngleX = GlesAnimation.this.angleX;;
		private float angleY = 0;
		private float srcAngleY = GlesAnimation.this.angleY;
		private float dstAngleY = GlesAnimation.this.angleY;;
		private float angleZ = 0;
		private float srcAngleZ = GlesAnimation.this.angleZ;
		private float dstAngleZ = GlesAnimation.this.angleZ;;
		private float scaleX = 0f;
		private float srcScaleX = GlesAnimation.this.scaleX;
		private float dstScaleX = GlesAnimation.this.scaleX;;
		private float scaleY = 0f;
		private float srcScaleY = GlesAnimation.this.scaleY;
		private float dstScaleY = GlesAnimation.this.scaleY;
		private float scaleZ = 0f;
		private float srcScaleZ = GlesAnimation.this.scaleZ;
		private float dstScaleZ = GlesAnimation.this.scaleZ;
		private boolean RealAnimation = true;
		private float alpha;
		private float srcAlpha = GlesAnimation.this.alpha;
		private float dstAlpha = 1f;
		private float x;
		private float srcX = GlesAnimation.this.x;
		private float dstX;
		private float y;
		private float dstY;
		private float srcY = GlesAnimation.this.y;
		private float z;
		private float srcZ = GlesAnimation.this.z;
		private float dstZ;

		private int animationType = Animation.ANIMATION_TYPE_UNIFORM;

		public void clearAnimation() {
			angleX = 0;
			angleY = 0;
			angleZ = 0;
			scaleX = 0f;
			scaleY = 0f;
			scaleZ = 0f;
			RealAnimation = true;
			alpha = 0f;
			srcAlpha = GlesAnimation.this.alpha;
			dstAlpha = GlesAnimation.this.alpha;
			srcX = GlesAnimation.this.x;
			srcY = GlesAnimation.this.y;
			srcZ = GlesAnimation.this.z;
			dstX = GlesAnimation.this.x;
			dstY = GlesAnimation.this.y;
			dstZ = GlesAnimation.this.z;
			srcAngleX = GlesAnimation.this.angleX;
			srcAngleY = GlesAnimation.this.angleY;
			srcAngleZ = GlesAnimation.this.angleZ;
			srcScaleX = GlesAnimation.this.scaleX;
			srcScaleY = GlesAnimation.this.scaleY;
			srcScaleZ = GlesAnimation.this.scaleZ;
			x = 0;
			y = 0;
			z = 0;
			animationType = Animation.ANIMATION_TYPE_UNIFORM;
		}
	}

	public void setAnimationStep(float s) {
		step = s;
	}

	public float getAnimationStep() {
		return step;
	}

	public void setAnimationType(int t) {
		animation.animationType = t;
		if (t == Animation.ANIMATION_TYPE_UNIFORM) {
			step = 0.05f;
		} else if (t == Animation.ANIMATION_TYPE_ACCELERATE) {
			step = 0.5f;
		}
	}

	public int getAnimationType() {
		return animation.animationType;
	}

	public void setRealAnimation(boolean b) {
		this.animation.RealAnimation = b;
	}

	public boolean getRealAnimation() {
		return animation.RealAnimation;
	}

	public void setAnimationAlpha(float a) {
		animation.alpha = a;
		animation.dstAlpha = animation.srcAlpha + animation.alpha;
	}

	public void setAnimationAlpha(float src, float dst) {
		animation.srcAlpha = src;
		animation.dstAlpha = dst;
		animation.alpha = dst - src;
	}

	public void setAnimationTranslate(float x, float y, float z) {
		animation.x = x;
		animation.y = y;
		animation.z = z;
		animation.dstX = animation.srcX + x;
		animation.dstY = animation.srcY + y;
		animation.dstZ = animation.srcZ + z;
	}

	public void setAnimationTranslate(float srcX, float dstX, float srcY,
			float dstY, float srcZ, float dstZ) {
		animation.srcX = srcX;
		animation.dstX = dstX;
		animation.x = dstX - srcX;
		animation.srcY = srcY;
		animation.dstY = dstY;
		animation.y = dstY - srcY;
		animation.srcZ = srcZ;
		animation.dstZ = dstZ;
		animation.z = dstZ - srcZ;
	}

	public void setAnimationRotate(float angleX, float angleY, float angleZ) {
		animation.angleX = angleX;
		animation.angleY = angleY;
		animation.angleZ = angleZ;
		animation.dstAngleX = animation.srcAngleX + angleX;
		animation.dstAngleY = animation.srcAngleY + angleY;
		animation.dstAngleZ = animation.srcAngleZ + angleZ;
	}

	public void setAnimationRotate(float srcX, float dstX, float srcY,
			float dstY, float srcZ, float dstZ) {
		animation.srcAngleX = srcX;
		animation.dstAngleX = dstX;
		animation.angleX = dstX - srcX;
		animation.srcAngleY = srcY;
		animation.dstAngleY = dstY;
		animation.angleY = dstY - srcY;
		animation.srcAngleZ = srcZ;
		animation.dstAngleZ = dstZ;
		animation.angleZ = dstZ - srcZ;
	}

	public void setAnimationScale(float x, float y, float z) {
		animation.scaleX = x;
		animation.scaleY = y;
		animation.scaleZ = z;
		animation.dstScaleX = animation.srcScaleX + x;
		animation.dstScaleY = animation.srcScaleY + y;
		animation.dstScaleZ = animation.srcScaleZ + z;
	}

	public void setAnimationScale(float srcX, float dstX, float srcY,
			float dstY, float srcZ, float dstZ) {
		animation.srcScaleX = srcX;
		animation.dstScaleX = dstX;
		animation.scaleX = dstX - srcX;
		animation.srcScaleY = srcY;
		animation.dstScaleY = dstY;
		animation.scaleY = dstY - srcY;
		animation.srcScaleZ = srcZ;
		animation.dstScaleZ = dstZ;
		animation.scaleZ = dstZ - srcZ;
	}

	private void moveAnimation() {
		if (animation != null) {
			if (getAnimationType() == Animation.ANIMATION_TYPE_UNIFORM) {
				if (animation.x != 0) {
					this.x += step * animation.x;
				}
				if (animation.y != 0) {
					this.y += step * animation.y;
				}
				if (animation.z != 0) {
					this.z += step * animation.z;
				}
				if (animation.angleX != 0) {
					this.angleX += step * animation.angleX;
				}
				if (animation.angleY != 0) {
					this.angleY += step * animation.angleY;
				}
				if (animation.angleZ != 0) {
					this.angleZ += step * animation.angleZ;
				}
				if (animation.scaleX != 0) {
					this.scaleX += step * animation.scaleX;
				}
				if (animation.scaleY != 0) {
					this.scaleY += step * animation.scaleY;
				}
				if (animation.scaleZ != 0) {
					this.scaleZ += step * animation.scaleZ;
				}
				if (animation.alpha != 0) {
					this.alpha += step * animation.alpha;
				}
			} else if (getAnimationType() == Animation.ANIMATION_TYPE_ACCELERATE) {
				if (animation.x != 0) {
					this.x += step * (dstX - this.x);
				}
				if (animation.y != 0) {
					this.y += step * (dstY - y);
				}
				if (animation.z != 0) {
					this.z += step * (dstZ - z);
				}
				if (animation.angleX != 0) {
					this.angleX += step * (dstAngleX - angleX);
				}
				if (animation.angleY != 0) {
					this.angleY += step * (dstAngleY - angleY);
				}
				if (animation.angleZ != 0) {
					this.angleZ += step * (dstAngleZ - angleZ);
				}
				if (animation.scaleX != 0) {
					this.scaleX += step * (dstScaleX - scaleX);
				}
				if (animation.scaleY != 0) {
					this.scaleY += step * (dstScaleY - scaleY);
				}
				if (animation.scaleZ != 0) {
					this.scaleZ += step * (dstScaleZ - scaleZ);
				}
				if (animation.alpha != 0) {
					this.alpha += step * (dstAlpha - alpha);
				}
			}
			process++;
			if (process > maxProcess) {
				stopAnimation();
			}
		}
	}

	public void onDraw() {
		if (isAnimation) {
			moveAnimation();
		}
	}

	public void SetAnimatoinCallback(AnimationCallback b) {
		this.animationCallback = b;
	}

	public interface AnimationCallback {

		public void onAnimationStop();

		public void onAnimationStart();
	}
}
