package com.dreamer.layout;

import java.util.ArrayList;

import com.dreamer.R;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends Activity {
	private String TAG = this.getClass().getSimpleName();
	private String URL = "http://www.baidu.com/";
	private String content;
	private ViewPager mViewPager;
	private PagerTitleStrip mPagerTitleStrip;
	private TextView contextMenu;
	private TextView subMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate");
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.menu);
		initMenu();
	}

	private void initMenu() {
		// TODO Auto-generated method stub
		// 为 R.id.txt1 注册一个上下文菜单（在此 TextView 上长按，则会呼出上下文菜单）
		// 具体呼出的菜单内容需要重写 onCreateContextMenu 来创建
		contextMenu = (TextView) this.findViewById(R.id.contextMenu);
		this.registerForContextMenu(contextMenu);
		contextMenu.requestFocus();
		SystemTool.showInputMethod(this, contextMenu);
		// 为 R.id.txt2 注册一个上下文菜单
		subMenu = (TextView) this.findViewById(R.id.subMenu);
		this.registerForContextMenu(subMenu);
	}

	// 重写 onCreateContextMenu 用以创建上下文菜单
	// 重写 onContextItemSelected 用以响应上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// 创建 R.id.txt1 的上下文菜单
		if (v == (TextView) this.findViewById(R.id.contextMenu)) {

			// ContextMenu.setIcon() - 设置菜单的图标
			// ContextMenu.setHeaderTitle() - 设置菜单的标题
			menu.setHeaderIcon(R.drawable.test);
			menu.setHeaderTitle("我是菜单");

			// 用 ContextMenu.add() 来增加菜单项，返回值为 MenuItem
			// 第一个参数：组ID
			// 第二个参数：菜单项ID
			// 第三个参数：顺序号
			// 第四个参数：菜单项上显示的内容
			Intent intent = new Intent();
			intent.setClassName("com.dreamer",
					"com.dreamer.layout.ViewpagerActivity");
			menu.add(1, 0, 0, "菜单1").setIntent(intent);

			// MenuItem - 新增菜单项后的返回类型，针对菜单项的其他设置在此对象上操作
			menu.add(1, 1, 1, "菜单2").setCheckable(true);

		}
		// 创建 R.id.txt2 的上下文菜单（多级上下文菜单）
		else if (v == (TextView) this.findViewById(R.id.subMenu)) {

			// ContextMenu.addSubMenu("菜单名称") - 用来添加子菜单。子菜单其实就是一个特殊的菜单
			SubMenu sub = menu.addSubMenu("父菜单1");
			sub.setIcon(R.drawable.test);
			Intent intent = new Intent();
			intent.setClassName("com.dreamer",
					"com.dreamer.layout.ViewpagerActivity");
			sub.add(0, 0, 0, "菜单1").setIntent(intent);
			sub.add(0, 1, 1, "菜单2");
			sub.setGroupCheckable(1, true, true);

			SubMenu sub2 = menu.addSubMenu("父菜单2");
			sub2.setIcon(R.drawable.test);
			sub2.add(1, 0, 0, "菜单3");
			sub2.add(1, 1, 1, "菜单4");
			sub2.setGroupCheckable(1, true, false);

		}
	}

	// 重写 onCreateOptionsMenu 用以创建选项菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem menuItem = menu.add(0, 0, 0, "OptionsMenu");

		// MenuItem.setIcon() - 设置菜单项的图标
		// MenuItem.setTitleCondensed() - 菜单的简标题，如果指定了简标题的话，菜单项上的标题将会以此简标题为准
		// MenuItem.setAlphabeticShortcut() - 设置选中此菜单项的快捷键
		// 注：菜单项超过 6 个的话，第 6 个菜单将会变为 More 菜单，多余的菜单会在单击 More 菜单之后显示出来
		menuItem.setIcon(R.drawable.ic_launcher);
		menuItem.setTitleCondensed("菜单1");
		menuItem.setAlphabeticShortcut('0');

		menu.add(0, 1, 1, "菜单2").setIcon(R.drawable.ic_launcher);
		menu.add(0, 2, 2, "菜单3").setIcon(R.drawable.ic_launcher);
		menu.add(0, 3, 3, "菜单4");
		menu.add(0, 4, 4, "菜单5");
		menu.add(0, 5, 5, "菜单6");
		menu.add(0, 6, 6, "菜单7").setIcon(R.drawable.ic_launcher);
		menu.add(0, 7, 7, "菜单8").setIcon(R.drawable.ic_launcher);

		return true;
	}

	// 重写 onOptionsItemSelected 用以响应选项菜单
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		Toast.makeText(MenuActivity.this,
				"被单击的菜单项为：" + String.valueOf(item.getItemId()),
				Toast.LENGTH_SHORT).show();

		return false;
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
				Toast.makeText(MenuActivity.this,
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
