
package com.dreamer.tool.bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class BitmapTool {

    private static final Size ZERO_SIZE = new Size(0, 0);
    private static final Options OPTIONS_GET_SIZE = new Options();
    private static final Options OPTIONS_DECODE = new Options();
    private static final byte[] LOCKED = new byte[0];
    // �˶�����������Bitmap�Ļ���˳��,��֤���ʹ�õ�ͼƬ������
    private static final LinkedList CACHE_ENTRIES = new LinkedList();
    // �߳����󴴽�ͼƬ�Ķ���
    private static final Queue TASK_QUEUE = new LinkedList();
    // ������������ڴ����ͼƬ��key,��Ч��ֹ�ظ���ӵ����󴴽�����
    private static final Set TASK_QUEUE_INDEX = new HashSet();
    // ����Bitmap
    private static final Map IMG_CACHE_INDEX = new HashMap(); // ͨ��ͼƬ·��,ͼƬ��С
    private static int CACHE_SIZE = 20; // ����ͼƬ����
    static {
        OPTIONS_GET_SIZE.inJustDecodeBounds = true;
        // ��ʼ������ͼƬ�߳�,���ȴ�����
        new Thread() {

            {
                setDaemon(true);
            }

            public void run() {
                while (true) {
                    synchronized (TASK_QUEUE) {
                        if (TASK_QUEUE.isEmpty()) {
                            try {
                                TASK_QUEUE.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    QueueEntry entry = (QueueEntry) TASK_QUEUE.poll();
                    String key = createKey(entry.path, entry.width, entry.height);
                    TASK_QUEUE_INDEX.remove(key);
                    createBitmap(entry.path, entry.width, entry.height);
                }
            }
        }.start();
    }

    public static Bitmap getBitmap(String path, int width, int height) {
        if (path == null) {
            return null;
        }
        Bitmap bitMap = null;
        try {
            if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
                destoryLast();
            }
            bitMap = useBitmap(path, width, height);
            if (bitMap != null && !bitMap.isRecycled()) {
                return bitMap;
            }
            bitMap = createBitmap(path, width, height);
            String key = createKey(path, width, height);
            synchronized (LOCKED) {
                IMG_CACHE_INDEX.put(key, bitMap);
                CACHE_ENTRIES.addFirst(key);
            }
        } catch (OutOfMemoryError err) {
            destoryLast();
            System.out.println(CACHE_SIZE);
            return createBitmap(path, width, height);
        }
        return bitMap;
    }

    public static Size getBitMapSize(String path) {
        File file = new File(path);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
                return new Size(OPTIONS_GET_SIZE.outWidth, OPTIONS_GET_SIZE.outHeight);
            } catch (FileNotFoundException e) {
                return ZERO_SIZE;
            } finally {
                closeInputStream(in);
            }
        }
        return ZERO_SIZE;
    }

    // ------------------------------------------------------------------ private Methods
    // ��ͼƬ�������ͷ
    private static Bitmap useBitmap(String path, int width, int height) {
        Bitmap bitMap = null;
        String key = createKey(path, width, height);
        synchronized (LOCKED) {
            bitMap = (Bitmap) IMG_CACHE_INDEX.get(key);
            if (null != bitMap) {
                if (CACHE_ENTRIES.remove(key)) {
                    CACHE_ENTRIES.addFirst(key);
                }
            }
        }
        return bitMap;
    }

    // �������һ��ͼƬ
    private static void destoryLast() {
        synchronized (LOCKED) {
            String key = (String) CACHE_ENTRIES.removeLast();
            if (key.length() > 0) {
                Bitmap bitMap = (Bitmap) IMG_CACHE_INDEX.remove(key);
                if (bitMap != null && !bitMap.isRecycled()) {
                    bitMap.recycle();
                    bitMap = null;
                }
            }
        }
    }

    // ������
    private static String createKey(String path, int width, int height) {
        if (null == path || path.length() == 0) {
            return "";
        }
        return path + "_" + width + "_" + height;
    }

    // ͨ��ͼƬ·��,��ȸ߶ȴ���һ��Bitmap����
    private static Bitmap createBitmap(String path, int width, int height) {
        File file = new File(path);
        if (file.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(file);
                Size size = getBitMapSize(path);
                if (size.equals(ZERO_SIZE)) {
                    return null;
                }
                int scale = 1;
                int a = size.getWidth() / width;
                int b = size.getHeight() / height;
                scale = Math.max(a, b);
                synchronized (OPTIONS_DECODE) {
                    OPTIONS_DECODE.inSampleSize = scale;
                    Bitmap bitMap = BitmapFactory.decodeStream(in, null, OPTIONS_DECODE);
                    return bitMap;
                }
            } catch (FileNotFoundException e) {
                Log.v("BitMapUtil", "createBitmap==" + e.toString());
            } finally {
                closeInputStream(in);
            }
        }
        return null;
    }

    // �ر�������
    private static void closeInputStream(InputStream in) {
        if (null != in) {
            try {
                in.close();
            } catch (IOException e) {
                Log.v("BitMapUtil", "closeInputStream==" + e.toString());
            }
        }
    }

    // ͼƬ��С
    static class Size {

        private int width, height;

        Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    // ���л����������
    static class QueueEntry {

        public String path;
        public int width;
        public int height;
    }
    

public static byte[] decodeBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
		BitmapFactory.decodeFile(path, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		opts.inTempStorage = new byte[16 * 1024];
		FileInputStream is = null;
		Bitmap bmp = null;
		ByteArrayOutputStream baos = null;
		try {
			is = new FileInputStream(path);
			bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
			double scale = getScaling(opts.outWidth * opts.outHeight,
					1024 * 600);
			Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
					(int) (opts.outWidth * scale),
					(int) (opts.outHeight * scale), true);
			bmp.recycle();
			baos = new ByteArrayOutputStream();
			bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			bmp2.recycle();
			return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.gc();
		}
		return baos.toByteArray();
	}

	private static double getScaling(int src, int des) {
		/**
		 * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
		 */
		double scale = Math.sqrt((double) des / (double) src);
		return scale;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
