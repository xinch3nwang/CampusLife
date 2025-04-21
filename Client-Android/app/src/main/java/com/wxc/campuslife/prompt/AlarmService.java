package com.wxc.campuslife.prompt;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.wxc.campuslife.R;

public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run");
                Intent intent = new Intent(getApplicationContext(), PromptActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                NotificationChannel notificationChannel = null;
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String channelId = "1";
                String channelName = "待办";
                manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), "1")
                        .setContentTitle("提醒")
                        .setContentText("你有待办事项待完成")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentIntent(pi)
                        //.setSound(Uri.fromFile(new File("")))
                        .setVibrate(new long[]{0, 1000, 1000, 1000})
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build();
                manager.notify(1, notification);
            }
        }).start();
        //AlarmManagerUtils.getInstance(getApplicationContext()).getUpAlarmManagerWorkOnOthers();
        return super.onStartCommand(intent, flags, startId);
    }
}