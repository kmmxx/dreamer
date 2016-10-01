package com.dreamer.tool.fragment;

import com.dreamer.R;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MFragment1 extends Fragment {
	private final String TAG = "MFragment";
	private MFragmentActivity parent;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		parent = (MFragmentActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, null);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((TextView) parent.findViewById(R.id.text1)).setText("f2");
	}
}
