package com.saki.client;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import com.ayzf.anwind.R;
import com.saki.ui.PagerActivity;

public class ServiceNotification {
    public static final int ID = 3028;
    NotificationManager notificationManager;
    Notification notification;
    PendingIntent mainIntent;
    Service context;

    public ServiceNotification(Service context) {
        this.context = context;
        Intent i = new Intent(context, PagerActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mainIntent = PendingIntent.getActivity(context, 0, i, 0);
        notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
    }

    private Notification createNotification(String text){
        Notification.Builder builder = null;
        //------------------------------------
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = null;
            String CHANNEL_ONE_ID = "setqq.com.saki.com";
            String CHANNEL_ONE_NAME = context.getString(R.string.app_name);
            builder = new Notification.Builder(context,CHANNEL_ONE_ID);
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(null,null);
            notificationManager.createNotificationChannel(notificationChannel);
        }else{
            builder = new Notification.Builder(context);
        }
        //-----------------------------------
        builder .setContentIntent(mainIntent);
        builder .setSmallIcon(R.mipmap.ic_launcher_round);
        builder .setContentTitle(context.getResources().getString(R.string.app_name));
        notification = builder .setContentText(text).build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        return notification;
    }
    public void pushMessage(String text){
        createNotification(text);
        notificationManager.notify(ID,notification);
    }

    public void startForeground() {
        createNotification("已启动");
        context.startForeground(ID, notification);
    }

    public void stopForeground() {
        context.stopForeground(true);
    }
}
