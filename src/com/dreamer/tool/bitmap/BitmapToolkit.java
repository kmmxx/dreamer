/**
 * BitmapToolkit.java
 * TODO
 */

package com.dreamer.tool.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author kmmxxx 图片处理类
 */
public class BitmapToolkit {

    private static float[] carray = new float[20];
    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    private static LruCache<Integer, Bitmap> mMemoryCache = new LruCache<Integer, Bitmap>(maxMemory) {

        @Override
        protected int sizeOf(Integer key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

    public static void initLruCache() {
        mMemoryCache = new LruCache<Integer, Bitmap>(maxMemory) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public static void addBitmapToMemoryCache(int key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(int key) {
        return mMemoryCache.get(key);
    }

    public static void setCacheSize(int size) {
        maxMemory = size;
    }

    public static int getCacheSize() {
        return maxMemory;
    }

    /**
     * save bitmap
     * 
     * @param bitName
     * @param b void
     */
    public static void saveBitmap(String bitName, Bitmap b) {
        File f = new File("/storage/sdcard0/" + bitName + ".jpeg");
        try {
            f.createNewFile();
        } catch (Exception e) {
            return;
            // TODO: handle exception
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        b.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param mRawId
     * @return
     */
    public static Bitmap createBitmapByRawId(Context context, int mRawId) {
        Bitmap bitmap = null;
        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(mRawId);
        if (inputStream != null) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            } catch (Exception e) {} finally {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
        }
        return bitmap;
    }

    /**
     * 根据url创建bitamp
     * 
     * @param url
     * @return
     */
    public static Bitmap createBitmap(URL url) {
        try {
            return BitmapFactory.decodeStream(url.openStream());
        } catch (IOException e) {}
        return null;
    }

    /**
     * 返回uri引用图片
     * 
     * @param context
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 根据字符串创建bitamp
     * 
     * @param url
     * @return
     */
    public static Bitmap createBitmap(String url) {
        try {
            return BitmapFactory.decodeStream(new URL(url).openStream());
        } catch (IOException e) {}
        return null;
    }

    /**
     * 创建纯色图片
     * 
     * @param ctx
     * @param width
     * @param height
     * @param color
     * @return
     */
    public static Bitmap createBitmap(Context ctx, int width, int height, int color) {
        Bitmap mbmpTest = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvasTemp = new Canvas(mbmpTest);
        canvasTemp.drawColor(color);
        Drawable drawable = new BitmapDrawable(ctx.getResources(), mbmpTest);
        return ((BitmapDrawable) drawable).getBitmap();
    }
    

    /**
     * 从asset的文件中创建bitmap
     * 
     * @param url
     * @param mContext
     * @return
     */
    public static Bitmap createBitmap(String url, Context mContext) {
        Bitmap image = null;
        AssetManager am = mContext.getResources().getAssets();
        try {
            InputStream is = am.open(url);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {}
        return image;
    }

    /**
     * 从rsid中得到图片
     * 
     * @param ctx
     * @param rsid
     * @return
     */
    public static Bitmap createBitmap(Context ctx, int rsid) {
        return BitmapFactory.decodeResource(ctx.getResources(), rsid);
    }

    /**
     * 生成图片边缘带光晕的图片
     * 
     * @param b
     * @return
     */
    public static Drawable createHalo(Bitmap b) {
        Bitmap bitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint();
        p.setColor(Color.CYAN);
        canvas.drawBitmap(b.extractAlpha(), 0, 0, p);

        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[] { android.R.attr.state_pressed }, new BitmapDrawable(bitmap));
        return sld;
    }

    /**
     * 创建原始图片的倒影图片
     * 
     * @param bm
     * @return
     */
    public static Bitmap createReflectBitmap(Bitmap bm) {
        int width = bm.getWidth(); // 图片宽度
        int height = bm.getHeight(); // 图片高度
        Matrix matrix = new Matrix();
        // 实现图片翻转90度
        matrix.preScale(1, -1);
        // 创建倒影图片（是原始图片的一半大小）
        Bitmap reflectionImage = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return reflectionImage;
    }

    /**
     * @param originalImage 原始图片
     * @param recyle 是否回收原始图片
     * @param x 原始图片的高度/x，即为增加的高度
     * @param y 原始图片与倒影图片垂直方向相隔的距离
     * @return
     */
    public static Bitmap createReflectedImage(Bitmap originalImage, boolean recyle, int x, int y) {
        if (originalImage == null) {
            return null;
        }
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        // 实现图片翻转90度
        matrix.preScale(1, -1);
        // 创建倒影图片（是原始图片的一半大小）
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width,
                height / 2, matrix, false);
        // 创建总图片（原图片 + 倒影图片）
        Bitmap finalReflection = Bitmap
                .createBitmap(width, (height + height / x), Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(finalReflection);
        canvas.drawBitmap(originalImage, 0, 0, null);
        // 把倒影图片画到画布上
        canvas.drawBitmap(reflectionImage, 0, height + y, null);
        Paint shaderPaint = new Paint();
        // 创建线性渐变LinearGradient对象
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                finalReflection.getHeight() + y, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
        shaderPaint.setShader(shader);
        shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // 画布画出反转图片大小区域，然后把渐变效果加到其中，就出现了图片的倒影效果。
        canvas.drawRect(0, height + y, width, finalReflection.getHeight(), shaderPaint);
        if (originalImage != null && recyle) {
            originalImage.recycle();
        }
        return finalReflection;
    }

    /**
     * 将一个图片切割成多个图片 有种场景，我们想将一个图片切割成多个图片。比如我们在开发一个拼图的游戏，就首先要对图片进行切割。
     * 
     * @param bitmap
     * @param xPiece
     * @param yPiece
     * @return
     */
    public static Map<Integer, Bitmap> split(Bitmap bitmap, int xPiece, int yPiece) {
        Map<Integer, Bitmap> pieces = new HashMap<Integer, Bitmap>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pieceWidth = width / 3;
        int pieceHeight = height / 3;
        for (int i = 0; i < yPiece; i++) {
            for (int j = 0; j < xPiece; j++) {
                int index = j + i * xPiece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceHeight;
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth,
                        pieceHeight);
                pieces.put(index, bitmap1);
            }
        }
        return pieces;
    }

    /** -----------------------------图片转换---------------------------------- */

    /**
     * Bitmap 转换成 byte 数组
     * 
     * @param bmp
     * @return
     */
    public static byte[] bitmapToByte(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 数组转换成 Bitmap
     * 
     * @param buffer
     * @return
     */
    public static Bitmap byteToBitmap(byte[] buffer) {
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    /**
     * Drawable 转化成bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap 转换成 Drawable
     * 
     * @param bmp
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    /**
     * BitmapDrawable 转换成 Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(BitmapDrawable drawable) {
        return drawable.getBitmap();
    }

    /** -----------------------------图片变换---------------------------------- */

    /**
     * 图片旋转
     * 
     * @param bmp
     * 
     *            要旋转的图片
     * @param degree
     * 
     *            图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * 缩放纹理
     * 
     * @param bm
     * @param width_Ratio
     * @param height_Ratio
     * @return
     */
    public static Bitmap resizeBitmapToWH(Bitmap bm, float width_Ratio, float height_Ratio) {
        int width = bm.getWidth(); // 图片宽度
        int height = bm.getHeight(); // 图片高度
        Matrix matrix = new Matrix();
        matrix.postScale((float) width_Ratio, (float) height_Ratio); // 图片等比例缩小为原来的fblRatio倍
        Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);// 声明位图
        return bmResult; // 返回被缩放的图片
    }

    /**
     * 图片缩放
     * 
     * @param bm
     * @param scaleX
     * @param scaleY
     * 
     *            值小于则为缩小，否则为放大
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    /**
     * 图片反转
     * 
     * @param bm
     * @param flag
     * 
     *            0为水平反转，1为垂直反转
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
        case 0: // 水平反转
            floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
            break;
        case 1: // 垂直反转
            floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
            break;
        }
        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        return bmp;
    }

    /**
     * @param bmpOriginal 原始图片
     * @return 生成的灰度图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        paint.setColorFilter(null);
        c.drawBitmap(bmpGrayscale, 0, 0, paint);
        ColorMatrix cm = new ColorMatrix();
        getValueBlackAndWhite();
        cm.set(carray);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 使头像变灰
     * 
     * @param drawable
     */
    public static Drawable toGray(Drawable drawable) {
        drawable.mutate();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(cm);
        drawable.setColorFilter(cf);
        return drawable;
    }

    /**
     * 
     * @param flag
     * 
     *            比特位0 表示是否改变色相，比位1表示是否改变饱和度,比特位2表示是否改变明亮度
     */
    /**
     * 饱和度标识float mSaturationValue = 0F;
     */
    public static final int FLAG_SATURATION = 0x0;
    /**
     * 亮度标识 float mLumValue = 1F;
     */
    public static final int FLAG_LUM = 0x1;
    /**
     * 色相标识 float mHueValue = 0F;
     */
    public static final int FLAG_HUE = 0x2;

    public Bitmap handleImage(Bitmap bm, float mLumValue, float mSaturationValue, float mHueValue,
            int flag) {
        ColorMatrix mLightnessMatrix = null;
        ColorMatrix mSaturationMatrix = null;
        ColorMatrix mHueMatrix = null;
        ColorMatrix mAllMatrix = null;
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
        Canvas canvas = new Canvas(bmp); // 得到画笔对象
        Paint paint = new Paint(); // 新建 paint
        paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理
        if (null == mAllMatrix) {
            mAllMatrix = new ColorMatrix();
        }
        if (null == mLightnessMatrix) {
            mLightnessMatrix = new ColorMatrix(); // 用于颜色变换的矩阵，android
                                                  // 位图颜色变化处理主要是靠该对象完成
        }
        if (null == mSaturationMatrix) {
            mSaturationMatrix = new ColorMatrix();
        }
        if (null == mHueMatrix) {
            mHueMatrix = new ColorMatrix();
        }
        switch (flag) {
        case FLAG_HUE: // 需要改变色相
            mHueMatrix.reset();
            mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考
            // // android
            // doc
            break;
        case FLAG_SATURATION: // 需要改变饱和度
            // saturation 饱和度值，最小可设为0，此时对应的是灰度图(也就是俗话的“黑白图”)，
            // 为1表示饱和度不变，设置大于1，就显示过饱和
            mSaturationMatrix.reset();
            mSaturationMatrix.setSaturation(mSaturationValue);
            break;
        case FLAG_LUM: // 亮度
            // hueColor 就是色轮旋转的角度,正值表示顺时针旋转，负值表示逆时针旋转
            mLightnessMatrix.reset(); // 设为默认值
            mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
            mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
            mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度
            // 这里相当于改变的是全图的色相
            break;
        }
        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
        mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加
        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
        canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
        // 返回新的位图，也即调色处理后的图片
        return bmp;
    }

    /**
     * 图片与边框组合
     * 
     * @param bm 原图片
     * @param res 边框资源
     * @return
     */
    public static Bitmap combinateFrame(Bitmap bm, Context ctx, int[] res) {
        Bitmap bmp = createBitmap(ctx, res[0]);
        final int smallW = bmp.getWidth();
        final int smallH = bmp.getHeight();
        // 原图片的宽高
        final int bigW = bm.getWidth();
        final int bigH = bm.getHeight();
        int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
        int hCount = (int) Math.ceil(bigH * 1.0 / smallH);
        // 组合后图片的宽高
        int newW = (wCount + 2) * smallW;
        int newH = (hCount + 2) * smallH;
        // 重新定义大小
        Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);
        canvas.drawRect(new Rect(0, 0, newW, newH), p);
        Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);
        // 绘原图
        canvas.drawBitmap(bm, (newW - bigW - 2 * smallW) / 2 + smallW, (newH - bigH - 2 * smallH)
                / 2 + smallH, null);
        // 绘边框
        // 绘四个角
        int startW = newW - smallW;
        int startH = newH - smallH;
        Bitmap leftTopBm = createBitmap(ctx, res[0]); // 左上角
        Bitmap leftBottomBm = createBitmap(ctx, res[2]); // 左下角
        Bitmap rightBottomBm = createBitmap(ctx, res[4]); // 右下角
        Bitmap rightTopBm = createBitmap(ctx, res[6]); // 右上角
        canvas.drawBitmap(leftTopBm, 0, 0, null);
        canvas.drawBitmap(leftBottomBm, 0, startH, null);
        canvas.drawBitmap(rightBottomBm, startW, startH, null);
        canvas.drawBitmap(rightTopBm, startW, 0, null);
        leftTopBm.recycle();
        leftTopBm = null;
        leftBottomBm.recycle();
        leftBottomBm = null;
        rightBottomBm.recycle();
        rightBottomBm = null;
        rightTopBm.recycle();
        rightTopBm = null;
        // 绘左右边框
        Bitmap leftBm = createBitmap(ctx, res[1]);
        Bitmap rightBm = createBitmap(ctx, res[5]);
        for (int i = 0, length = hCount; i < length; i++) {
            int h = smallH * (i + 1);
            canvas.drawBitmap(leftBm, 0, h, null);
            canvas.drawBitmap(rightBm, startW, h, null);
        }
        leftBm.recycle();
        leftBm = null;
        rightBm.recycle();
        rightBm = null;
        // 绘上下边框
        Bitmap bottomBm = createBitmap(ctx, res[3]);
        Bitmap topBm = createBitmap(ctx, res[7]);
        for (int i = 0, length = wCount; i < length; i++) {
            int w = smallW * (i + 1);
            canvas.drawBitmap(bottomBm, w, startH, null);
            canvas.drawBitmap(topBm, w, 0, null);
        }
        bottomBm.recycle();
        bottomBm = null;
        topBm.recycle();
        topBm = null;
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    /**
     * 截取图片的中间的200X200的区域
     * 
     * @param bm
     * @return
     */
    public static Bitmap cropCenter(Bitmap bm) {
        int dstWidth = 200;
        int dstHeight = 200;
        int startWidth = (bm.getWidth() - dstWidth) / 2;
        int startHeight = ((bm.getHeight() - dstHeight) / 2);
        Rect src = new Rect(startWidth, startHeight, startWidth + dstWidth, startHeight + dstHeight);
        return dividePart(bm, src);
    }

    /**
     * 剪切图片
     * 
     * @param bmp 被剪切的图片
     * @param src 剪切的位置
     * @return 剪切后的图片
     */
    public static Bitmap dividePart(Bitmap bmp, Rect src) {
        int width = src.width();
        int height = src.height();
        Rect des = new Rect(0, 0, width, height);
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);
        canvas.drawBitmap(bmp, src, des, null);
        return croppedImage;
    }

    /**
     * 添加边框
     * 
     * @param bm 原图片
     * @param res 边框资源
     * @return
     */
    public static Bitmap addBigFrame(Bitmap bm, Context ctx, int res) {
        Bitmap bitmap = createBitmap(ctx, res);
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bm);
        Bitmap b = resizeBitmapToWH(bitmap, bm.getWidth(), bm.getHeight());
        array[1] = new BitmapDrawable(b);
        LayerDrawable layer = new LayerDrawable(array);
        return drawableToBitmap(layer);
    }

    /**
     * 怀旧效果(相对之前做了优化快一倍)
     * 
     * @param bmp
     * @return
     */
    public static Bitmap oldRemeber(Bitmap bmp) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG,
                        newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 模糊效果
     * 
     * @param bmp
     * @return
     */
    public static Bitmap blurImage(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newColor = 0;
        int[][] colors = new int[9][3];
        for (int i = 1, length = width - 1; i < length; i++) {
            for (int k = 1, len = height - 1; k < len; k++) {
                for (int m = 0; m < 9; m++) {
                    int s = 0;
                    int p = 0;
                    switch (m) {
                    case 0:
                        s = i - 1;
                        p = k - 1;
                        break;
                    case 1:
                        s = i;
                        p = k - 1;
                        break;
                    case 2:
                        s = i + 1;
                        p = k - 1;
                        break;
                    case 3:
                        s = i + 1;
                        p = k;
                        break;
                    case 4:
                        s = i + 1;
                        p = k + 1;
                        break;
                    case 5:
                        s = i;
                        p = k + 1;
                        break;
                    case 6:
                        s = i - 1;
                        p = k + 1;
                        break;
                    case 7:
                        s = i - 1;
                        p = k;
                        break;
                    case 8:
                        s = i;
                        p = k;
                    }
                    pixColor = bmp.getPixel(s, p);
                    colors[m][0] = Color.red(pixColor);
                    colors[m][1] = Color.green(pixColor);
                    colors[m][2] = Color.blue(pixColor);
                }
                for (int m = 0; m < 9; m++) {
                    newR += colors[m][0];
                    newG += colors[m][1];
                    newB += colors[m][2];
                }
                newR = (int) (newR / 9F);
                newG = (int) (newG / 9F);
                newB = (int) (newB / 9F);
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                newColor = Color.argb(255, newR, newG, newB);
                bitmap.setPixel(i, k, newColor);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        return bitmap;
    }

    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     * 
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 图片锐化（拉普拉斯变换）
     * 
     * @param bmp
     * @return
     */
    public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 拉普拉斯矩阵
        int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + n) * width + k + m];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * laplacian[idx] * alpha);
                        newG = newG + (int) (pixG * laplacian[idx] * alpha);
                        newB = newB + (int) (pixB * laplacian[idx] * alpha);
                        idx++;
                    }
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        return bitmap;
    }

    /**
     * 浮雕效果
     * 
     * @param bmp
     * @return
     */
    public static Bitmap emboss(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixColor = pixels[pos + 1];
                newR = Color.red(pixColor) - pixR + 127;
                newG = Color.green(pixColor) - pixG + 127;
                newB = Color.blue(pixColor) - pixB + 127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 底片效果
     * 
     * @param bmp
     * @return
     */
    public static Bitmap film(Bitmap bmp) {
        // RGBA 的最大值
        final int MAX_VALUE = 255;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = MAX_VALUE - pixR;
                newG = MAX_VALUE - pixG;
                newB = MAX_VALUE - pixB;
                newR = Math.min(MAX_VALUE, Math.max(0, newR));
                newG = Math.min(MAX_VALUE, Math.max(0, newG));
                newB = Math.min(MAX_VALUE, Math.max(0, newB));
                pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 光照效果
     * 
     * @param bmp
     * @return
     */
    public static Bitmap sunshine(Bitmap bmp) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int centerX = width / 2;
        int centerY = height / 2;
        int radius = Math.min(centerX, centerY);
        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = pixR;
                newG = pixG;
                newB = pixB;
                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 图片效果叠加
     * 
     * @param bmp 限制了尺寸大小的 Bitmap
     * @return
     */
    public static Bitmap overlay(Bitmap bmp, Context ctx, int rsid) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 对边框图片进行缩放
        Bitmap overlay = BitmapFactory.decodeResource(ctx.getResources(), rsid);
        int w = overlay.getWidth();
        int h = overlay.getHeight();
        float scaleX = width * 1F / w;
        float scaleY = height * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
        int pixColor = 0;
        int layColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixA = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;
        int layR = 0;
        int layG = 0;
        int layB = 0;
        int layA = 0;
        final float alpha = 0.5F;
        int[] srcPixels = new int[width * height];
        int[] layPixels = new int[width * height];
        bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
        overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pos = i * width + k;
                pixColor = srcPixels[pos];
                layColor = layPixels[pos];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixA = Color.alpha(pixColor);
                layR = Color.red(layColor);
                layG = Color.green(layColor);
                layB = Color.blue(layColor);
                layA = Color.alpha(layColor);
                newR = (int) (pixR * alpha + layR * (1 - alpha));
                newG = (int) (pixG * alpha + layG * (1 - alpha));
                newB = (int) (pixB * alpha + layB * (1 - alpha));
                layA = (int) (pixA * alpha + layA * (1 - alpha));
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                newA = Math.min(255, Math.max(0, layA));
                srcPixels[pos] = Color.argb(newA, newR, newG, newB);
            }
        }
        bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 光晕效果
     * 
     * @param bmp
     * @param x 光晕中心点在 bmp 中的 x 坐标
     * @param y 光晕中心点在 bmp 中的 y 坐标
     * @param r 光晕的半径
     * @return
     */
    public static Bitmap halo(Bitmap bmp, int x, int y, float r) {
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 18; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));
                // 不是中心区域的点做模糊处理
                if (distance > r * r) {
                    for (int m = -1; m <= 1; m++) {
                        for (int n = -1; n <= 1; n++) {
                            pixColor = pixels[(i + m) * width + k + n];
                            pixR = Color.red(pixColor);
                            pixG = Color.green(pixColor);
                            pixB = Color.blue(pixColor);
                            newR = newR + (int) (pixR * gauss[idx]);
                            newG = newG + (int) (pixG * gauss[idx]);
                            newB = newB + (int) (pixB * gauss[idx]);
                            idx++;
                        }
                    }
                    newR /= delta;
                    newG /= delta;
                    newB /= delta;
                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));
                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                    newR = 0;
                    newG = 0;
                    newB = 0;
                }
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        return bitmap;
    }

    /**
     * 黑色条纹效果
     * 
     * @param bmp
     * @return
     */
    public static Bitmap stria(Bitmap bmp) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // 创建一个相同大小的图片
        // 保存像素点的 RGB 值
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height]; // 保存图片的像素点信息
        bmp.getPixels(pixels, 0, width, 0, 0, width, height); // 将整个图片保存到一维数组中，每
                                                              // width 个长度为一行
        final int delta = 40; // 每40个像素的高度作为一个单位
        final int blackHeight = 10; // 黑色区域高度
        final int BLACK = 0;
        for (int i = 0; i < height; i++) {
            // 对图片的进行纵向处理
            // 每隔30个像素的高度就会产生一个高度为10个像素的黑色宽带
            // 每40个像素为一个单位，前面的10像素就会被处理成黑色
            if (i % delta <= blackHeight) {
                for (int k = 0; k < width; k++) {
                    // 对当前像素点赋新的 RGB 值
                    newR = BLACK;
                    newG = BLACK;
                    newB = BLACK;
                    // Color.argb()，是将四个0~255的值组成一个像素点，也就是 RGBA 值，A 是 alpha，即透明度
                    pixels[i * width + k] = Color.argb(255, newR, newG, newB); // 修改像素点
                }
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height); // 向创建的相同大小的新图片绘像素点值
        long end = System.currentTimeMillis();
        return bitmap;
    }

    /**
     * 绘制国际象棋棋盘
     * 
     * @param width 棋盘的宽高
     * @return
     */
    public static Bitmap chessbord(int width) {
        long start = System.currentTimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
        int[] pixels = new int[width * width];
        final int delta = width / 8; // 将整个图片分成8 X 8个区域，delta 表示每个区域的宽度或高度
        final int blackPix = Color.BLACK; // 黑色点
        final int whitePix = Color.WHITE; // 白色点
        int pixColor = whitePix;
        boolean isWhite = false; // 白色的标识
        for (int i = 0; i < width; i++) // 纵向
        {
            isWhite = !(i / delta % 2 == 0); // 第一块区域开始为黑色，纵向的奇数块位置是白色
            for (int k = 0; k < width; k++) // 横向
            {
                if (k / delta % 2 != 0) // 横向上的黑白相间，偶数块区域是黑色
                {
                    pixColor = isWhite ? blackPix : whitePix; // 如果前面的是白色则要变成黑色
                } else {
                    pixColor = isWhite ? whitePix : blackPix; // 在纵向上是黑白相间
                }
                //
                pixColor = k / delta % 2 != 0 ? (isWhite ? blackPix : whitePix)
                        : (isWhite ? whitePix : blackPix);
                pixels[i * width + k] = pixColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
        long end = System.currentTimeMillis();
        return bitmap;
    }

    /**
     * 水平切割图片，也就是将图片分成左右两块
     * 
     * @param bmp 图片
     * @param flag 是保留左边还是右边的标识
     * @return
     */
    public static Bitmap cropBitmapLandscape(Bitmap bmp, int flag) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        int startWidth = 0; // 起始宽度位置
        int endWidth = width / 2; // 结束宽度位置
        Bitmap bitmap = Bitmap.createBitmap(endWidth, height, Bitmap.Config.RGB_565); // 创建新的图片，
        // 宽度只有原来的一半
        switch (flag) {
        case 1:
            break;
        case 8:
            startWidth = endWidth;
            endWidth = width;
            break;
        }
        Rect r = new Rect(startWidth, 0, endWidth, height); // 图片要切的范围
        Rect rect = new Rect(0, 0, width / 2, height); // 新图片的大小
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, r, rect, null); // 切割图片
        return bitmap;
    }

    /**
     * 垂直切割图片，也就是说将图片切成上下两块
     * 
     * @param bmp
     * @param flag 图片是保留上面还是下面
     * @return
     */
    public static Bitmap cropBitmapPortrait(Bitmap bmp, int flag) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        int startHeight = 0; // 高度的起始位置
        int endHeight = height / 2; // 高度的结束位置
        Bitmap bitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.RGB_565); // 创建新图片，
        // 高度只有原来的一半
        switch (flag) {
        case 1:
            break;
        case 8:
            startHeight = endHeight;
            endHeight = height;
            break;
        }
        Rect r = new Rect(0, startHeight, width, endHeight); // 图片要切的范围
        Rect rect = new Rect(0, 0, width, height / 2); // 新图片的大小
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, r, rect, null); // 切割图片
        return bitmap;
    }

    public static Intent createImageUriIntent(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        return intent;
    }

    public static void getValueSaturation() {
        carray[0] = 5;
        carray[1] = 0;
        carray[2] = 0;
        carray[3] = 0;
        carray[4] = -254;
        carray[5] = 0;
        carray[6] = 5;
        carray[7] = 0;
        carray[8] = 0;
        carray[9] = -254;
        carray[10] = 0;
        carray[11] = 0;
        carray[12] = 5;
        carray[13] = 0;
        carray[14] = -254;
        carray[15] = 0;
        carray[16] = 0;
        carray[17] = 0;
        carray[18] = 5;
        carray[19] = -254;

    }

    private static void getValueBlackAndWhite() {
        carray[0] = (float) 0.308;
        carray[1] = (float) 0.609;
        carray[2] = (float) 0.082;
        carray[3] = 0;
        carray[4] = 0;
        carray[5] = (float) 0.308;
        carray[6] = (float) 0.609;
        carray[7] = (float) 0.082;
        carray[8] = 0;
        carray[9] = 0;
        carray[10] = (float) 0.308;
        carray[11] = (float) 0.609;
        carray[12] = (float) 0.082;
        carray[13] = 0;
        carray[14] = 0;
        carray[15] = 0;
        carray[16] = 0;
        carray[17] = 0;
        carray[18] = 1;
        carray[19] = 0;
    }

    /**
     * @param bitmap
     * @param pixels
     * @return 返回圆角图片
     */

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap
                .createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap loadStringBitmap(String mString, int fontSize) {
        if (mString == null)
            return null;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int alpha = 255;
        int red = 255;
        int green = 255;
        int blue = 255;
        int color = Color.argb(alpha, red, green, blue);
        paint.setColor(color);
        paint.setUnderlineText(false);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextAlign(Align.LEFT);

        paint.setTextSize(fontSize);
        Bitmap.Config bmConfig = Bitmap.Config.ARGB_8888;
        Paint.FontMetricsInt metrics = paint.getFontMetricsInt();
        int padding = 5;
        Bitmap bitmap = Bitmap.createBitmap(120, 60, bmConfig);
        Canvas canvas = new Canvas(bitmap);
        int x = padding;
        int y = -metrics.top + padding;
        bitmap.eraseColor(Color.TRANSPARENT);
        canvas.drawText(mString, x, y, paint);
        return bitmap;
    }

}