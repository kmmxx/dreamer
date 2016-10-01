package com.dreamer.layout.view;

import com.dreamer.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @author kmm mListView = new MlistView(this);
 *         mListView.setOnMyItemSelectedListener(new onMyItemSelectedListener()
 *         {
 * @Override public void onNothingSelected(AdapterView<?> adapterView) { // TODO
 *           Auto-generated method stub
 * 
 *           }
 * @Override public void onItemSelected(AdapterView<?> adapterView, View view,
 *           int position, long id) { // TODO Auto-generated method stub
 * 
 *           } }); 布局采用listview.xml
 * 
 */
public class MlistView {

	private ListView listView;
	private Activity activity;
	private int index;
	private int lastIndex;
	private View focus;
	private float fromYDelta;
	private float toYDelta;
	private long duration;
	private int selectedPosition;
	private int indexMax = 5;

	public MlistView(Activity activity, ListView listView, View focus) {
		this.listView = listView;
		this.activity = activity;
		this.focus = focus;
	}

	public MlistView(Activity activity) {
		this.activity = activity;
		listView = (ListView) activity.findViewById(R.id.ListView);
		focus = activity.findViewById(R.id.focus);
		focus.clearAnimation();
		focus.setVisibility(View.INVISIBLE);
	}

	public void setAdapter(BaseAdapter adapter) {
		if (listView != null) {
			listView.setAdapter(adapter);
		}
	}

	public void setOnMyItemSelectedListener(
			final onMyItemSelectedListener listener) {
		if (listener != null) {
			focus.setVisibility(View.VISIBLE);
		}
		listView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				toYDelta = view.getY();
				if (getSelectedPosition() < position) {
					changeIndex(1);
				} else {
					changeIndex(-1);
				}
				setSelectedPosition(position);
				listener.onItemSelected(adapterView, view, position, id);

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				// TODO Auto-generated method stub
				listener.onNothingSelected(adapterView);
			}
		});

	}

	public void changeIndex(int i) {
		if (listView != null && focus != null && listView.getAdapter() == null
				|| listView.getAdapter().getCount() == 0)
			return;
		lastIndex = index;
		index += i;
		if (index < 0) {
			index = 0;
			return;
		}
		if (index > indexMax) {
			index = indexMax;
			return;
		}
		focus.clearAnimation();
		Animation anim = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		focus.startAnimation(anim);
		fromYDelta = toYDelta;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setContext(Activity activity) {
		this.activity = activity;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getIndexMax() {
		return indexMax;
	}

	public void setIndexMax(int indexMax) {
		this.indexMax = indexMax;
	}

	public View getFocus() {
		return focus;
	}

	public void setFocus(View focus) {
		this.focus = focus;
	}

	public float getFromYDelta() {
		return fromYDelta;
	}

	public void setFromYDelta(float fromYDelta) {
		this.fromYDelta = fromYDelta;
	}

	public float getToYDelta() {
		return toYDelta;
	}

	public void setToYDelta(float toYDelta) {
		this.toYDelta = toYDelta;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public interface onMyItemSelectedListener {
		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id);

		public void onNothingSelected(AdapterView<?> adapterView);
	}

}
