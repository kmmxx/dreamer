package com.dreamer.layout.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Mdialog extends Dialog {
	private Activity activity;
	private int viewId;
	private UpdateDialogView mUpdateDialogView;
	private String TAG = "Mdialog";

	public Mdialog(Activity activity, int styleId, int viewId) {
		super(activity, styleId);
		this.activity = activity;
		this.viewId = viewId;
	}

	public Mdialog(Activity activity, int viewId) {
		super(activity, android.R.style.Theme);
		this.activity = activity;
		this.viewId = viewId;
	}

	public interface UpdateDialogView {
		public void updateDialogView(Activity activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setContentView(viewId);
		if(mUpdateDialogView!=null){
			mUpdateDialogView.updateDialogView(activity);
		}
	}

	public UpdateDialogView getmUpdateDialogView() {
		return mUpdateDialogView;
	}

	public void setmUpdateDialogView(UpdateDialogView mUpdateDialogView) {
		this.mUpdateDialogView = mUpdateDialogView;
	}

	@Override
	public String toString() {
		return "MDialog";
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
		Log.d(TAG, "onKeyUp:" + keyCode);
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return true;
		}

		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
		Log.d(TAG , "onKeyDown:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
			return true;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			break;
		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_DPAD_CENTER:
			break;
		}
		return false;
	}

}
