
package com.dreamer.tool.animation;

import com.android.systemui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RoundProgressBar extends View {

    private static final String TAG = "RoundProgressBar";
    private final static int MSG_DOANIMATION = 1;
    private static final int MSG_ENDANIMATION = 2;
    private static final int MSG_UPDATEANIMATION = 3;
    private final static int WARNNING_PROGRESS = 85;

    private int UPDATE_MESSAGE = 4;

    private Paint paint;

    private int roundColor;

    private int roundProgressColor;

    private int textColor;

    private float textSize;

    private float roundWidth;

    private int max;

    private int progress = 100;

    private int style;

    private boolean mIsCanClear;

    private boolean upAnimationStart;

    private boolean mContinuedDecrease;
    private int mDelayTime;
    private int mFinalProgress;
    private boolean isFirst = true;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();

        TypedArray mTypedArray = context
                .obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.GRAY);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor,
                Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, STROKE);
        mTypedArray.recycle();
    }

    public static Bitmap createBitmap(Context ctx, int width, int height, Bitmap bitmap) {
        Bitmap mbmpTest = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvasTemp = new Canvas(mbmpTest);
        Matrix matrix = new Matrix();
        matrix.setScale(2f, 2f);
        canvasTemp.drawBitmap(bitmap, matrix, null);
        Drawable drawable = new BitmapDrawable(ctx.getResources(), mbmpTest);
        return ((BitmapDrawable) drawable).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // if (!mNeedUpdate) {
        // return;
        // }

        int centre = getWidth() / 2;
        int radius = (int) (centre - roundWidth / 2);
        paint.setColor(roundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(roundWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(centre, centre, radius, paint);

        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        paint.setStrokeWidth(roundWidth);
        paint.setColor(roundProgressColor);
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);

        if (upAnimationStart) {
            if (isFirst) {
                if (progress > mFinalProgress - 10 && mContinuedDecrease) {
                    progress -= 4;
                } else {
                    mContinuedDecrease = false;
                    progress += 3;
                    if (progress >= mFinalProgress) {
                        progress = mFinalProgress;
                        isFirst = false;
                        upAnimationStart = false;
                        mContinuedDecrease = true;
                    }
                }
            } else {
                if (progress > 0 && mContinuedDecrease) {
                    progress -= 4;
                } else {
                    mContinuedDecrease = false;
                    progress += 3;
                    if (progress >= mFinalProgress) {
                        progress = mFinalProgress;
                        mTouchListener.hideRecentView();
                        upAnimationStart = false;
                        mContinuedDecrease = true;
                    }
                }
            }

        }

        float angle = 360 * (float) progress / (float) max;
        switch (style) {
        case STROKE: {
            paint.setStyle(Paint.Style.STROKE);
           //COLOROS_EDIT  liuzhicang@Plf.SysApp, 2015-01-08 : add "!isFirst"
            if((progress>WARNNING_PROGRESS) && !isFirst) {
                paint.setColor(getResources().getColor(R.color.warning_progress_color));
            } else {
                paint.setColor(roundProgressColor);
            }
            canvas.drawArc(oval, -90, angle, false, paint);
            break;
        }
        case FILL: {
          //COLOROS_EDIT  liuzhicang@Plf.SysApp, 2015-01-08 : add "!isFirst"
            if((progress>WARNNING_PROGRESS)&& !isFirst) {
                paint.setColor(getResources().getColor(R.color.warning_progress_color));
            } else {
                paint.setColor(roundProgressColor);
            }
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            if (progress != 0)
                canvas.drawArc(oval, -90, angle, true, paint);
            break;
        }
        }

        if (upAnimationStart) {
            mDelayTime = 20;
            mHandler.removeMessages(MSG_UPDATEANIMATION);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATEANIMATION, mDelayTime);
        } else {
            mDelayTime = 50;
        }

    }

    public boolean isInTouchRect(int x, int y) {
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        int mRadius = (int) (getWidth() / 2 - roundWidth / 2);
        if ((x < centerX + mRadius) && (x > centerX - mRadius) && (y > centerY - mRadius)
                && (y < centerY + mRadius)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        // oukun add for avoid to click two times,run two times animation
        if (upAnimationStart) {
            return true;
        }
        // oukun add end
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (isInTouchRect(x, y)) {
                mIsCanClear = true;
                setPressed(true);
            }
            break;
        case MotionEvent.ACTION_UP:
            if (isInTouchRect(x, y) && mIsCanClear) {
                startAnimation();
                mTouchListener.touchSuccess();
                mIsCanClear = false;
            }
            setPressed(false);
            break;
        }
        return true;
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_DOANIMATION) {

            } else if (msg.what == MSG_ENDANIMATION) {
                invalidate();
            } else if (msg.what == MSG_UPDATEANIMATION) {
                invalidate();
            } else if (msg.what == UPDATE_MESSAGE) {
                invalidate();
            }
        }
    };

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
        progress = 100;
    }

    public void startAnimation() {
        upAnimationStart = true;
        mContinuedDecrease = true;
        mHandler.sendEmptyMessage(MSG_UPDATEANIMATION);
    }

    public void stopAnimation() {
        upAnimationStart = false;
        mContinuedDecrease = false;
    }

    public void setTouchEventListener(TouchEventListener listener) {
        mTouchListener = listener;
    }

    TouchEventListener mTouchListener;

    public interface TouchEventListener {

        public void touchSuccess();

        public void hideRecentView();
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public int getFinalProgress() {
        return mFinalProgress;
    }

    public void setFinalProgress(int finalProgress) {
        this.mFinalProgress = finalProgress;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            this.mFinalProgress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
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

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}
