package com.dreamer.opengles.expend;

import com.dreamer.R;
import com.dreamer.opengles.GlesLoadUtil;
import com.dreamer.opengles.GlesLoadedObject;
import com.dreamer.opengles.GlesMountion;
import com.dreamer.opengles.GlesToolkit;

import android.content.Context;

public class GlesObjectManager {

	private static GlesObjectManager mGlesViewData;
	private Context mContext;
	private boolean isPrepared = false;
	private GlesLoadedObject loadObject;
	private GlesMountion mGlesMountion;

	public static GlesObjectManager getInstance() {
		if (mGlesViewData == null) {
			mGlesViewData = new GlesObjectManager();
		}
		return mGlesViewData;
	}

	GlesObjectManager() {

	}

	public void prepare(Context ctx) {
		if (isPrepared) {
			return;
		}
		isPrepared = true;
		this.mContext = ctx;
	}

	public GlesLoadedObject getGlesLoadedObject() {
		if (loadObject == null) {
			loadObject = GlesLoadUtil.loadObjectWithNormalFromFile("ch_t.obj",
					mContext);
			if (loadObject != null)
				loadObject.setBackground(mContext, R.drawable.loading);
		}
		return loadObject;
	}

	public GlesMountion getGlesMountion() {
		// TODO Auto-generated method stub
		// if(mGlesMountion==null){
		// mGlesMountion = new GlesMountion(mContext);
		// mGlesMountion.initGlesMountion(GlesToolkit.getBitmap(mContext,
		// R.drawable.bar));
		// mGlesMountion.setBackground(mContext,
		// R.drawable.grass,R.drawable.rock);
		// }
		return mGlesMountion;
	}

}
