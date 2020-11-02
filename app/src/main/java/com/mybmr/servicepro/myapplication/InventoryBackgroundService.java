package com.mybmr.servicepro.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * Created by vishal on 10/8/20.
 */

public class InventoryBackgroundService extends Service {


    private boolean isRunning;
    private Thread backgroundThread;


    private final IBinder mBinder = new MyLocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyLocalBinder extends Binder {
        public InventoryBackgroundService getService() {
            return InventoryBackgroundService.this;
        }
    }

    @Override
    public void onCreate() {
        this.isRunning = false;
        Log.i("ServiceOnCreate", "isRunning:-- " + this.isRunning);

        this.backgroundThread = new Thread(myTask);
    }


    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            // do something in here

            //we're not stopping self as we want this service to be continuous
            //stopSelf();

            Log.d("MyBroadcastReceiverTwo", "Runnning");


            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Notifications Example")
                            .setContentText("This is a test notification");


            Intent notificationIntent = new Intent();

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true);
            builder.setLights(Color.BLUE, 500, 500);
            long[] pattern = {500,500,500,500,500,500,500,500,500};
            builder.setVibrate(pattern);
            builder.setStyle(new NotificationCompat.InboxStyle());
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            manager.notify(1, builder.build());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startMyOwnForeground();

        }
    };

    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.mybmr.servicepro.myapplication";
        String channelName = "My alarm demo";
        NotificationChannel chan = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLightColor(Color.BLUE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        Log.i("ServiceDestroy", "called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }

        Log.d("MyBroadcastReceiverTwo", "Success");

        return START_STICKY;
    }

}

