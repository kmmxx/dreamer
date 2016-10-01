package com.dreamer.layout.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class RotationGallery extends RelativeLayout {

	private final static int DEFAULT_LONG_RADIUS = 750;
	private final static int DEFAULT_SHORT_RADIUS = -50;
	private final static int DEFAULT_ANIMATION_TIME = 600;
	private final static int CW = 0;
	private final static int CCW = 1;
	private final static int scale = 6;
	private final static float DEFAULT_ANIMATION_LARGE_SCALE = 1.73f;
	private final static float DEFAULT_ANIMATION_SMALL_SCALE = 0.8f;
	private final static Interpolator sInterpolator=new DecelerateInterpolator();
	
	private int current_focus=0;//the index of focus drawable
	private int current_step=0;
	private int large_radius=DEFAULT_LONG_RADIUS;
	private int small_radius=DEFAULT_SHORT_RADIUS;
	private int animation_time=DEFAULT_ANIMATION_TIME;
	private float large_scale=DEFAULT_ANIMATION_LARGE_SCALE;
	private float small_scale=DEFAULT_ANIMATION_SMALL_SCALE;
	private int [] drawable_res;

	private View [] views = new View[scale];
	private RotationGalleryListener listener;

	public RotationGallery(Context context) {
		super(context);
		init();
	}

	public RotationGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RotationGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setRotationGalleryListener(RotationGalleryListener listenfer) {
		this.listener = listenfer;
	}

	public void init() {
		large_scale=DEFAULT_ANIMATION_LARGE_SCALE;
		small_scale=DEFAULT_ANIMATION_SMALL_SCALE;
		large_radius=DEFAULT_LONG_RADIUS;
		small_radius=DEFAULT_SHORT_RADIUS;
		animation_time=DEFAULT_ANIMATION_TIME;
		this.setFocusable(true);
	}

	public void prepare(int [] normal_res) {
		if(normal_res.length<3){
			throw new IllegalStateException("drawable array size can't be larger than scale and smaller than 3 ");
		}
		this.drawable_res=normal_res;
		for(int i=0;i<scale;i++){
			views[i]=buildView(i);
			this.addView(views[i]);
		}
		this.setVisibility(View.INVISIBLE);
	}
	public void show(){
		this.setVisibility(View.VISIBLE);
		this.requestFocus();
		for (int i = 0; i < scale; i++) {
			AnimationSet set = new AnimationSet(true);
			Animation animation = null;
			if (i == 1) {
				animation = new ScaleAnimation(1.0f, large_scale,
						1.0f, large_scale, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 1f);
				animation.setDuration(animation_time);
				set.addAnimation(animation);
				views[i].setBackgroundResource(getFocus());
			} else if (i == 0) {
				views[i].setBackgroundResource(getPreFocus());
			} else if (i == 2) {
				views[i].setBackgroundResource(getNextFocus());
			} else {
				views[i].setBackgroundDrawable(null);
			}

			set.addAnimation(new Rotate3dAnimation(i, 0, false));
			set.setFillAfter(true);
			set.setInterpolator(sInterpolator);
			views[i].startAnimation(set);
		}
	}

	private View buildView(int index) {
		if (listener != null) {
			return listener.buildView(index);
		} else {
			throw new IllegalStateException("listener is null");
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (listener != null) {
				listener.onClick(current_focus);
				return true;
			} 
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			subStepIndex();
			for (int i = 0; i < scale; i++) {
				int index=(i+current_step)%scale;
				AnimationSet set = new AnimationSet(true);
				Animation animation = null;
				if (index == 1) {
					animation = new ScaleAnimation(large_scale, 1.0f, large_scale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getFocus());
				} else if (index == 0) {
					animation = new ScaleAnimation(1.0f, large_scale, 1.0f, large_scale, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getPreFocus());
				} else if (index == scale-1) {
					animation = new ScaleAnimation(small_scale, 1.0f, small_scale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getNextFocus());

				} else if (index == 2) {
					animation = new ScaleAnimation(1.0f, small_scale, 1.0f, small_scale, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					animation = new AlphaAnimation(1.0f, 0.0f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getNextFocus());

				} else {
					views[i].setBackgroundDrawable(null);
				}

				set.addAnimation(new Rotate3dAnimation(index, CW, true));
				set.setFillAfter(true);
				set.setInterpolator(sInterpolator);
				views[i].startAnimation(set);
			}
			subFocusIndex();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			addStepIndex();
			for (int i = 0; i < scale; i++) {
				int index=(i+current_step)%scale;
				AnimationSet set = new AnimationSet(true);
				Animation animation = null;
				if (index == 1) {
					animation = new ScaleAnimation(large_scale, 1.0f, large_scale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getFocus());
				} else if (index == 2) {
					animation = new ScaleAnimation(1.0f, large_scale, 1.0f, large_scale, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getNextFocus());
				} else if (index == 3) {
					animation = new ScaleAnimation(small_scale, 1.0f, small_scale, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					animation = new AlphaAnimation(0.0f, 1.0f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getPreFocus());

				} else if (index == 0) {
					animation = new ScaleAnimation(1.0f, small_scale, 1.0f, small_scale, Animation.RELATIVE_TO_SELF, 0.5f,
							Animation.RELATIVE_TO_SELF, 1f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					animation = new AlphaAnimation(1.0f, 0.0f);
					animation.setDuration(animation_time);
					set.addAnimation(animation);
					views[i].setBackgroundResource(getPreFocus());
				} else {
					views[i].setBackgroundDrawable(null);
				}
				set.addAnimation(new Rotate3dAnimation(index, CCW, true));
				set.setFillAfter(true);
				set.setInterpolator(sInterpolator);
				views[i].startAnimation(set);
			}
			addFocusIndex();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void addFocusIndex(){
		current_focus++;
		if(current_focus>=drawable_res.length){
			current_focus=0;
		}
	}
	private void subFocusIndex(){
		current_focus--;
		if(current_focus<0){
			current_focus=drawable_res.length-1;
		}
	}
	private void addStepIndex(){
		current_step++;
		if(current_step>=scale){
			current_step=0;
		}
	}
	private void subStepIndex(){
		current_step--;
		if(current_step<0){
			current_step=scale-1;
		}
	}
	private int getNextFocus(){
		return drawable_res[current_focus + 1 >= drawable_res.length ? 0 : current_focus + 1];
	}
	
	private int getPreFocus(){
		return drawable_res[current_focus - 1 < 0 ? drawable_res.length-1 : current_focus - 1];
	}
	private int getFocus(){
		return drawable_res[current_focus];
	}

	public int getCurrent_focus() {
		return current_focus;
	}

	public void setLarge_scale(float large_scale) {
		this.large_scale = large_scale;
	}

	public void setSmall_scale(float small_scale) {
		this.small_scale = small_scale;
	}

	public void setLarge_radius(int large_radius) {
		this.large_radius = large_radius;
	}

	public void setSmall_radius(int small_radius) {
		this.small_radius = small_radius;
	}

	public void setAnimation_time(int animation_time) {
		this.animation_time = animation_time;
	}
	
	public interface RotationGalleryListener {
		public View buildView(int index);
		public void onClick(int index);
	}
	
	public class Rotate3dAnimation extends Animation { // 瀵拷顬婄憴鎺戝�?
		private final static float per = (float) (Math.PI * 2 / scale);
		private final static float add = (float) (Math.PI / scale);
		private final float mCenterX;
		private final float mCenterY;
		private final float a;
		private final float b;
		private final boolean mReverse; // 閹藉嫬鍎氭径锟�
		private int step = 0;
		private int direct = 0;
		private Camera mCamera;

		public Rotate3dAnimation(int step, int direct, boolean reverse) {
			mCenterX = 0;
			mCenterY = 0;
			mReverse = reverse;
			a = large_radius;
			b = small_radius;
			this.direct = direct;
			this.step = step;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			this.setDuration(animation_time);
			this.setFillAfter(true);
			this.setFillBefore(true);
		}

		// 閻㈢喐鍨歍ransformation
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			final float centerX = mCenterX;// 鐠佹澘缍峷iew 娑擃參妫块崸鎰�?
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final Matrix matrix = t.getMatrix();
			float mx, mz;
			camera.save();
			if (mReverse) {
				if (direct == 0) {
					mx = (float) (a * Math.cos(step * per + per
							* interpolatedTime + add));
					mz = (float) (b * Math.sin(step * per + per
							* interpolatedTime + add));
				} else {
					mx = (float) (a * Math.cos(step * per - per
							* interpolatedTime + add));
					mz = (float) (b * Math.sin(step * per - per
							* interpolatedTime + add));
				}
			} else {
				mx = (float) (a * Math.cos(step * per * interpolatedTime + add));
				mz = (float) (b * Math.sin(step * per * interpolatedTime + add));
			}
			camera.translate(mx, 0.0f, mz);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);// 闁俺绻冮崸鎰垼閸欐ɑ宕查敍灞惧Ω閸欏倽锟介悙鐧哥�?,0閿涘些閸斻劌鍩孷iew娑擃參妫�?			matrix.postTranslate(centerX, centerY);// 閸斻劎鏁剧�灞惧灇閸氬骸鍟�粔璇叉礀閺夛�?
		}
	}
}
