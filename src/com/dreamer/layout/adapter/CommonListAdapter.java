package com.dreamer.layout.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dreamer.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater lin;

	List<Object> mObjects;

	public CommonListAdapter(Context context, List<Object> list) {
		this.context = context;
		this.mObjects = list;
		if (mObjects == null) {
			mObjects = new ArrayList<Object>();
		}
		lin = LayoutInflater.from(context);
	}

	public void addWrieLessObject(Object mObject) {
		mObjects.add(mObject);
	}

	public void removeObject(Object mObject) {
		mObjects.remove(mObject);
	}

	public void removeObject(int pos) {
		mObjects.remove(pos);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private class HoldView {
		ImageView iv1;
		TextView tv1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView hv;
		if (convertView == null) {
			hv = new HoldView();
			convertView = lin.inflate(R.layout.common_list_item, null);
			hv.iv1 = (ImageView) convertView.findViewById(R.id.v1);
			hv.tv1 = (TextView) convertView.findViewById(R.id.t1);
			convertView.setTag(hv);
		} else {
			hv = (HoldView) convertView.getTag();
		}
//		if (mObjects.get(position).getV1() != null)
//			hv.iv1.setBackgroundDrawable(new BitmapDrawable(mObjects
//					.get(position).getV1()));
//		if (mObjects.get(position).getT1() != null)
//			hv.tv1.setText(mObjects.get(position).getT1());
		return convertView;
	}

}
