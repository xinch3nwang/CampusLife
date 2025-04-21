package com.wxc.campuslife.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.wxc.campuslife.prompt.AlarmService;
import com.wxc.campuslife.prompt.Remind;

import java.util.Calendar;

public class AlarmManagerUtils {

    private static final long TIME_INTERVAL = 10 * 1000;//闹钟执行任务的时间间隔
    private Context context;
    public static AlarmManager am;
    public static PendingIntent pendingIntent;

    private Calendar calendar;

    private AlarmManagerUtils(Context aContext) {
        this.context = aContext;
    }

    private static AlarmManagerUtils instance = null;

    public static AlarmManagerUtils getInstance(Context aContext) {
        if (instance == null) {
            synchronized (AlarmManagerUtils.class) {
                if (instance == null) {
                    instance = new AlarmManagerUtils(aContext);
                }
            }
        }
        return instance;
    }

    public void createAlarmManager() {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    public void createAlarmManagerNow() {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
    }

    @SuppressLint("NewApi")
    public void alarmManagerStartWork(int h, int m) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,h);
        calendar.set(Calendar.MINUTE,m);
        calendar.set(Calendar.SECOND,0);

//        Toast.makeText(MyApplication.getContext(), "已设置 " , Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
        }
    }

//    @SuppressLint("NewApi")
//    public void AlarmManagerWorkOnOthers() {
//        //高版本重复设置闹钟达到低版本中setRepeating相同效果
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {// 6.0及以上
//            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                    System.currentTimeMillis() + TIME_INTERVAL, pendingIntent);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4及以上
//            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                    + TIME_INTERVAL, pendingIntent);
//        }
//    }
}
