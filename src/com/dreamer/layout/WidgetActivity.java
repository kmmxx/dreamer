package com.dreamer.layout;

import java.util.ArrayList;
import java.util.Random;

import com.dreamer.R;
import com.dreamer.example.jni.HelloJni;
import com.dreamer.layout.adapter.GridAdapter;
import com.dreamer.layout.adapter.ImageAdapter;
import com.dreamer.layout.adapter.ScrollImageAdapter;
import com.dreamer.layout.view.GalleryAdapter;
import com.dreamer.layout.view.GalleryFlow;
import com.dreamer.tool.download.DownLoadCallback;
import com.dreamer.tool.download.DownloadManager;
import com.dreamer.tool.file.SharedPreferencesManager;
import com.dreamer.tool.http.AsyncHttpClient;
import com.dreamer.tool.http.AsyncHttpResponseHandler;
import com.dreamer.tool.http.RequestParams;
import com.dreamer.tool.http.SyncHttpClient;
import com.dreamer.tool.system.SystemTool;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Chronometer;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

public class WidgetActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private String URL = "http://www.baidu.com/";
	private String content;
	private Chronometer mChronometer;
	private TextSwitcher switcher;
	private ImageSwitcher mSwitcher;
	private ListView list;
	private ScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ����������������Ӧ�ó���ı���������ʾ����������״��
		requestWindowFeature(Window.FEATURE_PROGRESS);
		// ����������������Ӧ�ó���ı���������ʾ��������ԲȦ״��
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.view);
		initTextView();
		initProgressbar();
		initAutoTextComplete();
		intiMutiAutoText();
		// initVideoPlayer();
		initWebView();
		initTabView();

		initSpinner();

		initTimer();

		initTextSwitcher();

		initGally();

		initGridView();

		initRadioGroup();

		initScrollView();

	}

	private void initScrollView() {
		// TODO Auto-generated method stub
		ScrollImageAdapter ia = new ScrollImageAdapter(this);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(ia);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClassName(getPackageName(), getPackageName()
						+ ".MainActivity");
				startActivity(i);
			}
		});
		sv = (ScrollView) findViewById(R.id.scrollview);
		sv.smoothScrollTo(0, 0);
		sv.requestFocus();
	}

	private void testJni() {
		// TODO Auto-generated method stub
		String s = new HelloJni().stringFromJNI();
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	private void bluetoothTest() {
		// TODO Auto-generated method stub
		// Set up the custom title
		// mTitle = (TextView) findViewById(R.id.title_left_text);
		// mTitle.setText(R.string.app_name);
		// mTitle = (TextView) findViewById(R.id.title_right_text);
		// mBluetoothManager = BluetoothManager.getInstance();
		// mBluetoothManager.prepare(this);
		// mBluetoothManager.setHandler(mHandler);
		// mBluetoothAdapter = mBluetoothManager.getBluetoothAdapter();
		// if (mBluetoothManager == null) {
		// return;
		// }
		// if (mBluetoothManager.checkBluetoothIsEnabled()) {
		// if (mBluetoothManager == null)
		// doSomething();
		// }
		// if (mBluetoothManager != null) {
		// // Only if the state is STATE_NONE, do we know that we haven't
		// // started already
		// if (mBluetoothManager.getState() == BluetoothManager.STATE_NONE) {
		// // Start the Bluetooth chat services
		// mBluetoothManager.start();
		// }
		// }
	}

	private void initRadioGroup() {
		// TODO Auto-generated method stub
		// RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		// // setOnCheckedChangeListener() - ��Ӧ��ѡ�����ڵ�ѡ������仯ʱ���¼�
		// group.setOnCheckedChangeListener(new
		// RadioGroup.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		//
		// }
		// });

	}

	private void initGridView() {
		// TODO Auto-generated method stub
		GridView gridView = (GridView) findViewById(R.id.gridView);
		// ָ������ؼ���������Ϊ�Զ����ͼƬ������
		gridView.setAdapter(new GridAdapter(this));
		gridView.requestFocus();
	}

	private void initGally() {
		// TODO Auto-generated method stub
		mSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
		mSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				ImageView image = new ImageView(WidgetActivity.this);
				image.setMinimumHeight(200);
				image.setMinimumWidth(200);
				image.setScaleType(ImageView.ScaleType.FIT_CENTER);
				image.setLayoutParams(new ImageSwitcher.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				return image;
			}

		});
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		// Ϊ����ͼ�����ָ��һ��������
		gallery.setAdapter(new ImageAdapter(WidgetActivity.this));

		// ��Ӧ ������ͼ�б���ѡ��ĳ������ͼ��� �¼�
		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			private Integer[] mImageIds = { R.drawable.test,
					R.drawable.ic_launcher, R.drawable.test,
					R.drawable.ic_launcher, R.drawable.test };

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(WidgetActivity.this, String.valueOf(position),
						Toast.LENGTH_SHORT).show();
				mSwitcher.setImageResource(mImageIds[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private void initTextSwitcher() {
		// TODO Auto-generated method stub
		switcher = (TextSwitcher) findViewById(R.id.textSwitcher);
		// ָ��ת������ ViewSwitcher.ViewFactory
		switcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				// TODO Auto-generated method stub
				TextView textView = new TextView(WidgetActivity.this);
				textView.setTextSize(36);
				return textView;
			}
		});

		// ���õ���͵����Ķ���Ч��
		Animation in = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out);
		switcher.setInAnimation(in);
		switcher.setOutAnimation(out);

	}

	private void initTimer() {
		// TODO Auto-generated method stub
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		// ���ü�ʱ������ʾ��ʱ���ʽ
		mChronometer.setFormat("��ʱ��(%s)");
		mChronometer.start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				long currentTime = 0;
				// TODO Auto-generated method stub
				while (true) {
					if (currentTime == 0) {
						currentTime = System.currentTimeMillis();
					}
					if (System.currentTimeMillis() - currentTime > 6000) {
						mChronometer.stop();
					}
					if (System.currentTimeMillis() - currentTime > 8000) {
						mHandler.sendEmptyMessage(1);
						currentTime = 0;
					}
					SystemTool.sleep(100);
				}

			}
		}).start();

	}

	private void initSpinner() {
		// TODO Auto-generated method stub
		Spinner spinner = (Spinner) findViewById(R.id.spinner);

		// ����������ؼ��ı����ı�
		spinner.setPrompt("��ѡ��");
		// ʵ������������ָ����ʾ��ʽ������Դ
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ary, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// setOnItemSelectedListener() - ��Ӧ�������ѡ��ֵ�����仯���¼�
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void initTabView() {
		// TODO Auto-generated method stub
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		TabWidget tabWidget = tabHost.getTabWidget();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("tab1",
						getResources().getDrawable(R.drawable.test))
				.setContent(R.id.view1));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("tab3")
				.setContent(R.id.view3));

		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("tab2")
				.setContent(R.id.view2));

		final int tabs = tabWidget.getChildCount();
		Log.i(TAG, "***tabWidget.getChildCount() : " + tabs);

		final int tabWidth = 90;
		final int tabHeight = 45;

		for (int i = 0; i < tabs; i++) {
			/* */
			final View view = tabWidget.getChildAt(i);
			view.getLayoutParams().width = tabWidth;
			view.getLayoutParams().height = tabHeight;
			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.black));
			MarginLayoutParams tvMLP = (MarginLayoutParams) tv
					.getLayoutParams();
			tvMLP.bottomMargin = 8;
		}
	}

	private void initWebView() {
		// TODO Auto-generated method stub
		WebView webView = (WebView) findViewById(R.id.webView);

		// �����������ʹ���֧�� JavaScript
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		// ������������
		webView.clearCache(true);
		// ָ���������Ҫ������ url ��ַ
		webView.loadUrl("http://www.baidu.com/");
		// ָ���������Ҫ������ html ����
		// webView.loadData("<a href='http://webabcd.cnblogs.com/'>webabcd</a>",
		// "text/html", "utf-8");
	}

	private void initVideoPlayer() {
		// TODO Auto-generated method stub
		VideoView videoView = (VideoView) findViewById(R.id.videoView);

		// ָ����Ҫ���ŵ���Ƶ�ĵ�ַ
		videoView.setVideoURI(Uri.parse("android.resource://com.dreamer/"
				+ R.raw.test));
		// videoView.setVideoPath();

		// ���ò������Ŀ�����
		videoView.setMediaController(new MediaController(this));
		// ��ʼ������Ƶ
		videoView.start();
	}

	private void intiMutiAutoText() {
		// TODO Auto-generated method stub
		// ʵ������������ָ����ʾ��ʽ������Դ
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, ary);
		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.MultiAutoCompleteTextView);
		textView.setAdapter(adapter);

		// ���ö��ֵ֮��ķָ������˴�Ϊ����
		textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	private void initAutoTextComplete() {
		// TODO Auto-generated method stub
		// ʵ������������ָ����ʾ��ʽ������Դ
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, ary);
		AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView);
		// ָ���Զ���ɿؼ���������
		textView.setAdapter(adapter);
	}

	// �Զ���ɿؼ����������ݵ�����Դ
	private String[] ary = new String[] { "abc", "abcd", "abcde", "abcdef",
			"abcdefg", "hij", "hijk", "hijkl", "hijklm", "234", "1111", "123", };

	private void initProgressbar() {
		// TODO Auto-generated method stub

		setTitle("ProgressBar");

		// �ڱ���������ʾ����������״��
		setProgressBarVisibility(true);
		// �ڱ���������ʾ��������ԲȦ״��
		setProgressBarIndeterminateVisibility(true);

		// ָ���������Ľ���
		setProgress(50 * 100);
		setSecondaryProgress(75 * 100);
	}

	private void initTextView() {
		// TODO Auto-generated method stub
		TextView textView = (TextView) findViewById(R.id.TextView);
		String html = "<html><head><title>TextViewʹ��HTML</title></head><body><p><strong>ǿ��</strong></p><p><em>б��</em></p>"
				+ "<p><a href=\"http://www.dreamdu.com/xhtml/\">������HTML����</a>ѧϰHTML!</p><p><font color=\"#aabb00\">��ɫ1"
				+ "</p><p><font color=\"#00bbaa\">��ɫ2</p><h1>����1</h1><h3>����2</h3><h6>����3</h6><p>����>С��<</p><p>"
				+ "����������ͼƬ</p><img src=\"http://avatar.csdn.net/0/3/8/2_zhang957411207.jpg\"/></body></html>";

		textView.setMovementMethod(ScrollingMovementMethod.getInstance());// ����
		textView.setText(Html.fromHtml(html));
		SystemTool.hideInputMethod(this, textView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		testJni();
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(WidgetActivity.this,
						"goto URL:" + URL + "content:" + content,
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				switcher.setText(String.valueOf(new Random().nextInt()));
				mChronometer.setBase(SystemClock.elapsedRealtime());
				mChronometer.start();
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
