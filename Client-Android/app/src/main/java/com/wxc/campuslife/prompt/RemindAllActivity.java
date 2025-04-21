package com.wxc.campuslife.prompt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.dbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemindAllActivity extends AppCompatActivity {

    private List<Remind> RemindList = new ArrayList<Remind>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_all);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_remindall);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RemindAllAdapter adapter = new RemindAllAdapter(RemindList);
        recyclerView.setAdapter(adapter);

        adapter.showRemind();
    }

//    private void showRemind() {
//        dbHelper remindDB = new dbHelper(this);
//        SQLiteDatabase db = remindDB.getWritableDatabase();
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_remindall);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        RemindAllAdapter adapter = new RemindAllAdapter(RemindList);
//        recyclerView.setAdapter(adapter);
//
//        try {
//            String title = "";
//            String detail = "";
//            String time = "";
//            int isfin;
//            Cursor c = db.rawQuery("select * from record order by year,month,day,hour,min",null);
//            //如果成功则显示
//            if(c.getCount() > 0) {
//                while (c.moveToNext()){
//                    isfin = c.getInt(9);
//                    title = c.getString(7);
//                    detail = c.getString(8);
//                    time = c.getString(1)+"-"+c.getString(2)+"-"+c.getString(3)+" "+c.getString(4)+":"+c.getString(5)+":"+c.getString(6);
//                    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                    Date date = fmt.parse(time);
//                    if(isfin==1)
//                        title+="(已完成)";
//                    else
//                        title+="(未完成)";
//                    Remind item = new Remind(title,detail,date);
//                    RemindList.add(item);
//                }
//            }
//            //Log.d("datebase","find all ok");
//        } catch (Exception e) {
//            Toast.makeText(RemindAllActivity.this, e+"", Toast.LENGTH_LONG).show();
//        }
//    }


}