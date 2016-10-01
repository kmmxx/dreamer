package com.dreamer.layout;

import java.util.ArrayList;
import java.util.List;

import com.dreamer.R;
import com.dreamer.layout.adapter.ImageAdapter;
import com.dreamer.tool.animation.AnimationManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class ViewpagerActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private String URL = "http://www.baidu.com/";
	private String content;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private ListView list;
	private ScrollView sv;
	private GridView gv;
	private SlidingDrawer sd;
	private ImageView iv;
	private List<ResolveInfo> apps;
	private View view1;
	private View view2;
	private View view3;
	private View view4;
	private Button btn_start;
	private LinearLayout layout;
	private LinearLayout animLayout;
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.viewpager);
		// viewpager
		initViewPager();

	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		// ��Ҫ��ҳ��ʾ��Viewװ��������
		createFloatView();
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.viewpager_item1, null);
		view2 = mLi.inflate(R.layout.viewpager_item2, null);
		view3 = mLi.inflate(R.layout.viewpager_item3, null);
		view4 = mLi.inflate(R.layout.viewpager_item4, null);
		// view1 srcollview
		initView1();
		// view3 ����Ч��
		initView3();
		// view4 ����Ч��
		initView4();

		// ÿ��ҳ���Title����
		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		final ArrayList<String> titles = new ArrayList<String>();
		titles.add("viewpager_item1");
		titles.add("viewpager_item2");
		titles.add("viewpager_item3");
		titles.add("viewpager_item4");
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};
		mViewPager.setAdapter(mPagerAdapter);

	}

	private void initView4() {
		// TODO Auto-generated method stub
		btn_start = (Button) view4.findViewById(R.id.btn_start);
		btn_start.setOnClickListener(onClickListener);

		layout = (LinearLayout) view4.findViewById(R.id.layout);
		animLayout = (LinearLayout) view4.findViewById(R.id.animLayout);
		leftLayout = (LinearLayout) view4.findViewById(R.id.leftLayout);
		rightLayout = (LinearLayout) view4.findViewById(R.id.rightLayout);

	}

	View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_start:
				doOpenDoor();
				break;

			default:
				break;
			}
		}

	};
	private Button btn_floatView;
	private WindowManager wm;
	private LayoutParams params;
	private boolean isAdded;

	private void initView1() {
		// TODO Auto-generated method stub
		ImageAdapter ia = new ImageAdapter(this);
		sv = (ScrollView) view1.findViewById(R.id.act_solution_1_sv);
		sv.smoothScrollTo(0, 0);
		list = (ListView) view1.findViewById(R.id.list);
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
				AnimationManager.startWindowAnimation(ViewpagerActivity.this,
						R.anim.scale_in, R.anim.scale_out);
			}
		});
	}

	private void doOpenDoor() {

		layout.setVisibility(View.GONE);
		animLayout.setVisibility(View.VISIBLE);
		Animation leftOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_in_left);
		Animation rightOutAnimation = AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_out_right);

		leftLayout.setAnimation(leftOutAnimation);
		rightLayout.setAnimation(rightOutAnimation);
		leftOutAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				leftLayout.setVisibility(View.GONE);
				rightLayout.setVisibility(View.GONE);
			}
		});

	}

	private void initView3() {
		// TODO Auto-generated method stub
		loadApps();
		gv = (GridView) view3.findViewById(R.id.allApps);
		sd = (SlidingDrawer) view3.findViewById(R.id.sliding);
		iv = (ImageView) view3.findViewById(R.id.imageViewIcon);
		// iv.setImageResource(R.drawable.ic_launcher);
		// setStateDrawable(iv);
		iv.setBackgroundDrawable(createHalo(BitmapFactory.decodeResource(
				this.getResources(), R.drawable.ic_launcher)));
		gv.setAdapter(new GridAdapter());
		sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// ������
		{
			@Override
			public void onDrawerOpened() {
				// iv.setImageResource(R.drawable.ic_launcher);// ��Ӧ�������¼�
				startAnim(iv); // ����ͼƬ��Ϊ���µ�
			}
		});
		sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				// iv.setImageResource(R.drawable.ic_launcher);// ��Ӧ�س����¼�
				startAnim(iv);
			}
		});
	}

	private void setStateDrawable(ImageView v) {
		BitmapDrawable bd = (BitmapDrawable) v.getDrawable();
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint p = new Paint();
		p.setColor(Color.CYAN);
		canvas.drawBitmap(b.extractAlpha(), 0, 0, p);

		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[] { android.R.attr.state_pressed },
				new BitmapDrawable(bitmap));
		v.setBackgroundDrawable(sld);
	}

	private Drawable createHalo(Bitmap b) {
		Bitmap bitmap = Bitmap.createBitmap(b.getWidth(), b.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint p = new Paint();
		p.setColor(Color.CYAN);
		canvas.drawBitmap(b.extractAlpha(), 0, 0, p);

		StateListDrawable sld = new StateListDrawable();
		sld.addState(new int[] { android.R.attr.state_pressed },
				new BitmapDrawable(bitmap));
		return sld;
	}

	private void createFloatView1() {
		// ���帡�����ڲ���
		final LinearLayout mFloatLayout;
		final WindowManager.LayoutParams wmParams;
		// ���������������ò��ֲ����Ķ���
		final WindowManager mWindowManager;

		final Button mFloatView;
		wmParams = new WindowManager.LayoutParams();
		// ��ȡ����WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager) getApplication().getSystemService(
				getApplication().WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager--->" + mWindowManager);
		// ����window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// ����ͼƬ��ʽ��Ч��Ϊ����͸��
		wmParams.format = PixelFormat.RGBA_8888;
		// ���ø������ڲ��ɾ۽���ʵ�ֲ���������������������ɼ����ڵĲ�����
		wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
		// ������������ʾ��ͣ��λ��Ϊ����ö�
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ�������gravity
		wmParams.x = 0;
		wmParams.y = 0;

		// �����������ڳ�������
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		/*
		 * // �����������ڳ������� wmParams.width = 200; wmParams.height = 80;
		 */

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// ��ȡ����������ͼ���ڲ���
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.main, null);
		// ���mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);
		// �������ڰ�ť
		mFloatView = (Button) mFloatLayout.findViewById(R.id.button);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		Log.i(TAG, "Width/2--->" + mFloatView.getMeasuredWidth() / 2);
		Log.i(TAG, "Height/2--->" + mFloatView.getMeasuredHeight() / 2);
		// ���ü����������ڵĴ����ƶ�
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// getRawX�Ǵ���λ���������Ļ�����꣬getX������ڰ�ť������
				wmParams.x = (int) event.getRawX()
						- mFloatView.getMeasuredWidth() / 2;
				Log.i(TAG, "RawX" + event.getRawX());
				Log.i(TAG, "X" + event.getX());
				// ��25Ϊ״̬���ĸ߶�
				wmParams.y = (int) event.getRawY()
						- mFloatView.getMeasuredHeight() / 2 - 25;
				Log.i(TAG, "RawY" + event.getRawY());
				Log.i(TAG, "Y" + event.getY());
				// ˢ��
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false; // �˴����뷵��false������OnClickListener��ȡ��������
			}
		});

		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(ViewpagerActivity.this, "onClick",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * ����������
	 */
	private void createFloatView() {
		btn_floatView = new Button(getApplicationContext());
		btn_floatView.setText("������");
		btn_floatView.setBackgroundResource(R.drawable.ic_launcher);

		wm = (WindowManager) getApplicationContext().getSystemService(
				Context.WINDOW_SERVICE);
		params = new WindowManager.LayoutParams();

		// ����window type
		 params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		/*
		 * �������Ϊparams.type = WindowManager.LayoutParams.TYPE_PHONE; ��ô���ȼ��ή��һЩ,
		 * ������֪ͨ�����ɼ�
		 */

		params.format = PixelFormat.RGBA_8888; // ����ͼƬ��ʽ��Ч��Ϊ����͸��

		// ����Window flag
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * �����flags���Ե�Ч����ͬ���������� ���������ɴ������������κ��¼�,ͬʱ��Ӱ�������¼���Ӧ��
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL |
		 * LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */

		// �����������ĳ��ÿ�
		params.width = 100;
		params.height = 100;

		// ������������Touch����
		btn_floatView.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY;
			int paramX, paramY;

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = params.x;
					paramY = params.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					params.x = paramX + dx;
					params.y = paramY + dy;
					// ����������λ��
					wm.updateViewLayout(btn_floatView, params);
					break;
				}
				return true;
			}
		});

		wm.addView(btn_floatView, params);
		isAdded = true;
	}

	private void loadApps() {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		apps = getPackageManager().queryIntentActivities(intent, 0);
	}

	public class GridAdapter extends BaseAdapter {
		public GridAdapter() {
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return apps.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return apps.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView imageView = null;
			if (convertView == null) {
				imageView = new ImageView(ViewpagerActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
			} else {
				imageView = (ImageView) convertView;
			}
			ResolveInfo ri = apps.get(position);
			imageView.setImageDrawable(ri.activityInfo
					.loadIcon(getPackageManager()));
			return imageView;
		}

	}

	public void startAnim(ImageView img) { // ����ҡһҡ��������

		AnimationSet animup = new AnimationSet(true);

		TranslateAnimation mup0 = new TranslateAnimation(

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,

		-0.5f);

		mup0.setDuration(300);

		TranslateAnimation mup1 = new TranslateAnimation(

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,

		+0.5f);

		mup1.setDuration(300);

		// �ӳ�ִ��1��

		mup1.setStartOffset(300);

		animup.addAnimation(mup0);

		animup.addAnimation(mup1);

		// ��ͼƬ�Ķ���Ч�������

		img.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);

		TranslateAnimation mdn0 = new TranslateAnimation(

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,

		+0.5f);

		mdn0.setDuration(300);

		TranslateAnimation mdn1 = new TranslateAnimation(

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,

		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,

		-0.5f);

		mdn1.setDuration(300);

		// �ӳ�ִ��1��

		mdn1.setStartOffset(300);

		animdn.addAnimation(mdn0);

		animdn.addAnimation(mdn1);

		// ��ͼƬ����Ч�������

		img.startAnimation(animdn);
		// setStateDrawable(img);
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
				Toast.makeText(ViewpagerActivity.this,
						"goto URL:" + URL + "content:" + content,
						Toast.LENGTH_LONG).show();
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
