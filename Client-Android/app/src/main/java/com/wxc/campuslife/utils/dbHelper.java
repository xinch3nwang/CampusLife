package com.wxc.campuslife.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {
    public dbHelper(Context context){
        super(context, "record.db", null,1);  //建立或打开库
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table record (" +
                "create_time TIMESTAMP DEFAULT current_timestamp primary key," +
                "year int," +
                "month int," +
                "day int," +
                "hour int," +
                "min int," +
                "sec int DEFAULT 0," +
                "title text," +
                "detail text," +
                "isfin int DEFAULT 0);";
        db.execSQL(sql);
        Log.d("database","create ok");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS record");
        onCreate(db);
    }

}
