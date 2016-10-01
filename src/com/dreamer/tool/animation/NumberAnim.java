
package com.dreamer.tool.animation;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

public class NumberAnim {

    private TextView view;
    private ValueAnimator anim;
    private long duration = 500;
    private Interpolator mInterpolator;

    public NumberAnim(TextView view) {
        this.view = view;
        mInterpolator = new LinearInterpolator();
    }

    public void start(int start, int end) {
        start(start, end, duration);
    }

    public void start(int start, int end, long duration) {
        this.duration = duration;
        anim = ValueAnimator.ofInt(start, end);
        anim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                if (view != null) {
                    view.setText(((Integer) anim.getAnimatedValue()).toString());
                }
            }
        });
        anim.setInterpolator(mInterpolator);
        anim.setDuration(duration);
        anim.start();
    }

    public void start(float start, float end) {
        start(start, end, duration);
    }

    public void start(float start, float end, long duration) {
        this.duration = duration;
        anim = ValueAnimator.ofFloat(start, end);
        anim.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                if (view != null) {
                    view.setText(((Float) anim.getAnimatedValue()).toString());
                }

            }
        });
        anim.setInterpolator(mInterpolator);
        anim.setDuration(duration);
        anim.start();
    }

    public void stop() {
        if (anim != null) {
            anim.cancel();
        }
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }

    public ValueAnimator getAnim() {
        return anim;
    }

    public void setAnim(ValueAnimator anim) {
        this.anim = anim;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public Interpolator getInterpolator() {
        return mInterpolator;
    }

    public void setInterpolator(Interpolator mInterpolator) {
        this.mInterpolator = mInterpolator;
    }

}
