package com.dreamer.layout;

import com.dreamer.R;
import com.dreamer.layout.view.GalleryAdapter;
import com.dreamer.layout.view.GalleryFlow;
import com.dreamer.layout.view.RotationGallery;
import com.dreamer.layout.view.RotationGallery.RotationGalleryListener;
import com.dreamer.tool.download.DownLoadCallback;
import com.dreamer.tool.download.DownloadManager;
import com.dreamer.tool.file.SharedPreferencesManager;
import com.dreamer.tool.http.AsyncHttpClient;
import com.dreamer.tool.http.AsyncHttpResponseHandler;
import com.dreamer.tool.http.RequestParams;
import com.dreamer.tool.http.SyncHttpClient;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private RotationGallery show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.gallery);
//		initGallyFlow();
		initGalleryLayout();
	}

	private void initGalleryLayout() {
		// TODO Auto-generated method stub
		int[] resID = { R.drawable.covermusic, R.drawable.covermovie, R.drawable.coverpicture };
		show = (RotationGallery) this.findViewById(R.id.rgview);
		show.setRotationGalleryListener(new RotationGalleryListener() {

			@Override
			public void onClick(int index) {
			}

			@Override
			public View buildView(int index) {
				View view = new View(GalleryActivity.this);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
						290, 280);// 434,95//379
				params.leftMargin = 533;
				params.topMargin = 390;
				view.setLayoutParams(params);
				view.setFocusable(false);
				return view;
			}
		});
		show.prepare(resID);
		show.show();
	}

	private void initGallyFlow() {
		// TODO Auto-generated method stub
		Integer[] images = { R.drawable.covermovie, R.drawable.covermusic,
				R.drawable.coverpicture };

		GalleryAdapter adapter = new GalleryAdapter(this, images);
		adapter.createReflectedImages();

		GalleryFlow galleryFlow = (GalleryFlow) this
				.findViewById(R.id.Gallery_main);
		galleryFlow.setFadingEdgeLength(0);
		galleryFlow.setSpacing(100);
		galleryFlow.setAdapter(adapter);

		galleryFlow.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						String.valueOf(position), Toast.LENGTH_SHORT).show();
			}

		});
		galleryFlow.setSelection(1);
		galleryFlow.requestFocus();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			default:
				break;
			}
		}
	};

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e(TAG, "onPause");
	};

	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

}
