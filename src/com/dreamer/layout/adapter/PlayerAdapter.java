package com.dreamer.layout.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dreamer.app.hlsplayer.R;
import com.dreamer.app.hlsplayer.data.ProgramInfo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater lin;

	List<ProgramInfo> mObjects;

	public PlayerAdapter(Context context, List<ProgramInfo> list) {
		this.context = context;
		this.mObjects = list;
		if (mObjects == null) {
			mObjects = new ArrayList<ProgramInfo>();
		}
		lin = LayoutInflater.from(context);
	}
	
	public void setList(List<ProgramInfo> list){
		mObjects = list;
		if (mObjects == null) {
			mObjects = new ArrayList<ProgramInfo>();
		}
	}

	public void addObject(ProgramInfo mObject) {
		mObjects.add(mObject);
	}

	public void removeObject(ProgramInfo mObject) {
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
		TextView tv1;
		TextView tv2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView hv;
		if (convertView == null) {
			hv = new HoldView();
			convertView = lin.inflate(R.layout.common_list_item, null);
			hv.tv1 = (TextView) convertView.findViewById(R.id.t1);
			hv.tv2 = (TextView) convertView.findViewById(R.id.t2);
			convertView.setTag(hv);
		} else {
			hv = (HoldView) convertView.getTag();
		}
		if (mObjects.get(position).getNum() != -1)
			hv.tv1.setText(mObjects.get(position).getNum() + " . ");
		if (mObjects.get(position).getName() != null)
			hv.tv2.setText(mObjects.get(position).getName());
		return convertView;
	}

}
