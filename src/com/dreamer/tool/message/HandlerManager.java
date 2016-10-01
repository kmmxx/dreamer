
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
                // 系统空闲时进行资源回收操作
                return false;
            }
        });
    }

    public void addHandlerThread(String name) {
        HandlerThread tt = new HandlerThread(name);
        tt.start();// 启动线程
        Handler handler = new Handler(tt.getLooper());
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                System.out.println("ThreadName:" + Thread.currentThread().getName());
                ((HandlerThread) Thread.currentThread()).getLooper().quit();// 关闭消息循环，退出线程
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

        // 子类必须重写此方法,接受数据
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

    // 当创建一个新的Handler实例时, 它会绑定到当前线程和消息的队列中,开始分发数据
    // Handler有两个作用, (1) : 定时执行Message和Runnalbe 对象
    // (2): 让一个动作,在不同的线程中执行.
    // 它安排消息,用以下方法
    // post(Runnable)
    // postAtTime(Runnable,long)
    // postDelayed(Runnable,long)
    // sendEmptyMessage(int)
    // sendMessage(Message);
    // sendMessageAtTime(Message,long)
    // sendMessageDelayed(Message,long)

    // 以上方法以 post开头的允许你处理Runnable对象
    // sendMessage()允许你处理Message对象(Message里可以包含数据,)

}
