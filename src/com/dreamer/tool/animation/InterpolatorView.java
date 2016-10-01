
package com.dreamer.tool.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InterpolatorView extends View {

    public static final int RECT_SIZE = 40;
    private Point mSelectedPoint = null;

    public static final int POINT_ARRAY_SIZE = 7;
    public static final int C_START = 0;
    public static final int C_END = 1;
    public static final int C_CONTROL_1 = 2;
    public static final int C_CONTROL_2 = 3;
    public static final int Q_START = 4;
    public static final int Q_END = 5;
    public static final int Q_CONTROL = 6;

    private Point[] mPoints = new Point[POINT_ARRAY_SIZE];
    Paint mPaint;
    Point center;
    Point coordX;
    Point coordY;
    int offset = 100;
    int endY = 400;
    int endX = 1050;
    int centerX = 50;
    int centerY = 1400;

    public InterpolatorView(Context context) {
        super(context);
        initView();
    }

    public InterpolatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(4);
        mPaint.setTextSize(30);

        center = new Point(centerX, centerY);
        coordX = new Point(endX, centerY);
        coordY = new Point(centerX, endY);

        mPoints[C_START] = new Point(centerX, centerY);
        mPoints[C_END] = new Point(endX, endY);
        mPoints[C_CONTROL_1] = new Point(centerX + 150, centerY - 300);
        mPoints[C_CONTROL_2] = new Point(centerX + 150, centerY - 700);

        mPoints[Q_START] = new Point(centerX, centerY);
        mPoints[Q_END] = new Point(endX, endY);
        mPoints[Q_CONTROL] = new Point((centerX + endX) / 2, (centerY + endY) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw backgroud white
        canvas.drawColor(Color.WHITE);

        // draw coordinate system
        drawCoordinate(canvas);

        // draw the cubic line
        Path path = new Path();
        mPaint.setColor(Color.RED);
        path.moveTo(mPoints[C_START].x, mPoints[C_START].y);
        path.cubicTo(mPoints[C_CONTROL_1].x, mPoints[C_CONTROL_1].y, mPoints[C_CONTROL_2].x,
                mPoints[C_CONTROL_2].y, mPoints[C_END].x, mPoints[C_END].y);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Style.STROKE);
        canvas.drawPath(path, mPaint);
        canvas.drawLine(mPoints[C_START].x, mPoints[C_START].y, mPoints[C_CONTROL_1].x,
                mPoints[C_CONTROL_1].y, mPaint);
        canvas.drawLine(mPoints[C_END].x, mPoints[C_END].y, mPoints[C_CONTROL_2].x,
                mPoints[C_CONTROL_2].y, mPaint);

        // draw the quad line
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(3);
        path.reset();
        path.moveTo(mPoints[Q_START].x, mPoints[Q_START].y);
        path.quadTo(mPoints[Q_CONTROL].x, mPoints[Q_CONTROL].y, mPoints[Q_END].x, mPoints[Q_END].y);
        canvas.drawPath(path, mPaint);
        canvas.drawLine(mPoints[Q_START].x, mPoints[Q_START].y, mPoints[Q_CONTROL].x,
                mPoints[Q_CONTROL].y, mPaint);
        canvas.drawLine(mPoints[Q_END].x, mPoints[Q_END].y, mPoints[Q_CONTROL].x,
                mPoints[Q_CONTROL].y, mPaint);

        // draw control points
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextSize(30);
        for (int i = 0; i < POINT_ARRAY_SIZE; i++) {
            canvas.drawOval(pointToRect(mPoints[i]), mPaint);
            canvas.drawText("(" + (mPoints[i].x - centerX) / 1000f + "," + (centerY - mPoints[i].y)
                    / 1000f + ")", mPoints[i].x, mPoints[i].y + 50, mPaint);
        }

        // draw bezier expressions
        drawExpression(canvas);

    }

    private void drawExpression(Canvas canvas) {
        // mPaint.setColor(Color.RED);
        // mPaint.setStrokeWidth(5);
        // mPaint.setTextSize(30);
        // canvas.drawText("bezier_one:B(t)=" + , mPaint);
    }

    private void drawCoordinate(Canvas canvas) {
        mPaint.setColor(Color.CYAN);
        mPaint.setStrokeWidth(5);
        mPaint.setTextSize(30);
        canvas.drawLine(center.x, center.y, coordX.x, coordX.y, mPaint);
        canvas.drawLine(center.x, center.y, coordY.x, coordY.y, mPaint);
        canvas.drawText("(0,0)", centerX - 30, centerY + 30, mPaint);
        canvas.drawText("(0,1.0)", coordX.x - 50, coordX.y + 30, mPaint);
        canvas.drawText("(0,1.0)", coordY.x, coordY.y - 20, mPaint);
        canvas.drawText("(1.0,1.0)", coordX.x - 100, coordY.y - 20, mPaint);
        mPaint.reset();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(3);
        for (int i = 0; i < 10; i++) {
            canvas.drawLine(center.x + offset * (i + 1), center.y, center.x + offset * (i + 1),
                    coordY.y, mPaint);
            canvas.drawLine(center.x, center.y - offset * (i + 1), coordX.x, center.y - offset
                    * (i + 1), mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            for (int i = 0; i < POINT_ARRAY_SIZE; i++) {
                if (pointToRect(mPoints[i]).contains(event.getX(), event.getY())) {
                    mSelectedPoint = mPoints[i];
                }
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (null != mSelectedPoint) {
                mSelectedPoint.x = (int) event.getX();
                mSelectedPoint.y = (int) event.getY();
                invalidate();
            }
            break;
        case MotionEvent.ACTION_UP:
            mSelectedPoint = null;
            break;
        default:
            break;
        }
        return true;

    }

    private RectF pointToRect(Point p) {
        return new RectF(p.x - RECT_SIZE / 2, p.y - RECT_SIZE / 2, p.x + RECT_SIZE / 2, p.y
                + RECT_SIZE / 2);
    }
}
