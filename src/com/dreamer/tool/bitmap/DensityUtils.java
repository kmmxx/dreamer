/*
 * Copyright (C) 2013  WhiteCat 鐧界尗 (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dreamer.tool.bitmap;

import android.content.Context;

/**
 * @Title DensityUtils
 * @Package com.ta.util.extend.draw
 * @Description DensityUtils dip与像素之间的转换
 * @author kmmxxx
 * @date 2013-1-22
 * @version V1.0
 */
public class DensityUtils {
	/**
	 * @param context
	 * @param dpValue
	 * @return dip转成像素
	 */
	public static int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
