/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
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
package com.dreamer.tool.message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @Title MailShareUtil
 * @Package com.ta.core.util.extend.share
 * @Description
 * @author
 * @date
 * @version V1.0
 */
public class MessageUtil {
	/**
	 * @param mContext
	 * @param title
	 * @param text
	 * @return
	 */
	public static Boolean sendMail(Context mContext, String title, String text) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		emailIntent.setType("text/plain");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		try {
			mContext.startActivity(Intent.createChooser(emailIntent,
					"Choose Email Client"));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static Boolean sendSms(Context mContext, String smstext) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
				smsToUri);
		mIntent.putExtra("sms_body", smstext);
		try {
			mContext.startActivity(mIntent);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
