package com.dreamer.layout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.dreamer.R;

	public class ScrollImageAdapter extends BaseAdapter {

		private Context mContext;

		public ScrollImageAdapter(Context context) {
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
			ImageView image = new ImageView(mContext);

			image.setImageResource(mThumbIds[position]);
			return image;
		}

	// ��Ҫ��ʾ��ͼƬ����
	private Integer[] mThumbIds = { R.drawable.ic_launcher, R.drawable.test,
			R.drawable.button01_1, R.drawable.button01_2, R.drawable.button01_3 };
}
