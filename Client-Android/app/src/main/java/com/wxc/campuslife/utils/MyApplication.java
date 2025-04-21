package com.wxc.campuslife.utils;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //调用getApplicationContext()得到程序级别的context
        context = getApplicationContext();
    }
    //创建getContext方法，返回获取的程序级别的context
    public static Context getContext(){
        return context;
    }
}
