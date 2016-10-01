package com.dreamer.tool.file;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class SdcardManager {
	
	private static SdcardManager mSdcardManager;

	public static SdcardManager getInstance(){
		if(mSdcardManager == null){
			mSdcardManager = new SdcardManager();
		}
		return mSdcardManager;
	}

	private Context context;
	
	private SdcardManager(){}
	
	public void prepare(Context context){
		this.context = context;
	}
	
}
