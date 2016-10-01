
package com.dreamer.tool.message;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Interpolator.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.util.Log;

public class HandlerManager {

    private static HandlerManager manager;

    private Map<String, MyHandler> map;

    public static HandlerManager getInstance() {
        if (manager == null) {
            manager = new HandlerManager();
        }
        return manager;
    }

    private Context mContext;
    private boolean isPrepared = false;

    private HandlerManager() {
        map = new HashMap<String, MyHandler>();
    }

    public void prepare(Context context) {
        if (isPrepared) {
            return;
        }
        isPrepared = true;
        this.mContext = context;
    }

    public void addIdleHandler() {

        Looper.myQueue().addIdleHandler(new IdleHandler() {

            @Override
            public boolean queueIdle() {
                // ϵͳ����ʱ������Դ���ղ���
                return false;
            }
        });
    }

    public void addHandlerThread(String name) {
        HandlerThread tt = new HandlerThread(name);
        tt.start();// �����߳�
        Handler handler = new Handler(tt.getLooper());
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                System.out.println("ThreadName:" + Thread.currentThread().getName());
                ((HandlerThread) Thread.currentThread()).getLooper().quit();// �ر���Ϣѭ�����˳��߳�
            }

        }, 1000);
    }

    public MyHandler getHandler(Activity activity, String str) {
        if (!map.containsKey(str)) {
            MyHandler handler = new MyHandler(activity);
            map.put(str, handler);
        }
        return map.get(str);
    }

    public void clearMap() {
        if (map != null) {
            map.clear();
        }
    }

    public MyHandler addHandler(Activity activity, String str) {
        MyHandler handler = null;
        if (map.containsKey(str)) {
            handler = map.get(str);
        }
        if (handler == null) {
            handler = new MyHandler(activity);
            map.put(str, handler);
        }
        return handler;
    }

    public boolean removeHandler(String str) {
        if (map.containsKey(str)) {
            map.remove(str);
            return true;
        } else {
            return false;
        }
    }

    public class MyHandler extends Handler {

        private HandlerCallback handlerCallback;
        WeakReference<Activity> wr;

        public MyHandler(Activity me) {
            wr = new WeakReference<Activity>(me);
        }

        public MyHandler(Looper L) {
            super(L);
        }

        public void setHandlerCallback(HandlerCallback b) {
            this.handlerCallback = b;
        }

        // ���������д�˷���,��������
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Activity me = wr.get();
            if (me == null || msg == null) {
                Log.d("MyHandler", "handleMessage error ,activity is release......");
                return;
            }
            Log.d("MyHandler", "handleMessage......");
            super.handleMessage(msg);
            if (handlerCallback != null) {
                handlerCallback.handleMessage(msg);
            }
        }
    }

    public interface HandlerCallback {

        public void handleMessage(Message msg);
    }

    // ������һ���µ�Handlerʵ��ʱ, ����󶨵���ǰ�̺߳���Ϣ�Ķ�����,��ʼ�ַ�����
    // Handler����������, (1) : ��ʱִ��Message��Runnalbe ����
    // (2): ��һ������,�ڲ�ͬ���߳���ִ��.
    // ��������Ϣ,�����·���
    // post(Runnable)
    // postAtTime(Runnable,long)
    // postDelayed(Runnable,long)
    // sendEmptyMessage(int)
    // sendMessage(Message);
    // sendMessageAtTime(Message,long)
    // sendMessageDelayed(Message,long)

    // ���Ϸ����� post��ͷ�������㴦��Runnable����
    // sendMessage()�����㴦��Message����(Message����԰�������,)

}
