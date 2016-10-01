
package com.dreamer.tool.animation;

import java.util.Random;

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

public class MInterpolator implements Interpolator {

    private int interpolatorType = Interpolator_Linear;
    /**
     * 
     * AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     * AccelerateInterpolator在动画开始的地方速率改变比较慢，然后开始加速 AnticipateInterpolator 开始的时候向后然后向前甩
     * AnticipateOvershootInterpolator开始的时候向后然后向前甩一定值后返回最后的值 BounceInterpolator 动画结束的时候弹起
     * CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线 DecelerateInterpolator 在动画开始的地方快然后慢
     * LinearInterpolator 以常量速率改变 OvershootInterpolator向前甩一定值后再回到原来位置 BezierInterpolator
     * 以贝塞尔曲线形式定义运动速率及轨迹 t --> [0,1]
     * 
     */
    public static final int Interpolator_Define = 0;

    public static final int Interpolator_Linear = Interpolator_Define + 1;
    public static final int Interpolator_Accelerate = Interpolator_Define + 2;
    public static final int Interpolator_Decelerate = Interpolator_Define + 3;
    public static final int Interpolator_AccelerateDecelerate = Interpolator_Define + 4;
    public static final int Interpolator_Anticipate = Interpolator_Define + 5;
    public static final int Interpolator_AnticipateOvershoot = Interpolator_Define + 6;
    public static final int Interpolator_Bounce = Interpolator_Define + 7;
    public static final int Interpolator_Cycle = Interpolator_Define + 8;
    public static final int Interpolator_Overshoot = Interpolator_Define + 9;
    public static final int Interpolator_BEZIER = Interpolator_Define + 10;

    public static final int Interpolator_Random = Interpolator_Define + 11;

    public static final int Interpolator_EaseIn = Interpolator_Define + 12;
    public static final int Interpolator_EaseOut = Interpolator_Define + 13;
    public static final int Interpolator_EaseInOut = Interpolator_Define + 14;
    public static final int Interpolator_EaseOutIn = Interpolator_Define + 15;
    public static final int Interpolator_EaseInBack = Interpolator_Define + 16;
    public static final int Interpolator_EaseOutBack = Interpolator_Define + 17;
    public static final int Interpolator_EaseInOutBack = Interpolator_Define + 18;
    public static final int Interpolator_EaseOutInBack = Interpolator_Define + 19;
    public static final int Interpolator_EaseInElastic = Interpolator_Define + 20;
    public static final int Interpolator_EaseOutElastic = Interpolator_Define + 21;
    public static final int Interpolator_EaseInOutElastic = Interpolator_Define + 22;
    public static final int Interpolator_EaseOutInElastic = Interpolator_Define + 23;
    public static final int Interpolator_EaseInBounce = Interpolator_Define + 24;
    public static final int Interpolator_EaseOutBounce = Interpolator_Define + 25;
    public static final int Interpolator_EaseInOutBounce = Interpolator_Define + 26;
    public static final int Interpolator_EaseOutInBounce = Interpolator_Define + 27;

    private float mFactor = 1.0f;
    private int mCycles = 1;
    private float[] mPoints;
    // This governs how accurate the approximation of the Path is.
    private static final float BEZIER_PRECISION = 0.0000625f;
    private static final float TWO_PI = 2 * 3.14159f;

    private Path mPath;
    private BezierUtil mBezierUtil;

    public MInterpolator() {

    }

    public MInterpolator(float factor) {
        setFactor(factor);
    }

    public MInterpolator(float x1, float y1, float x2, float y2) {
        setInterpolatorType(Interpolator_BEZIER);
        setBezierControlPoint(x1, y1, x2, y2);
    }

    public MInterpolator(int type) {
        setInterpolatorType(type);
    }

    public MInterpolator(int type, int cycles) {
        setInterpolatorType(type);
        setCycles(cycles);
    }

    public MInterpolator(Context context, AttributeSet attrs) {}

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
        case Interpolator_BEZIER:
            initCubic(0.4f, 0, 0.2f, 1f);
            mBezierUtil = new BezierUtil(0.4f, 0, 0.2f, 1f);
            break;
        }
    }

    public void setPath(Path path) {
        mPath = path;
    }

    public Path getPath() {
        return mPath;
    }

    public void setBezierControlPoint(float x1, float y1, float x2, float y2) {
        initCubic(x1, y1, x2, y2);
        mBezierUtil = new BezierUtil(x1, y1, x2, y2);
    }

    public void setBezierControlPoint(float x, float y) {
        initQuad(x, y);
        mBezierUtil = new BezierUtil(x, y, x, y);
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

    public float bezierInterpolation(float t) {
        return (float) mBezierUtil.solve(t, BEZIER_PRECISION);
    }

    public float randomize(float ratio) {
        Random r = new Random();
        float d = (float) r.nextDouble() * ratio;
        return d;
    }

    // ios interpolator
    public float easeIn(float ratio) {
        return ratio * ratio * ratio;
    }

    public float easeOut(float ratio) {
        float invRatio = ratio - 1.0f;
        return invRatio * invRatio * invRatio + 1.0f;
    }

    public float easeInOut(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeIn(ratio * 2.0f);
        else
            return 0.5f * easeOut((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeOutIn(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeOut(ratio * 2.0f);
        else
            return 0.5f * easeIn((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeInBack(float ratio) {
        float s = 1.70158f;
        return (float) Math.pow(ratio, 2.0f) * ((s + 1.0f) * ratio - s);
    }

    public float easeOutBack(float ratio) {
        float invRatio = ratio - 1.0f;
        float s = 1.70158f;
        return (float) Math.pow(invRatio, 2.0f) * ((s + 1.0f) * invRatio + s) + 1.0f;
    }

    public float easeInOutBack(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeInBack(ratio * 2.0f);
        else
            return 0.5f * easeOutBack((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeOutInBack(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeOutBack(ratio * 2.0f);
        else
            return 0.5f * easeInBack((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeInElastic(float ratio) {
        if (ratio == 0.0f || ratio == 1.0f)
            return ratio;
        else {
            float p = 0.3f;
            float s = p / 4.0f;
            float invRatio = ratio - 1.0f;
            return -1.0f * (float) Math.pow(2.0f, 10.0f * invRatio)
                    * (float) Math.sin((invRatio - s) * TWO_PI / p);
        }
    }

    public float easeOutElastic(float ratio) {
        if (ratio == 0.0f || ratio == 1.0f)
            return ratio;
        else {
            float p = 0.3f;
            float s = p / 4.0f;
            return (float) Math.pow(2.0f, -10.0f * ratio)
                    * (float) Math.sin((ratio - s) * TWO_PI / p) + 1.0f;
        }
    }

    public float easeInOutElastic(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeInElastic(ratio * 2.0f);
        else
            return 0.5f * easeOutElastic((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeOutInElastic(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeOutElastic(ratio * 2.0f);
        else
            return 0.5f * easeInElastic((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeInBounce(float ratio) {
        return 1.0f - easeOutBounce(1.0f - ratio);
    }

    public float easeOutBounce(float ratio) {
        float s = 7.5625f;
        float p = 2.75f;
        float l;
        if (ratio < (1.0f / p)) {
            l = s * (float) Math.pow(ratio, 2.0f);
        } else {
            if (ratio < (2.0f / p)) {
                ratio -= 1.5f / p;
                l = s * (float) Math.pow(ratio, 2.0f) + 0.75f;
            } else {
                if (ratio < 2.5f / p) {
                    ratio -= 2.25f / p;
                    l = s * (float) Math.pow(ratio, 2.0f) + 0.9375f;
                } else {
                    ratio -= 2.625f / p;
                    l = s * (float) Math.pow(ratio, 2.0f) + 0.984375f;
                }
            }
        }
        return l;
    }

    public float easeInOutBounce(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeInBounce(ratio * 2.0f);
        else
            return 0.5f * easeOutBounce((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    public float easeOutInBounce(float ratio) {
        if (ratio < 0.5f)
            return 0.5f * easeOutBounce(ratio * 2.0f);
        else
            return 0.5f * easeInBounce((ratio - 0.5f) * 2.0f) + 0.5f;
    }

    private void initQuad(float controlX, float controlY) {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.quadTo(controlX, controlY, 1f, 1f);
    }

    private void initCubic(float x1, float y1, float x2, float y2) {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.cubicTo(x1, y1, x2, y2, 1f, 1f);
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
        case Interpolator_BEZIER:
            return bezierInterpolation(t);
        case Interpolator_Random: // 11
            return randomize(t);
        case Interpolator_EaseIn:
            return easeIn(t);
        case Interpolator_EaseInBack:
            return easeInBack(t);
        case Interpolator_EaseInBounce:
            return easeInBounce(t);
        case Interpolator_EaseInElastic:
            return easeInElastic(t);
        case Interpolator_EaseInOut:
            return easeInOut(t);
        case Interpolator_EaseInOutBack:
            return easeInOutBack(t);
        case Interpolator_EaseInOutBounce:
            return easeInOutBounce(t);
        case Interpolator_EaseInOutElastic:
            return easeInOutElastic(t);
        case Interpolator_EaseOut:
            return easeOut(t);
        case Interpolator_EaseOutBack:
            return easeOutBack(t);
        case Interpolator_EaseOutBounce:
            return easeOutBounce(t);
        case Interpolator_EaseOutElastic:
            return easeOutElastic(t);
        case Interpolator_EaseOutIn:
            return easeOutIn(t);
        case Interpolator_EaseOutInBack:
            return easeOutInBack(t);
        case Interpolator_EaseOutInBounce:
            return easeOutInBounce(t);
        case Interpolator_EaseOutInElastic:
            return easeOutInElastic(t);
        case Interpolator_Define:
            return defineInterpolation(t);
        case Interpolator_Linear:
        default:
            return linearInterpolation(t);
        }
    }

    public class BezierUtil {

        private double ax;
        private double bx;
        private double cx;

        private double ay;
        private double by;
        private double cy;

        public BezierUtil(double p1x, double p1y, double p2x, double p2y) {
            InitBezierUtil(p1x, p1y, p2x, p2y);
        }

        private void InitBezierUtil(double p1x, double p1y, double p2x, double p2y) {
            // Calculate the polynomial coefficients, implicit first and last
            // control points are (0,0) and (1,1).
            cx = 3.0 * p1x;
            bx = 3.0 * (p2x - p1x) - cx;
            ax = 1.0 - cx - bx;

            cy = 3.0 * p1y;
            by = 3.0 * (p2y - p1y) - cy;
            ay = 1.0 - cy - by;
        }

        private double sampleCurveX(double t) {
            // `ax t^3 + bx t^2 + cx t' expanded using Horner's rule.
            return ((ax * t + bx) * t + cx) * t;
        }

        private double sampleCurveY(double t) {
            return ((ay * t + by) * t + cy) * t;
        }

        private double sampleCurveDerivativeX(double t) {
            return (3.0 * ax * t + 2.0 * bx) * t + cx;
        }

        // Given an x value, find a parametric value it came from.
        private double solveCurveX(double x, double epsilon) {
            double t0;
            double t1;
            double t2;
            double x2;
            double d2;
            int i;
            // First try a few iterations of Newton's method -- normally very fast.
            for (t2 = x, i = 0; i < 8; i++) {
                x2 = sampleCurveX(t2) - x;
                if (Math.abs(x2) < epsilon)
                    return t2;
                d2 = sampleCurveDerivativeX(t2);
                if (Math.abs(d2) < 1e-6)
                    break;
                t2 = t2 - x2 / d2;
            }
            // Fall back to the bisection method for reliability.
            t0 = 0.0;
            t1 = 1.0;
            t2 = x;

            if (t2 < t0)
                return t0;
            if (t2 > t1)
                return t1;

            while (t0 < t1) {
                x2 = sampleCurveX(t2);
                if (Math.abs(x2 - x) < epsilon)
                    return t2;
                if (x > x2)
                    t0 = t2;
                else
                    t1 = t2;
                t2 = (t1 - t0) * .5 + t0;
            }
            // Failure.
            return t2;
        }

        // epsilon = 1. / (1000. * (dur);
        public double solve(double x, double epsilon) {
            return sampleCurveY(solveCurveX(x, epsilon));
        }
    }
}
