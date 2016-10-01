package com.dreamer.tool.fragment;

import com.dreamer.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MDialogFragment extends DialogFragment implements OnClickListener {
	private DialogInterface.OnClickListener mListener;
	TextView message;
	String msg = "";

	boolean mHasCancel = true;

	public static MDialogFragment createDlg(String msg, boolean hasCancel) {
		return createDlg(msg, hasCancel, false);
	}

	public static MDialogFragment createDlg(String msg, boolean hasCancel,
			boolean disableAllButton) {
		MDialogFragment f = new MDialogFragment();
		Bundle b = new Bundle();
		b.putString("msg", msg);
		b.putBoolean("hasCancel", hasCancel);
		b.putBoolean("disableAll", disableAllButton);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL,
				android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		msg = getArguments().getString("msg");
		mHasCancel = getArguments().getBoolean("hasCancel");
		message = (TextView) getView().findViewById(R.id.message);
		message.setText(msg);

		getView().findViewById(R.id.btn_ok).setOnClickListener(this);
		if (mHasCancel)
			getView().findViewById(R.id.btn_cancle).setOnClickListener(this);
		else
			getView().findViewById(R.id.btn_cancle).setVisibility(View.GONE);

		if (getArguments().getBoolean("disableAll")) {
			getView().findViewById(R.id.btn_ok).setEnabled(false);
			getView().findViewById(R.id.btn_cancle).setEnabled(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		getView().requestFocus();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_dialog, null);
	}

	@Override
	public void onClick(View v) {
		dismissAllowingStateLoss();
		if (mListener != null)
			mListener.onClick(getDialog(),
					v.getId() == R.id.btn_ok ? DialogInterface.BUTTON_POSITIVE
							: DialogInterface.BUTTON_NEGATIVE);
	}

	public void setListener(DialogInterface.OnClickListener mListener) {
		this.mListener = mListener;
	}
}
