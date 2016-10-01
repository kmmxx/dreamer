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
		// Ϊ R.id.txt1 ע��һ�������Ĳ˵����ڴ� TextView �ϳ����������������Ĳ˵���
		// ��������Ĳ˵�������Ҫ��д onCreateContextMenu ������
		contextMenu = (TextView) this.findViewById(R.id.contextMenu);
		this.registerForContextMenu(contextMenu);
		contextMenu.requestFocus();
		SystemTool.showInputMethod(this, contextMenu);
		// Ϊ R.id.txt2 ע��һ�������Ĳ˵�
		subMenu = (TextView) this.findViewById(R.id.subMenu);
		this.registerForContextMenu(subMenu);
	}

	// ��д onCreateContextMenu ���Դ��������Ĳ˵�
	// ��д onContextItemSelected ������Ӧ�����Ĳ˵�
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// ���� R.id.txt1 �������Ĳ˵�
		if (v == (TextView) this.findViewById(R.id.contextMenu)) {

			// ContextMenu.setIcon() - ���ò˵���ͼ��
			// ContextMenu.setHeaderTitle() - ���ò˵��ı���
			menu.setHeaderIcon(R.drawable.test);
			menu.setHeaderTitle("���ǲ˵�");

			// �� ContextMenu.add() �����Ӳ˵������ֵΪ MenuItem
			// ��һ����������ID
			// �ڶ����������˵���ID
			// ������������˳���
			// ���ĸ��������˵�������ʾ������
			Intent intent = new Intent();
			intent.setClassName("com.dreamer",
					"com.dreamer.layout.ViewpagerActivity");
			menu.add(1, 0, 0, "�˵�1").setIntent(intent);

			// MenuItem - �����˵����ķ������ͣ���Բ˵�������������ڴ˶����ϲ���
			menu.add(1, 1, 1, "�˵�2").setCheckable(true);

		}
		// ���� R.id.txt2 �������Ĳ˵����༶�����Ĳ˵���
		else if (v == (TextView) this.findViewById(R.id.subMenu)) {

			// ContextMenu.addSubMenu("�˵�����") - ��������Ӳ˵����Ӳ˵���ʵ����һ������Ĳ˵�
			SubMenu sub = menu.addSubMenu("���˵�1");
			sub.setIcon(R.drawable.test);
			Intent intent = new Intent();
			intent.setClassName("com.dreamer",
					"com.dreamer.layout.ViewpagerActivity");
			sub.add(0, 0, 0, "�˵�1").setIntent(intent);
			sub.add(0, 1, 1, "�˵�2");
			sub.setGroupCheckable(1, true, true);

			SubMenu sub2 = menu.addSubMenu("���˵�2");
			sub2.setIcon(R.drawable.test);
			sub2.add(1, 0, 0, "�˵�3");
			sub2.add(1, 1, 1, "�˵�4");
			sub2.setGroupCheckable(1, true, false);

		}
	}

	// ��д onCreateOptionsMenu ���Դ���ѡ��˵�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem menuItem = menu.add(0, 0, 0, "OptionsMenu");

		// MenuItem.setIcon() - ���ò˵����ͼ��
		// MenuItem.setTitleCondensed() - �˵��ļ���⣬���ָ���˼����Ļ����˵����ϵı��⽫���Դ˼����Ϊ׼
		// MenuItem.setAlphabeticShortcut() - ����ѡ�д˲˵���Ŀ�ݼ�
		// ע���˵���� 6 ���Ļ����� 6 ���˵������Ϊ More �˵�������Ĳ˵����ڵ��� More �˵�֮����ʾ����
		menuItem.setIcon(R.drawable.ic_launcher);
		menuItem.setTitleCondensed("�˵�1");
		menuItem.setAlphabeticShortcut('0');

		menu.add(0, 1, 1, "�˵�2").setIcon(R.drawable.ic_launcher);
		menu.add(0, 2, 2, "�˵�3").setIcon(R.drawable.ic_launcher);
		menu.add(0, 3, 3, "�˵�4");
		menu.add(0, 4, 4, "�˵�5");
		menu.add(0, 5, 5, "�˵�6");
		menu.add(0, 6, 6, "�˵�7").setIcon(R.drawable.ic_launcher);
		menu.add(0, 7, 7, "�˵�8").setIcon(R.drawable.ic_launcher);

		return true;
	}

	// ��д onOptionsItemSelected ������Ӧѡ��˵�
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		Toast.makeText(MenuActivity.this,
				"�������Ĳ˵���Ϊ��" + String.valueOf(item.getItemId()),
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
