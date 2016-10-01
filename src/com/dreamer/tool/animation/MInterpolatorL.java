
package com.dreamer.tool.animation;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class MInterpolatorL implements Interpolator {

    private int interpolatorType = Interpolator_Linear;
    /**
     * 
     * AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     * AccelerateInterpolator在动画开始的地方速率改变比较慢，然后开始加速 AnticipateInterpolator 开始的时候向后然后向前甩
     * AnticipateOvershootInterpolator开始的时候向后然后向前甩一定值后返回最后的值 BounceInterpolator 动画结束的时候弹起
     * CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线 DecelerateInterpolator 在动画开始的地方快然后慢
     * LinearInterpolator 以常量速率改变 OvershootInterpolator向前甩一定值后再回到原来位置 PathInterpolator
     * 以贝塞尔曲线形式定义运动速率及轨迹 t --> [0,1]
     * 
     */
    public static final int Interpolator_Linear = 0;
    public static final int Interpolator_Accelerate = 1;
    public static final int Interpolator_Decelerate = 2;
    public static final int Interpolator_AccelerateDecelerate = 3;
    public static final int Interpolator_Anticipate = 4;
    public static final int Interpolator_AnticipateOvershoot = 5;
    public static final int Interpolator_Bounce = 6;
    public static final int Interpolator_Cycle = 7;
    public static final int Interpolator_Overshoot = 8;
    public static final int Interpolator_PATH = 9;
    public static final int Interpolator_Define = 10;
    private float mFactor = 1.0f;
    private int mCycles = 1;
    private float[] mPoints;
    // This governs how accurate the approximation of the Path is.
    private static final float PATH_PRECISION = 0.002f;

    private float[] mX; // x coordinates in the line

    private float[] mY; // y coordinates in the line

    private Path mPath;

    public MInterpolatorL() {

    }

    public MInterpolatorL(float factor) {
        setFactor(factor);
    }

    public MInterpolatorL(int type) {
        setInterpolatorType(type);
    }

    public MInterpolatorL(int type, int cycles) {
        setInterpolatorType(type);
        setCycles(cycles);
    }

    public MInterpolatorL(Context context, AttributeSet attrs) {}

    public int getInterpolatorType() {
        return interpolatorType;
    }

    public void setInterpolatorType(int type) {
        interpolatorType = type;
        switch (type) {
        case Interpolator_Anticipate:
        case Interpolator_Cycle:
        case Interpolator_Overshoot:
            mFactor = 2.0f;
            break;
        case Interpolator_AnticipateOvershoot:
            mFactor = 3.0f;
            break;
        case Interpolator_PATH:
            initCubic(0.4f, 0, 0.2f, 1f);
            break;
        }
    }

    public void setPath(Path path) {
        mPath = path;
        initPath(path);
    }

    public Path getPath() {
        return mPath;
    }

    public void setPathControlPoint(float x1, float y1, float x2, float y2) {
        initCubic(x1, y1, x2, y2);
    }

    public void setPathControlPoint(float x, float y) {
        initQuad(x, y);
    }

    public void setFactor(float factor) {
        this.mFactor = factor;
    }

    public float getFactor() {
        return mFactor;
    }

    public void setCycles(int cycles) {
        this.mCycles = cycles;
    }

    public float getCycles() {
        return mCycles;
    }

    public void setPoints(float[] points) {
        this.mPoints = points;
    }

    public float linearInterpolation(float t) {
        return t;
    }

    public float accelerateInterpolation(float t) {
        if (mFactor == 1.0f) {
            return t * t;
        } else {
            return (float) Math.pow(t, 2 * mFactor);
        }
    }

    public float decelerateInterpolation(float t) {
        float result;
        if (mFactor == 1.0f) {
            result = (float) (1.0f - (1.0f - t) * (1.0f - t));
        } else {
            result = (float) (1.0f - Math.pow((1.0f - t), 2 * mFactor));
        }
        return result;
    }

    public float accelerateDecelerateInterpolation(float t) {
        return (float) (Math.cos((t + 1) * Math.PI) / 2.0f) + 0.5f;
    }

    public float anticipateInterpolation(float t) {
        return t * t * ((mFactor + 1) * t - mFactor);
    }

    public float anticipateOvershootInterpolation(float t) {
        if (t < 0.5f)
            return 0.5f * a(t * 2.0f, mFactor);
        else
            return 0.5f * (o(t * 2.0f - 2.0f, mFactor) + 2.0f);
    }

    public float bounceInterpolation(float t) {
        t *= 1.1226f;
        if (t < 0.3535f)
            return bounce(t);
        else if (t < 0.7408f)
            return bounce(t - 0.54719f) + 0.7f;
        else if (t < 0.9644f)
            return bounce(t - 0.8526f) + 0.9f;
        else
            return bounce(t - 1.0435f) + 0.95f;
    }

    public float cycleInterpolation(float t) {
        return (float) (Math.sin(mFactor * mCycles * Math.PI * t));
    }

    public float overshootInterpolation(float t) {
        t -= 1.0f;
        return t * t * ((mFactor + 1) * t + mFactor) + 1.0f;
    }

    public float pathInterpolation(float t) {
        if (t <= 0) {
            return 0;
        } else if (t >= 1) {
            return 1;
        }
        // Do a binary search for the correct x to interpolate between.
        int startIndex = 0;
        int endIndex = mX.length - 1;

        while (endIndex - startIndex > 1) {
            int midIndex = (startIndex + endIndex) / 2;
            if (t < mX[midIndex]) {
                endIndex = midIndex;
            } else {
                startIndex = midIndex;
            }
        }

        float xRange = mX[endIndex] - mX[startIndex];
        if (xRange == 0) {
            return mY[startIndex];
        }

        float tInRange = t - mX[startIndex];
        float fraction = tInRange / xRange;

        float startY = mY[startIndex];
        float endY = mY[endIndex];
        return startY + (fraction * (endY - startY));
    }

    private void initQuad(float controlX, float controlY) {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.quadTo(controlX, controlY, 1f, 1f);
        initPath(mPath);
    }

    private void initCubic(float x1, float y1, float x2, float y2) {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.cubicTo(x1, y1, x2, y2, 1f, 1f);
        initPath(mPath);
    }

    private void initPath(Path path) {
        float[] pointComponents = path.approximate(PATH_PRECISION);

        int numPoints = pointComponents.length / 3;
        if (pointComponents[1] != 0 || pointComponents[2] != 0
                || pointComponents[pointComponents.length - 2] != 1
                || pointComponents[pointComponents.length - 1] != 1) {
            throw new IllegalArgumentException("The Path must start at (0,0) and end at (1,1)");
        }

        mX = new float[numPoints];
        mY = new float[numPoints];
        float prevX = 0;
        float prevFraction = 0;
        int componentIndex = 0;
        for (int i = 0; i < numPoints; i++) {
            float fraction = pointComponents[componentIndex++];
            float x = pointComponents[componentIndex++];
            float y = pointComponents[componentIndex++];
            if (fraction == prevFraction && x != prevX) {
                throw new IllegalArgumentException(
                        "The Path cannot have discontinuity in the X axis.");
            }
            if (x < prevX) {
                throw new IllegalArgumentException("The Path cannot loop back on itself.");
            }
            mX[i] = x;
            mY[i] = y;
            prevX = x;
            prevFraction = fraction;
        }
    }

    public float defineInterpolation(float t) {
        t = (float) Math.floor(mPoints.length * t);
        return mPoints[(int) t];
    }

    private static float a(float t, float s) {
        return t * t * ((s + 1) * t - s);
    }

    private static float o(float t, float s) {
        return t * t * ((s + 1) * t + s);
    }

    private static float bounce(float t) {
        return t * t * 8.0f;
    }

    @Override
    public float getInterpolation(float t) {
        switch (getInterpolatorType()) {
        case Interpolator_Accelerate:
            return accelerateInterpolation(t);
        case Interpolator_Decelerate:
            return decelerateInterpolation(t);
        case Interpolator_AccelerateDecelerate:
            return accelerateDecelerateInterpolation(t);
        case Interpolator_Anticipate:
            return anticipateInterpolation(t);
        case Interpolator_AnticipateOvershoot:
            return anticipateOvershootInterpolation(t);
        case Interpolator_Bounce:
            return bounceInterpolation(t);
        case Interpolator_Cycle:
            return cycleInterpolation(t);
        case Interpolator_Overshoot:
            return overshootInterpolation(t);
        case Interpolator_PATH:
            return pathInterpolation(t);
        case Interpolator_Define:
            return defineInterpolation(t);
        case Interpolator_Linear:
        default:
            return linearInterpolation(t);
        }
    }
}
