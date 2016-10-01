
package com.dreamer.tool.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;

public class FrameAnim {

    AnimationDrawable anim;
    View image;
    Handler mHandler;
    private int rsid;
    private long duration = 1000;
    FrameAnimListener listener;

    public FrameAnim(Handler mHandler, View imageView, int rsid) {
        image = imageView;
        this.rsid = rsid;
        this.mHandler = mHandler;
    }

    public FrameAnim() {

    }

    public void start() {
        if (mHandler != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            image.setBackgroundResource(rsid);
                            anim = new AnimationDrawable();
                            anim = (AnimationDrawable) image.getBackground();
                            if (anim.isRunning()) {
                                anim.stop();
                            }
                            anim.start();
                            if(listener!=null){
                                listener.onAnimStart();
                            }
                            if (anim.isOneShot()) {
                                stop();
                            }
                        }
                    });
                }
            }).start();
        }
    }

    public void stop() {
        if (mHandler != null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (anim.isRunning()) {
                                anim.stop();
                                image.clearAnimation();
                                image.destroyDrawingCache();
                                image.setBackgroundResource(rsid);
                            }
                            if(listener!=null){
                                listener.onAnimEnd();
                            }
                        }
                    }, duration);
                }
            }).start();
        }
    }

    public AnimationDrawable getAnim() {
        return anim;
    }

    public void setAnim(AnimationDrawable anim) {
        this.anim = anim;
    }

    public View getImage() {
        return image;
    }

    public void setImage(View image) {
        this.image = image;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public int getRsid() {
        return rsid;
    }

    public void setRsid(int rsid) {
        this.rsid = rsid;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public FrameAnimListener getListener() {
        return listener;
    }

    public void setListener(FrameAnimListener listener) {
        this.listener = listener;
    }

    public interface FrameAnimListener {

        public void onAnimStart();

        public void onAnimEnd();
    }

}
