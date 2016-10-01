package com.dreamer.layout.view;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import com.dreamer.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class MAppWidgetProvider extends AppWidgetProvider {
	private Context context;


	/** Called when the activity is first created. */    
    
    @Override    
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,     
            int[] appWidgetIds) {     
             this.context = context;
        Timer timer = new Timer();     
        timer.scheduleAtFixedRate(new MyTime(context,appWidgetManager), 1, 60000);     
        super.onUpdate(context, appWidgetManager, appWidgetIds);     
    }     
         
         
    private class MyTime extends TimerTask{     
        RemoteViews remoteViews;     
        AppWidgetManager appWidgetManager;     
        ComponentName thisWidget;     
             
        public MyTime(Context context,AppWidgetManager appWidgetManager){     
            this.appWidgetManager = appWidgetManager;     
            remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_main); 
            thisWidget = new ComponentName(context,MAppWidgetProvider.class);     
        }     
        public void run() {     
                 
            Date date = new Date();     
            Calendar calendar = new GregorianCalendar(2012,06,11);     
            long days = (((calendar.getTimeInMillis()-date.getTime())/1000))/86400;     
            remoteViews.setTextViewText(R.id.wordcup, "距离南非世界杯还有" + days+"天"); 
            Intent intent = new Intent();
            intent.setClassName(context.getPackageName(), "com.dreamer.main.MainActivity");
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent,0);
            remoteViews.setOnClickPendingIntent(R.id.wordcup, mPendingIntent)  ; 
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);     
                 
        }     
             
    }     
}
