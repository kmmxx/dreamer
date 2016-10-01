package com.dreamer.tool.bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GifView extends View {
	private static final String TAG = GifView.class.getSimpleName();
	private Movie mMovie;
	private long mPlayMovieTime;
	private static String DOWNLOAD_ADDR = "http://img3.3lian.com/2006/013/08/20051105155416881.gif";
	private Context context;

	public GifView(Context context, String gif) {
		super(context);
		this.context = context;
		initGif(gif);
	}

	private void initGif(String gif) {
		// TODO Auto-generated method stub
		if (gif == null)
			return;
		if (gif.contains("http:")) {
			readGifFormNet(gif);// 播放网络的gif格式图片
		} else {
			readGifFormNative(gif);// 播放本地的gif格式图片
		}
	}

	public GifView(Context context, AttributeSet attrs, String gif) {
		super(context, attrs);
		initGif(gif);
	}

	private void readGifFormNative(String gif) {
		InputStream in = null;
		try {
			in = context.getAssets().open(gif);
			mMovie = Movie.decodeStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void readGifFormNet(final String gif) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				httpTest(gif);
				mHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				invalidate();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void httpTest(String gif) {
		try {
			URL url = new URL(gif);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			// connection.setRequestMethod("GET");
			int size = connection.getContentLength();
			Log.e(TAG, "size = " + size);
			InputStream in = connection.getInputStream();
			byte[] array = streamToBytes(in);
			mMovie = Movie.decodeByteArray(array, 0, array.length);
			// mMovie = Movie.decodeStream(in);
			// mBmp = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] streamToBytes(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = is.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (java.io.IOException e) {
		}
		return os.toByteArray();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint p = new Paint();
		p.setAntiAlias(true);
		setLayerType(LAYER_TYPE_SOFTWARE, p);

		if (mMovie != null) {
			long now = android.os.SystemClock.uptimeMillis();
			if (mPlayMovieTime == 0) { // first time
				mPlayMovieTime = now;
			}
			int dur = mMovie.duration();
			if (dur == 0) {
				dur = 1000;
			}
			int relTime = (int) ((now - mPlayMovieTime) % dur);
			mMovie.setTime(relTime);
			mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight()
					- mMovie.height());
			invalidate();
		}
	}

}
