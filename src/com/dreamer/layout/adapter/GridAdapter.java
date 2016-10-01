package com.dreamer.layout.adapter;

import com.dreamer.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

// �Զ����ͼƬ������
public class GridAdapter extends BaseAdapter {

	private Context mContext;

	public GridAdapter(Context context) {
		mContext = context;
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(48, 48));
			imageView.setAdjustViewBounds(false);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(5, 5, 5, 5);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mThumbIds[position]);

		return imageView;
	}

	// ����ؼ�����ͼƬ���ݵ�����Դ
	private Integer[] mThumbIds = { R.drawable.test, R.drawable.ic_launcher,
			R.drawable.test, R.drawable.ic_launcher, R.drawable.test };
}
