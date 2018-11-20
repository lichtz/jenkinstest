package com.example.licht.idcardtestss.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.licht.idcardtestss.R;

public class NotificationUtils extends ContextWrapper {
 
    private NotificationManager manager;
    public static final String id = "CGB";
    public static final String name = "广发银行";
    private static int noticeID = 910;
    private final Bitmap bitmap;

    public NotificationUtils(Context context){
        super(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.xxa);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    private NotificationManager getManager(){
        if (manager == null){
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }
    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, PendingIntent broadcast){
        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.xxa)
                .setAutoCancel(true)
                .setLargeIcon(bitmap)
                .setBadgeIconType(R.drawable.xxa)
                .setContentIntent(broadcast);
    }
    public NotificationCompat.Builder getNotification_25(String title, String content,PendingIntent broadcast){
        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.xxa)
                .setAutoCancel(true)
                .setBadgeIconType(R.drawable.xxa)
                .setContentIntent(broadcast);
    }
    public void sendNotification(String title, String content,PendingIntent broadcast){
        noticeID++;
        if (Build.VERSION.SDK_INT>=26){
            createNotificationChannel();
            Notification notification = getChannelNotification
                    (title, content,broadcast).build();
            getManager().notify(noticeID,notification);
        }else{
            Notification notification = getNotification_25(title, content,broadcast).build();
            getManager().notify(noticeID,notification);
        }
    }
}

