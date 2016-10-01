package com.dreamer.surfaceview.views;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;

import com.dreamer.R;
import com.dreamer.surfaceview.FocusView;
import com.dreamer.surfaceview.SVGraphics;
import com.dreamer.surfaceview.SVImageView;
import com.dreamer.surfaceview.SVObject;
import com.dreamer.surfaceview.SVTextView;
import com.dreamer.surfaceview.SVTools;

public class MainView extends SVObject {

	private Context mContext;
	private SVImageView mSVImageView;
	private SVTextView mTextView;
	private SVGraphics mSVGraphics;
	private SVTools mSVTools;
	private BitmapShader mBitmapShader;
	private LinearGradient mLinearGradient;
	private ComposeShader mComposeShader;
	private ShapeDrawable mShapeDrawable;
	private boolean isFirst = true;
	private int alpha =255;
	private String TAG = this.getClass().getSimpleName();
	private FocusView mFocusView;
	private int index = 0;
	private int index1;

	public MainView(Context ctx) {
		mContext = ctx;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);
		mSVImageView = new SVImageView();
		mSVImageView.setBackground(mContext, R.drawable.ic_launcher);

		mTextView = new SVTextView();
		mTextView.setText("kemm");

		mSVGraphics = new SVGraphics(SVGraphics.RECT);
		mSVGraphics.setRect(new Rect(5, 5, 125, 145));
		mSVGraphics.getPaint().setColor(Color.GREEN);

		mSVTools = SVTools.getInstance();
		mSVImageView.setBackground(mSVTools.scaleImage(
				BitmapFactory.decodeResource(mContext.getResources(),
						R.drawable.ic_launcher), 2.0f, 2.0f));
		mBitmapShader = new BitmapShader(bitmap,Shader.TileMode.REPEAT,Shader.TileMode.MIRROR); 
		mLinearGradient = new LinearGradient(0,0,100,100,new int[]{
				Color.RED,Color.GREEN,Color.BLUE,Color.WHITE
		},null,Shader.TileMode.REPEAT);
		mComposeShader = new ComposeShader(mBitmapShader,mLinearGradient,PorterDuff.Mode.DARKEN);

		mShapeDrawable = new ShapeDrawable(new OvalShape());
		mShapeDrawable.getPaint().setShader(mComposeShader);
		mShapeDrawable.setBounds(200, 100, 800, 800);
		
		
		mFocusView = new FocusView(mSVImageView);
		mFocusView.setDirXY(100, 100, 50, 50, index, index1);
		if(mSVFrame==null){
			Log.e("mSVFrame", "mSVFrame is null");
		}else{
			Log.e("mSVFrame", "mSVFrame is not null");
		}
		
	}

	public boolean onDraw(Canvas canvas) {
		
		
		mSVFrame.getSurfaceViewWindow().getPaint().setAlpha(alpha);
//		Log.d(TAG , "alpha:"+alpha);
		mSVImageView.setXY(0, 0);
		mSVImageView.onDraw(canvas);

		mTextView.setXY(100, 300);
		mTextView.onDraw(canvas);

		mSVGraphics.onDraw(canvas);
		
		mShapeDrawable.draw(canvas);
		mFocusView.setIndexXY(index,index1 );
		mFocusView.onDraw(canvas);
//		alpha --;
//		if(alpha<0)
//			alpha = 255;
		
		return true;
	}
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.e("MainView", "onKeyUp");
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			setVisible(false);
			((SurfaceViewActivity)mContext).getSVFrame().setFrameFocus(MainFrame.PFVIEW);
			return true;
		case KeyEvent.KEYCODE_DPAD_UP:
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			return true;
		case KeyEvent.KEYCODE_BACK:
//			setVisible(false);
			return true;
		default:
			return true;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		Log.e("MainView", "onKeyDown");
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			index1 --;
			if(index1 <0 )
				index1 = 0;
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			index1 ++ ;
			if(index1 >20 ){
				index1 = 20;
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			index --;
			if(index <0 )
				index = 0;
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			index ++ ;
			if(index >20 ){
				index = 20;
			}
			return true;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		case KeyEvent.KEYCODE_BACK:
			((SurfaceViewActivity)mContext).finish();
			return true;
		}
		return true;
	}
	
	protected boolean onKeyEvent(KeyEvent event) {
		Log.e("MainView", "onKeyEvent");
		switch (event.getAction()) {
		case KeyEvent.ACTION_DOWN:
			return onKeyDown(event.getKeyCode(), event);
		case KeyEvent.ACTION_UP:
			return onKeyUp(event.getKeyCode(), event);
		default:
			return false;
		}
	}
	
}
