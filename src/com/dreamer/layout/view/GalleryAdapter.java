package com.dreamer.layout.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

	int mGalleryItemBackground;
	private Context mContext;
	private Integer[] mImageIds;
	private ImageView[] mImages;

	public GalleryAdapter(Context c, Integer[] ImageIds) {
		mContext = c;
		mImageIds = ImageIds;
		mImages = new ImageView[mImageIds.length];
	}

	/**
	 * 创建倒影效果
	 * 
	 * @return
	 */
	public boolean createReflectedImages() {
		// 倒影图和原图之间的距�?
		final int reflectionGap = 4;
		int index = 0;

		for (int imageId : mImageIds) {
			// 返回原图解码之后的bitmap对象
			Bitmap originalImage = BitmapFactory.decodeResource(
					mContext.getResources(), imageId);
			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			// 创建矩阵对象
			Matrix matrix = new Matrix();

			// 指定�?��角度�?,0为坐标进行旋�?
			// matrix.setRotate(30);

			// 指定矩阵(x轴不变，y轴相�?
			matrix.preScale(1, -1);

			// 将矩阵应用到该原图之中，返回�?��宽度不变，高度为原图1/2的�?影位�?
			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			// 创建�?��宽度不变，高度为原图+倒影图高度的位图
			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			// 将上面创建的位图初始化到画布
			Canvas canvas = new Canvas(bitmapWithReflection);
			canvas.drawBitmap(originalImage, 0, 0, null);

			Paint deafaultPaint = new Paint();
			deafaultPaint.setAntiAlias(false);
			// canvas.drawRect(0, height, width, height +
			// reflectionGap,deafaultPaint);
			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			paint.setAntiAlias(false);

			/**
			 * 参数�?为渐变起初点坐标x位置�?参数�?为y轴位置，
			 * 参数三和�?分辨对应渐变终点�?�?��参数为平铺方式，
			 * 这里设置为镜像Gradient是基于Shader类，�
			 * ?��我们通过Paint的setShader方法来设置这个渐�?
			 */
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.MIRROR);
			// 设置阴影
			paint.setShader(shader);
			paint.setXfermode(new PorterDuffXfermode(
					android.graphics.PorterDuff.Mode.DST_IN));
			// 用已经定义好的画笔构建一个矩形阴影渐变效�?
			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);

			// 创建�?��ImageView用来显示已经画好的bitmapWithReflection
			ImageView imageView = new ImageView(mContext);
			imageView.setImageBitmap(bitmapWithReflection);
			// 设置imageView大小 ，也就是�?��显示的图片大�?
			imageView.setLayoutParams(new GalleryFlow.LayoutParams(506, 484));
			// imageView.setScaleType(ScaleType.MATRIX);
			mImages[index++] = imageView;
		}
		return true;
	}

	@SuppressWarnings("unused")
	private Resources getResources() {
		return null;
	}

	public int getCount() {
		// return mImageIds.length;
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return position;
		// return mImages[position % 3];
	}

	public long getItemId(int position) {
		return position;
		// return position % 3;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("GalleryAdapter", "position: " + position
				+ " position % mImageIds.length: " + position
				% mImageIds.length);
		ImageView imageview = new ImageView(parent.getContext());
		imageview.setImageDrawable(mImages[position % 3].getDrawable());
		imageview.setLayoutParams(new GalleryFlow.LayoutParams(506, 484));
		return imageview;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}
}
