
package com.dreamer.tool.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

public class ShortcutUtil {

    public static void createShortCut(Activity act, int iconResId, int appnameResId) {

        // com.android.launcher.permission.INSTALL_SHORTCUT

        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // �������ظ�����
        shortcutintent.putExtra("duplicate", true);
        // ��Ҫ��ʵ������
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, act.getString(appnameResId));
        // ���ͼƬ
        Parcelable icon = Intent.ShortcutIconResource.fromContext(act.getApplicationContext(),
                iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // ������ͼƬ�����еĳ��������
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));
        // ���͹㲥
        act.sendBroadcast(shortcutintent);
    }
    
    public static void createShortCut(Activity act, int iconResId, String name) {

        // com.android.launcher.permission.INSTALL_SHORTCUT

        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // �������ظ�����
        shortcutintent.putExtra("duplicate", true);
        // ��Ҫ��ʵ������
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // ���ͼƬ
        Parcelable icon = Intent.ShortcutIconResource.fromContext(act.getApplicationContext(),
                iconResId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // ������ͼƬ�����еĳ��������
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(act.getApplicationContext(), act.getClass()));
        // ���͹㲥
        act.sendBroadcast(shortcutintent);
    }
}