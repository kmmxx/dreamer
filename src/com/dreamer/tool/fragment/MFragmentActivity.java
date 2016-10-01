package com.dreamer.tool.fragment;

import com.dreamer.R;
import com.dreamer.tool.log.Mlog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TabHost.OnTabChangeListener;

public class MFragmentActivity extends FragmentActivity implements
		OnKeyListener {

	private Handler mHandler;
	private MDialogFragment commonDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mlog.d("onCreate");
		setContentView(R.layout.fragment_main);
		mHandler = new Handler();
		findViewById(R.id.tab_channel_search).requestFocus();
		findViewById(R.id.tab_channel_search).setOnKeyListener(this);
		MFragmentManager.getInstance().prepare(this);
		MFragmentManager.getInstance().setRootView(android.R.id.tabcontent);
		MFragmentManager.getInstance().add(new MFragment(), "f2");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Mlog.d("onResume");
		// showCommonDialog();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Mlog.d("onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Mlog.d("onDestroy");
	}

	public void showCommonDialog() {
		commonDialog = MDialogFragment.createDlg(getString(R.string.app_name),
				true, true);
		commonDialog.setCancelable(true);
		commonDialog.show(getSupportFragmentManager(), "dlg-common");
		commonDialog.setListener(new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Mlog.d("onClick");
				commonDialog.dismiss();
				commonDialog.getDialog().dismiss();
			}
		});
	}

	@Override
	public boolean onKey(View view, int keycode, KeyEvent event) {
		// TODO Auto-generated method stub
		Mlog.d("onKey in");
		if (event.getAction() == KeyEvent.ACTION_UP
				&& (keycode == KeyEvent.KEYCODE_DPAD_UP)) {
			Mlog.d("onKey-up");
			MFragmentManager.getInstance().setTransactionAnimation(
					FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			MFragmentManager.getInstance().openFragment(new MFragment(), null);
		} else if (event.getAction() == KeyEvent.ACTION_UP
				&& (keycode == KeyEvent.KEYCODE_DPAD_DOWN)) {
			Mlog.d("onKey-down");
			MFragmentManager.getInstance().setTransactionAnimation(
					FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			MFragmentManager.getInstance().openNewFragment(new MFragment1());
		} else if (event.getAction() == KeyEvent.ACTION_UP
				&& (keycode == KeyEvent.KEYCODE_DPAD_LEFT)) {
			Mlog.d("onKey-down");
			MFragmentManager.getInstance().openSubFragment("f1",
					new MFragment1());
		}
		return false;
	}

}
