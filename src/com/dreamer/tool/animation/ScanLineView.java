package com.dreamer.tool.animation;

import com.oppo.safe.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ScanLineView extends View {
    private static final String TAG = "ScanLineView";
    Paint mPaint;
    private Bitmap line;
    private Bitmap invertLine;
    private Context context;
    private Path mPath;
    private float angle = 180;
    private float x;
    private float y;
    private ValueAnimator line1;
    private ValueAnimator line2;
    private boolean isDrawLine = true;
    // #ifdef COLOROS_EDIT
    // XiaoKang.Feng@Plf.SDK, 2015-1-20, delete for Optimizate
    private float mRadius;
    private int mLineWidth;
    private int mLineHeight;
    private float mYBegin = 0f;
    private float mYEnd = 980f;
    //#endif /* COLOROS_EDIT */

    public ScanLineView(Context context) {
        super(context);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        initView();
    }

    // #ifdef COLOROS_EDIT
    // XiaoKang.Feng@Plf.SDK, 2015-1-20, delete for Optimizate
//    private float clipRectLeft;
//    private float clipRectTop;
//    private float clipRectRight;
//    private float clipRectBottom;
//
//    public void initClipRect(float left, float top, float right, float bottom){
//        clipRectLeft = left;
//        clipRectTop = top;
//        clipRectRight = right;
//        clipRectBottom = bottom;
//    }
    //#endif /* COLOROS_EDIT */

    public void initView() {
        /*BitmapToolkit.createBitmap(context, R.drawable.scan_line);*/
        line = BitmapFactory.decodeResource(context.getResources(), R.drawable.scan_line);
        mLineWidth = line.getWidth();
        mLineHeight = line.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        invertLine = Bitmap.createBitmap(line, 0, 0, mLineWidth, mLineHeight, matrix,
                true);
        mPath = new Path();
        // #ifdef COLOROS_EDIT
        // XiaoKang.Feng@Plf.SDK, 2015-1-20, modify for Optimizate
        mRadius = (context.getResources().getDrawable(R.drawable.roundbg).getIntrinsicWidth() + 1)/2;
        float mainViewHeight = context.getResources().getDimension(R.dimen.secure_safe_main_green_high);
        mYBegin = java.lang.Math.max((mainViewHeight / 2 - mRadius - mLineHeight),0);
        mYEnd = java.lang.Math.min((mainViewHeight / 2 + mRadius),mainViewHeight);
        y = 0.0f;
        isDrawLine = true;
        //#endif /* COLOROS_EDIT */
    }

    public void recyle() {
        if (line != null) {
            line.recycle();
            line = null;
        }
        if (invertLine != null) {
            invertLine.recycle();
            invertLine = null;
        }
        // #ifdef COLOROS_EDIT
        // XiaoKang.Feng@Plf.SDK, 2015-1-20, delete for Optimizate
        y = 0.0f;
        isDrawLine = true;
        line1 = null;
        line2 = null;
        //#endif /* COLOROS_EDIT */
    }

    public Bitmap getBitmap() {
        return line;
    }

    public ScanLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        initView();
    }

    public void startAnimation() {
        line1 = ValueAnimator.ofFloat(mYBegin, mYEnd);
        line1.setDuration(700);
        line1.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                y = (Float) anim.getAnimatedValue();
                invalidate();
            }
        });
        line1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                isDrawLine = false;
                angle = 180;
                if(line2 != null)line2.start();
            }
        });
        line1.setInterpolator(new MInterpolator(MInterpolator.Interpolator_Linear));

        line2 = ValueAnimator.ofFloat(mYEnd, mYBegin);
        line2.setDuration(700);
        line2.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator anim) {
                y = (Float) anim.getAnimatedValue();
                invalidate();
            }
        });
        line2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                isDrawLine = true;
                angle = 0;
                if(line1 != null)line1.start();
            }
        });
        line2.setInterpolator(new MInterpolator(MInterpolator.Interpolator_Linear));
        line1.start();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // #ifdef COLOROS_EDIT
        // XiaoKang.Feng@Plf.SDK, 2015-1-20, delete for Optimizate
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }
        canvas.save();
        canvas.translate(0, 0);
        mPath.reset();

        int left = getLeft();
        int right = getRight();
        int top = getTop();
        int bottom = getBottom();

        mPath.addCircle(left + (right - left)/2, top + (bottom - top)/2, mRadius, Path.Direction.CCW);
        canvas.clipPath(mPath);
//        canvas.clipRect(clipRectLeft, clipRectTop , clipRectRight, clipRectBottom, Region.Op.DIFFERENCE);
        if (line != null && invertLine != null) {
            canvas.drawBitmap(isDrawLine ? line : invertLine, left
                    + (right - left) / 2 - mLineWidth / 2, y, mPaint);
        }
        //#endif /* COLOROS_EDIT */
        canvas.restore();
    }
}
