package com.dreamer.opengles.expend;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.dreamer.R;
import com.dreamer.opengles.GlesImage;
import com.dreamer.opengles.GlesMatrix;
import com.dreamer.opengles.GlesText;
import com.dreamer.opengles.GlesToolkit;
import com.dreamer.tool.util.PageUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;

public class GlesListView {

	List<String> list;
	List<GlesText> texts;
	int showLine = 1;
	private Context mContex;
	int width;
	int height;
	int spaceY;
	int spaceX;
	private Align textAlign;
	private int textColor;
	private float textSize;
	private int barX = 877;
	private int barY = 20;
	private int srcX;
	private int srcY;
	private int index;
	private boolean isShow = false;
	private GlesImage listFocus;
	private int page = 0;
	private int maxPage = 0;
	private String TAG = GlesListView.class.getSimpleName();
	private float pageDistance;
	private boolean showBar = false;
	private GlesImage backGround;
	private GlesImage bar;
	private GlesImage bgBar;
	private boolean fontStyleNormal = true;

	public GlesListView(Context context, int width, int height) {
		this.mContex = context;
		this.height = height;
		this.width = width;
		textSize = 23;
		textAlign = Align.LEFT;
		textColor = Color.GRAY;
		setShowLine(1);
	}

	public void setShowLine(int l) {
		this.showLine = l;
		if(texts !=null){
			for(GlesText t :texts){
				t.onRecyle();
			}
		}
		texts = new ArrayList<GlesText>();
		for (int i = 0; i < showLine; i++) {
			GlesText text = new GlesText(mContex, width, height);
			text.setTextAlign(textAlign);
			text.setTextColor(textColor);
			text.setTextSize(textSize);
			if (fontStyleNormal)
				text.setFontStyleNormal(true);
			texts.add(text);
		}
	}

	public void setFontStyleNormal(boolean b) {
		this.fontStyleNormal = b;
	}

	public boolean isShowBar() {
		return showBar;
	}

	public void setShowBar(boolean showBar) {
		this.showBar = showBar;
	}

	public Align getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(Align textAlign) {
		this.textAlign = textAlign;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public int getSpaceY() {
		return spaceY;
	}

	public void setSpaceY(int spaceY) {
		this.spaceY = spaceY;
	}

	public int getSpaceX() {
		return spaceX;
	}

	public void setSpaceX(int spaceX) {
		this.spaceX = spaceX;
	}

	public void setSrcX(int x, int y) {
		this.srcX = x;
		this.srcY = y;
	}

	public void setList(List<String> list) {
		synchronized (this) {
			this.list = list;
			page = 0;
			index = 0;
			setMaxPage(PageUtil.getMaxPage(list, showLine));
			updateView();
		}
	}

	public void changeIndex(int i) {
		if (list == null)
			return;
		index += i;
		Log.d(TAG, "changeIndex before:" + index + " page:" + page
				+ " listsize:" + list.size());
		if ((index + page * showLine) > list.size() - 1) {
			index = 0;
			changePage(0);
			Log.d(TAG, "changeIndex after1:" + index + " page:" + page);
			return;
		}
		if (index > showLine - 1) {
			index = 0;
			changePage(1);
		}
		if (index + page * showLine < 0) {
			index = 0;
			changePage(maxPage - 1);
			index = list.size() % showLine - 1;
			if (index < 0) {
				index = showLine - 1;
			}
			Log.d(TAG, "changeIndex after11:" + index + " page:" + page);
			return;
		}
		if (index < 0) {
			index = 0;
			changePage(-1);
			index = showLine - 1;
		}
		Log.d(TAG, "changeIndex after2:" + index + " page:" + page);
	}

	public void changePage(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			page = 0;
		} else {
			Log.d(TAG, "changeIndex changePage1:" + maxPage + " page:" + page);
			page += i;
			if (page < 0) {
				page = 0;
			}
			if (page > maxPage - 1) {
				page = 0;
				index = 0;
			}
		}
		Log.d(TAG, "changeIndex changePage2:" + maxPage + " page:" + page);
		updateView();
	}

	private void updateView() {
		// TODO Auto-generated method stub
		synchronized (this) {
			List<String> l = PageUtil.getSubList(list,
					index + page * showLine, index + (page + 1) * showLine);
			for (int i = 0; i < showLine; i++) {
				if (l != null && i < l.size())
					texts.get(i).setText(l.get(i));
				else
					texts.get(i).setText("");
			}
		}
	}

	public void add(String str) {
		synchronized (this) {
			if (list == null) {
				list = new ArrayList<String>();
			}
			list.add(str);
			setMaxPage(PageUtil.getMaxPage(list, showLine));
		}
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
		updateBar();
	}

	public void updateBar() {
		// TODO Auto-generated method stub
		if (!showBar)
			return;
		if (bar != null) {
			if (maxPage != 0) {
				pageDistance = (float) 508 / maxPage;
			}
			Log.d(TAG, "page:" + page + " pageDistance:" + pageDistance);
			bar.setFloatWidthHeight(10, pageDistance, R.drawable.ic_launcher);
		}
	}

	public void setBar(GlesImage bar) {
		this.bar = bar;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isShow() {
		return isShow;
	}

	public void showFocus(boolean isShow) {
		this.isShow = isShow;
		if (showBar)
			updateBar();
	}

	public int getSelectItem() {
		return index + page * showLine;
	}

	public boolean onDraw() {
		if (list == null || list.size() == 0) {
			return false;
		}
		if (backGround != null) {
			GlesMatrix.pushMatrix();
			backGround.onDraw();
			GlesMatrix.popMatrix();
		}
		for (int i = 0; i < showLine; i++) {
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(GlesToolkit.translateX(srcX + spaceX * i),
					GlesToolkit.translateY(srcY + spaceY * i), 0);
			texts.get(i).onDraw();
			if (i == index && isShow) {
				GlesMatrix.translate(GlesToolkit.translateX(50),
						GlesToolkit.translateY(30), 0);
				listFocus.onDraw();
			}
			GlesMatrix.popMatrix();
		}

		if (showBar) {
			GlesMatrix.pushMatrix();
			GlesMatrix.translate(GlesToolkit.translateX(barX + 3),
					GlesToolkit.translateY(barY), 0);
			if (bgBar != null) {
				bgBar.onDraw();
			}
			GlesMatrix.translate(GlesToolkit.translateX(-3),
					GlesToolkit.translateY(page * pageDistance), 0);
			if (bar != null)
				bar.onDraw();
			GlesMatrix.popMatrix();
		}
		return true;
	}

	public int getBarX() {
		return barX;
	}

	public void setBarX(int barX) {
		this.barX = barX;
	}

	public int getBarY() {
		return barY;
	}

	public void setBarY(int barY) {
		this.barY = barY;
	}

	public void setListFocus(GlesImage listFocus) {
		this.listFocus = listFocus;
	}

	public int getLine() {
		// TODO Auto-generated method stub
		return showLine;
	}

	public void setBackGround(GlesImage bg) {
		// TODO Auto-generated method stub
		this.backGround = bg;
	}

	public void setBgBar(GlesImage bar) {
		// TODO Auto-generated method stub
		this.bgBar = bar;
	}

}
