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
 * @author kmmxxx ͼƬ������
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
     * ����url����bitamp
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
     * ����uri����ͼƬ
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
     * �����ַ�������bitamp
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
     * ������ɫͼƬ
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
     * ��asset���ļ��д���bitmap
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
     * ��rsid�еõ�ͼƬ
     * 
     * @param ctx
     * @param rsid
     * @return
     */
    public static Bitmap createBitmap(Context ctx, int rsid) {
        return BitmapFactory.decodeResource(ctx.getResources(), rsid);
    }

    /**
     * ����ͼƬ��Ե�����ε�ͼƬ
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
     * ����ԭʼͼƬ�ĵ�ӰͼƬ
     * 
     * @param bm
     * @return
     */
    public static Bitmap createReflectBitmap(Bitmap bm) {
        int width = bm.getWidth(); // ͼƬ���
        int height = bm.getHeight(); // ͼƬ�߶�
        Matrix matrix = new Matrix();
        // ʵ��ͼƬ��ת90��
        matrix.preScale(1, -1);
        // ������ӰͼƬ����ԭʼͼƬ��һ���С��
        Bitmap reflectionImage = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return reflectionImage;
    }

    /**
     * @param originalImage ԭʼͼƬ
     * @param recyle �Ƿ����ԭʼͼƬ
     * @param x ԭʼͼƬ�ĸ߶�/x����Ϊ���ӵĸ߶�
     * @param y ԭʼͼƬ�뵹ӰͼƬ��ֱ��������ľ���
     * @return
     */
    public static Bitmap createReflectedImage(Bitmap originalImage, boolean recyle, int x, int y) {
        if (originalImage == null) {
            return null;
        }
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        Matrix matrix = new Matrix();
        // ʵ��ͼƬ��ת90��
        matrix.preScale(1, -1);
        // ������ӰͼƬ����ԭʼͼƬ��һ���С��
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width,
                height / 2, matrix, false);
        // ������ͼƬ��ԭͼƬ + ��ӰͼƬ��
        Bitmap finalReflection = Bitmap
                .createBitmap(width, (height + height / x), Config.ARGB_8888);
        // ��������
        Canvas canvas = new Canvas(finalReflection);
        canvas.drawBitmap(originalImage, 0, 0, null);
        // �ѵ�ӰͼƬ����������
        canvas.drawBitmap(reflectionImage, 0, height + y, null);
        Paint shaderPaint = new Paint();
        // �������Խ���LinearGradient����
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
                finalReflection.getHeight() + y, 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
        shaderPaint.setShader(shader);
        shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // ����������תͼƬ��С����Ȼ��ѽ���Ч���ӵ����У��ͳ�����ͼƬ�ĵ�ӰЧ����
        canvas.drawRect(0, height + y, width, finalReflection.getHeight(), shaderPaint);
        if (originalImage != null && recyle) {
            originalImage.recycle();
        }
        return finalReflection;
    }

    /**
     * ��һ��ͼƬ�и�ɶ��ͼƬ ���ֳ����������뽫һ��ͼƬ�и�ɶ��ͼƬ�����������ڿ���һ��ƴͼ����Ϸ��������Ҫ��ͼƬ�����и
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

    /** -----------------------------ͼƬת��---------------------------------- */

    /**
     * Bitmap ת���� byte ����
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
     * ����ת���� Bitmap
     * 
     * @param buffer
     * @return
     */
    public static Bitmap byteToBitmap(byte[] buffer) {
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
    }

    /**
     * Drawable ת����bitmap
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
     * Bitmap ת���� Drawable
     * 
     * @param bmp
     * @return
     */
    public static Drawable bitmap2Drawable(Bitmap bmp) {
        return new BitmapDrawable(bmp);
    }

    /**
     * BitmapDrawable ת���� Bitmap
     * 
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(BitmapDrawable drawable) {
        return drawable.getBitmap();
    }

    /** -----------------------------ͼƬ�任---------------------------------- */

    /**
     * ͼƬ��ת
     * 
     * @param bmp
     * 
     *            Ҫ��ת��ͼƬ
     * @param degree
     * 
     *            ͼƬ��ת�ĽǶȣ���ֵΪ��ʱ����ת����ֵΪ˳ʱ����ת
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    /**
     * ��������
     * 
     * @param bm
     * @param width_Ratio
     * @param height_Ratio
     * @return
     */
    public static Bitmap resizeBitmapToWH(Bitmap bm, float width_Ratio, float height_Ratio) {
        int width = bm.getWidth(); // ͼƬ���
        int height = bm.getHeight(); // ͼƬ�߶�
        Matrix matrix = new Matrix();
        matrix.postScale((float) width_Ratio, (float) height_Ratio); // ͼƬ�ȱ�����СΪԭ����fblRatio��
        Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);// ����λͼ
        return bmResult; // ���ر����ŵ�ͼƬ
    }

    /**
     * ͼƬ����
     * 
     * @param bm
     * @param scaleX
     * @param scaleY
     * 
     *            ֵС����Ϊ��С������Ϊ�Ŵ�
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bm, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }

    /**
     * ͼƬ��ת
     * 
     * @param bm
     * @param flag
     * 
     *            0Ϊˮƽ��ת��1Ϊ��ֱ��ת
     * @return
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
        case 0: // ˮƽ��ת
            floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
            break;
        case 1: // ��ֱ��ת
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
     * @param bmpOriginal ԭʼͼƬ
     * @return ���ɵĻҶ�ͼƬ
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
     * ʹͷ����
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
     *            ����λ0 ��ʾ�Ƿ�ı�ɫ�࣬��λ1��ʾ�Ƿ�ı䱥�Ͷ�,����λ2��ʾ�Ƿ�ı�������
     */
    /**
     * ���Ͷȱ�ʶfloat mSaturationValue = 0F;
     */
    public static final int FLAG_SATURATION = 0x0;
    /**
     * ���ȱ�ʶ float mLumValue = 1F;
     */
    public static final int FLAG_LUM = 0x1;
    /**
     * ɫ���ʶ float mHueValue = 0F;
     */
    public static final int FLAG_HUE = 0x2;

    public Bitmap handleImage(Bitmap bm, float mLumValue, float mSaturationValue, float mHueValue,
            int flag) {
        ColorMatrix mLightnessMatrix = null;
        ColorMatrix mSaturationMatrix = null;
        ColorMatrix mHueMatrix = null;
        ColorMatrix mAllMatrix = null;
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        // ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
        Canvas canvas = new Canvas(bmp); // �õ����ʶ���
        Paint paint = new Paint(); // �½� paint
        paint.setAntiAlias(true); // ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
        if (null == mAllMatrix) {
            mAllMatrix = new ColorMatrix();
        }
        if (null == mLightnessMatrix) {
            mLightnessMatrix = new ColorMatrix(); // ������ɫ�任�ľ���android
                                                  // λͼ��ɫ�仯������Ҫ�ǿ��ö������
        }
        if (null == mSaturationMatrix) {
            mSaturationMatrix = new ColorMatrix();
        }
        if (null == mHueMatrix) {
            mHueMatrix = new ColorMatrix();
        }
        switch (flag) {
        case FLAG_HUE: // ��Ҫ�ı�ɫ��
            mHueMatrix.reset();
            mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // �졢�̡�������������ͬ�ı���,���һ������1��ʾ͸���Ȳ����仯���˺�����ϸ˵���ο�
            // // android
            // doc
            break;
        case FLAG_SATURATION: // ��Ҫ�ı䱥�Ͷ�
            // saturation ���Ͷ�ֵ����С����Ϊ0����ʱ��Ӧ���ǻҶ�ͼ(Ҳ�����׻��ġ��ڰ�ͼ��)��
            // Ϊ1��ʾ���ͶȲ��䣬���ô���1������ʾ������
            mSaturationMatrix.reset();
            mSaturationMatrix.setSaturation(mSaturationValue);
            break;
        case FLAG_LUM: // ����
            // hueColor ����ɫ����ת�ĽǶ�,��ֵ��ʾ˳ʱ����ת����ֵ��ʾ��ʱ����ת
            mLightnessMatrix.reset(); // ��ΪĬ��ֵ
            mLightnessMatrix.setRotate(0, mLumValue); // �����ú�ɫ����ɫ������ת�ĽǶ�
            mLightnessMatrix.setRotate(1, mLumValue); // �������̺�ɫ����ɫ������ת�ĽǶ�
            mLightnessMatrix.setRotate(2, mLumValue); // ��������ɫ����ɫ������ת�ĽǶ�
            // �����൱�ڸı����ȫͼ��ɫ��
            break;
        }
        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix); // Ч������
        mAllMatrix.postConcat(mLightnessMatrix); // Ч������
        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// ������ɫ�任Ч��
        canvas.drawBitmap(bm, 0, 0, paint); // ����ɫ�仯���ͼƬ������´�����λͼ��
        // �����µ�λͼ��Ҳ����ɫ������ͼƬ
        return bmp;
    }

    /**
     * ͼƬ��߿����
     * 
     * @param bm ԭͼƬ
     * @param res �߿���Դ
     * @return
     */
    public static Bitmap combinateFrame(Bitmap bm, Context ctx, int[] res) {
        Bitmap bmp = createBitmap(ctx, res[0]);
        final int smallW = bmp.getWidth();
        final int smallH = bmp.getHeight();
        // ԭͼƬ�Ŀ��
        final int bigW = bm.getWidth();
        final int bigH = bm.getHeight();
        int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
        int hCount = (int) Math.ceil(bigH * 1.0 / smallH);
        // ��Ϻ�ͼƬ�Ŀ��
        int newW = (wCount + 2) * smallW;
        int newH = (hCount + 2) * smallH;
        // ���¶����С
        Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);
        canvas.drawRect(new Rect(0, 0, newW, newH), p);
        Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);
        // ��ԭͼ
        canvas.drawBitmap(bm, (newW - bigW - 2 * smallW) / 2 + smallW, (newH - bigH - 2 * smallH)
                / 2 + smallH, null);
        // ��߿�
        // ���ĸ���
        int startW = newW - smallW;
        int startH = newH - smallH;
        Bitmap leftTopBm = createBitmap(ctx, res[0]); // ���Ͻ�
        Bitmap leftBottomBm = createBitmap(ctx, res[2]); // ���½�
        Bitmap rightBottomBm = createBitmap(ctx, res[4]); // ���½�
        Bitmap rightTopBm = createBitmap(ctx, res[6]); // ���Ͻ�
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
        // �����ұ߿�
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
        // �����±߿�
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
     * ��ȡͼƬ���м��200X200������
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
     * ����ͼƬ
     * 
     * @param bmp �����е�ͼƬ
     * @param src ���е�λ��
     * @return ���к��ͼƬ
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
     * ��ӱ߿�
     * 
     * @param bm ԭͼƬ
     * @param res �߿���Դ
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
     * ����Ч��(���֮ǰ�����Ż���һ��)
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
     * ģ��Ч��
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
     * �ữЧ��(��˹ģ��)(�Ż�������������)
     * 
     * @param bmp
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // ��˹����
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
        int delta = 16; // ֵԽСͼƬ��Խ����Խ����Խ��
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
     * ͼƬ�񻯣�������˹�任��
     * 
     * @param bmp
     * @return
     */
    public static Bitmap sharpenImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // ������˹����
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
     * ����Ч��
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
     * ��ƬЧ��
     * 
     * @param bmp
     * @return
     */
    public static Bitmap film(Bitmap bmp) {
        // RGBA �����ֵ
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
     * ����Ч��
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
        final float strength = 150F; // ����ǿ�� 100~150
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
                // ���㵱ǰ�㵽�������ĵľ��룬ƽ������ϵ��������֮��ľ���
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // ���վ����С�������ӵĹ���ֵ
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
     * ͼƬЧ������
     * 
     * @param bmp �����˳ߴ��С�� Bitmap
     * @return
     */
    public static Bitmap overlay(Bitmap bmp, Context ctx, int rsid) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // �Ա߿�ͼƬ��������
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
     * ����Ч��
     * 
     * @param bmp
     * @param x �������ĵ��� bmp �е� x ����
     * @param y �������ĵ��� bmp �е� y ����
     * @param r ���εİ뾶
     * @return
     */
    public static Bitmap halo(Bitmap bmp, int x, int y, float r) {
        // ��˹����
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
        int delta = 18; // ֵԽСͼƬ��Խ����Խ����Խ��
        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));
                // ������������ĵ���ģ������
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
     * ��ɫ����Ч��
     * 
     * @param bmp
     * @return
     */
    public static Bitmap stria(Bitmap bmp) {
        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // ����һ����ͬ��С��ͼƬ
        // �������ص�� RGB ֵ
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height]; // ����ͼƬ�����ص���Ϣ
        bmp.getPixels(pixels, 0, width, 0, 0, width, height); // ������ͼƬ���浽һά�����У�ÿ
                                                              // width ������Ϊһ��
        final int delta = 40; // ÿ40�����صĸ߶���Ϊһ����λ
        final int blackHeight = 10; // ��ɫ����߶�
        final int BLACK = 0;
        for (int i = 0; i < height; i++) {
            // ��ͼƬ�Ľ���������
            // ÿ��30�����صĸ߶Ⱦͻ����һ���߶�Ϊ10�����صĺ�ɫ���
            // ÿ40������Ϊһ����λ��ǰ���10���ؾͻᱻ����ɺ�ɫ
            if (i % delta <= blackHeight) {
                for (int k = 0; k < width; k++) {
                    // �Ե�ǰ���ص㸳�µ� RGB ֵ
                    newR = BLACK;
                    newG = BLACK;
                    newB = BLACK;
                    // Color.argb()���ǽ��ĸ�0~255��ֵ���һ�����ص㣬Ҳ���� RGBA ֵ��A �� alpha����͸����
                    pixels[i * width + k] = Color.argb(255, newR, newG, newB); // �޸����ص�
                }
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height); // �򴴽�����ͬ��С����ͼƬ�����ص�ֵ
        long end = System.currentTimeMillis();
        return bitmap;
    }

    /**
     * ���ƹ�����������
     * 
     * @param width ���̵Ŀ��
     * @return
     */
    public static Bitmap chessbord(int width) {
        long start = System.currentTimeMillis();
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
        int[] pixels = new int[width * width];
        final int delta = width / 8; // ������ͼƬ�ֳ�8 X 8������delta ��ʾÿ������Ŀ�Ȼ�߶�
        final int blackPix = Color.BLACK; // ��ɫ��
        final int whitePix = Color.WHITE; // ��ɫ��
        int pixColor = whitePix;
        boolean isWhite = false; // ��ɫ�ı�ʶ
        for (int i = 0; i < width; i++) // ����
        {
            isWhite = !(i / delta % 2 == 0); // ��һ������ʼΪ��ɫ�������������λ���ǰ�ɫ
            for (int k = 0; k < width; k++) // ����
            {
                if (k / delta % 2 != 0) // �����ϵĺڰ���䣬ż���������Ǻ�ɫ
                {
                    pixColor = isWhite ? blackPix : whitePix; // ���ǰ����ǰ�ɫ��Ҫ��ɺ�ɫ
                } else {
                    pixColor = isWhite ? whitePix : blackPix; // ���������Ǻڰ����
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
     * ˮƽ�и�ͼƬ��Ҳ���ǽ�ͼƬ�ֳ���������
     * 
     * @param bmp ͼƬ
     * @param flag �Ǳ�����߻����ұߵı�ʶ
     * @return
     */
    public static Bitmap cropBitmapLandscape(Bitmap bmp, int flag) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        int startWidth = 0; // ��ʼ���λ��
        int endWidth = width / 2; // �������λ��
        Bitmap bitmap = Bitmap.createBitmap(endWidth, height, Bitmap.Config.RGB_565); // �����µ�ͼƬ��
        // ���ֻ��ԭ����һ��
        switch (flag) {
        case 1:
            break;
        case 8:
            startWidth = endWidth;
            endWidth = width;
            break;
        }
        Rect r = new Rect(startWidth, 0, endWidth, height); // ͼƬҪ�еķ�Χ
        Rect rect = new Rect(0, 0, width / 2, height); // ��ͼƬ�Ĵ�С
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, r, rect, null); // �и�ͼƬ
        return bitmap;
    }

    /**
     * ��ֱ�и�ͼƬ��Ҳ����˵��ͼƬ�г���������
     * 
     * @param bmp
     * @param flag ͼƬ�Ǳ������滹������
     * @return
     */
    public static Bitmap cropBitmapPortrait(Bitmap bmp, int flag) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        int startHeight = 0; // �߶ȵ���ʼλ��
        int endHeight = height / 2; // �߶ȵĽ���λ��
        Bitmap bitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.RGB_565); // ������ͼƬ��
        // �߶�ֻ��ԭ����һ��
        switch (flag) {
        case 1:
            break;
        case 8:
            startHeight = endHeight;
            endHeight = height;
            break;
        }
        Rect r = new Rect(0, startHeight, width, endHeight); // ͼƬҪ�еķ�Χ
        Rect rect = new Rect(0, 0, width, height / 2); // ��ͼƬ�Ĵ�С
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bmp, r, rect, null); // �и�ͼƬ
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
     * @return ����Բ��ͼƬ
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