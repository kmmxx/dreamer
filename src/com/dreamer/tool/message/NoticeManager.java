package com.dreamer.tool.message;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoticeManager {

	private static NoticeManager mNoticeManager;

	private long[] vibrate;

	public static NoticeManager getInstance() {
		if (mNoticeManager == null) {
			mNoticeManager = new NoticeManager();
		}
		return mNoticeManager;
	}

	private Context context;
	private NotificationManager mNotificationManager;

	public void prepare(Context context) {
		this.context = context;
	}

	private NoticeManager() {

	}

	public void showTextLong(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public void showTextLongAtCenter(String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showTextShortAtCenter(String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showTextShort(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public void showViewLong(View view) {
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	public void showTextView(String str, int size) {
		TextView text = new TextView(context);
		text.setBackgroundColor(Color.BLACK);
		text.setText(str);
		text.setTextSize(size);
		Toast toast = new Toast(context);
		toast.setView(text);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	public void showViewShort(View view) {
		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

	public void showTextAlert(Context context, View view, long time) {
		TranslateAnimation ani = new TranslateAnimation(0, 0, 300, 0);
		ani.setDuration(time);
		view.setVisibility(View.VISIBLE);
		view.startAnimation(ani);
	}
	
	public static void showImageToast(Context context, int ImageResourceId,
			CharSequence text, int duration, int textSize) {
		Toast toast = null;
		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		TextView textview = new TextView(context);
		textview.setText(text);
		textview.setTextSize(textSize);
		textview.setTextColor(Color.WHITE);
		LinearLayout ll = new LinearLayout(context);
		ll.setBackgroundResource(ImageResourceId);
		ll.setGravity(Gravity.CENTER);
		ll.addView(textview);
		toast.setView(ll);
		toast.show();
	}

	public void notice(Intent intent, int id, String title, int rsid,
			String sender, String msg) {
		// ָ������֪ͨ�����򿪵���ϸ��֪ͨҳ�棨����֪ͨ��� NotificationView��
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		// ʵ����һ��֪ͨ����ָ����ͼ��ͱ��⣨����ʾ������ʾ��
		Notification n = new Notification(rsid, title,
				System.currentTimeMillis());

		// ����֪ͨ�ķ����˺�֪ͨ����ϸ���ݣ�����ʾ������֪ͨ�б�����ʾ��
		n.setLatestEventInfo(context, sender, msg, contentIntent);

		// 100 �����ӳٺ��� 250 ���룬��ͣ 100 ��������� 500 ����
		n.vibrate = getVibrate();

		// ����֪ͨ�����е�һ������Ϊ֪ͨ��ʶ����
		getNotificationManager().notify(id, n);
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		return mNotificationManager;
	}

	public void cancel(int id) {
		getNotificationManager().cancel(id);
	}

	public void cancelAll() {
		getNotificationManager().cancelAll();
	}

	public long[] getVibrate() {
		if (vibrate == null) {
			vibrate = new long[] { 100, 250, 100, 500 };
		}
		return vibrate;
	}

	public void setVibrate(long[] vibrate) {
		this.vibrate = vibrate;
	}

}
