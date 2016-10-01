
package com.dreamer.tool.animation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ClipPathView extends View {

    private Drawable mDrawable;
    private Path mPath;
    private float mX = -1;
    private float mY = -1;
    private float mPastX;
    private float mPastY;
    private Paint mPaint;
    private ClipPath mClipPath;
    private boolean isClip = false;

    public ClipPathView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Style.STROKE);
        mPaint.setTextSize(80);
        mClipPath = new ClipPath();
    }

    public void setDrawable(Drawable pDrawable) {
        mDrawable = pDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        mDrawable.setBounds(0, 0, getWidth(), getHeight());
        Rect lRect = mDrawable.getBounds();
        // Log.i("xiao", "lRect.left =  " + lRect.left + " top = " + lRect.top + " right = " +
        // lRect.right + " bottom = " + lRect.bottom);
        canvas.save();
        if (!mPath.isEmpty()&&isClip ) {
            canvas.clipPath(mPath);
        }
        mDrawable.draw(canvas);
        mPaint.setColor(Color.GREEN);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        mPaint.setColor(Color.RED);
        canvas.drawPath(mClipPath.getPath(), mPaint);
        canvas.drawText("reset", 50, 50, mPaint);
    }

    public ClipPath getClipPath() {
        return mClipPath;
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        int lAction = event.getAction();
        if (mX != -1) {
            mPastX = mX;
            mPastY = mY;
        }
        mX = event.getX();
        mY = event.getY();
        switch (lAction) {
        case MotionEvent.ACTION_DOWN:
            mPath.reset();
            mPath.moveTo(mX, mY);
            mClipPath.reset();
            mClipPath.start = new Point(mX,mY);
            isClip = false;
            break;
        case MotionEvent.ACTION_MOVE:
            mPath.quadTo(mPastX, mPastY,(mX + mPastX) / 2, (mY + mPastY) / 2 );
            mClipPath.controls.add(new Point(mPastX,mPastY));
            mClipPath.ends.add(new Point((mX + mPastX) / 2,(mY + mPastY) / 2) );
            isClip = false;
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
//            mPath = new Path();
//            mPath.lineTo(mX, mY);
            isClip = true;
            resetPath(mX,mY);
            mPath.quadTo(mPastX, mPastY,(mX + mPastX) / 2, (mY + mPastY) / 2 );
            mClipPath.controls.add(new Point(mPastX,mPastY));
            mClipPath.ends.add(new Point((mX + mPastX) / 2,(mY + mPastY) / 2) );
            mX = -1;
            mY = -1;
            invalidate();
            break;
        default:

            break;
        }
        return true;
    }
    
    public void resetPath(float x,float y){
        if(x>0&&y>0&&y<200){
            isClip = false;
        }else{
            isClip = true;
        }
    }

    public class ClipPath {
        public Point start;
        public List<Point> ends;
        public List<Point> controls;
        Path mPath;
        
        public ClipPath(){
            mPath = new Path();
            start = new Point(0,0);
            controls = new ArrayList<Point>();
            ends = new ArrayList<Point>();
        }
        
        public void reset() {
            mPath.reset();
            start = new Point(0,0);
            controls = new ArrayList<Point>();
            ends = new ArrayList<Point>();
        }

        public Path getPath(){
            mPath.moveTo(start.x, start.y);
            for(int i = 0;i<controls.size();i++){
                mPath.quadTo(controls.get(i).x, controls.get(i).y, ends.get(i).x, ends.get(i).y);
            }
            return mPath;
        }
    }
    
    public class Point{
       public  float x;
       public  float y;
        
        public Point(float x,float y){
            this.x = x;
            this.y = y;
        }
    }
}
