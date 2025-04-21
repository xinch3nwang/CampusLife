package com.wxc.campuslife.prompt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.gson.Gson;
import com.wxc.campuslife.R;
import com.wxc.campuslife.community.CommunityActivity;
import com.wxc.campuslife.mine.MineActivity;
import com.wxc.campuslife.utils.MyApplication;
import com.wxc.campuslife.utils.dbHelper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PromptActivity extends AppCompatActivity {

    private List<Remind> RemindList = new ArrayList<Remind>();
    private final Gson gson = new Gson();
    String detail = null;
    String title = null;
    int year;
    int month;
    int day;
    int hour;
    int min;

    @Override
    protected void onResume() {
        super.onResume();
        showRemind();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);
        //getSupportActionBar().hide();

        showWeather();
        showRemind();

        Button btn_allremind = findViewById(R.id.button_more);
        btn_allremind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PromptActivity.this , RemindAllActivity.class);
                startActivity(intent);
            }
        });

        Button btn_community = findViewById(R.id.button_community);
        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PromptActivity.this , CommunityActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Button btn_mine = findViewById(R.id.button_mine);
        btn_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PromptActivity.this , MineActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    private void toAdd(RemindAdapter remindAdapter) {
        Button btn_add = (Button) findViewById(R.id.button_addtodo);
        EditText et_add = (EditText) findViewById((R.id.editText_addtodo));

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_add.getText().toString();
                et_add.setText("");

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(PromptActivity.this,
                        null,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        year = datePickerDialog.getDatePicker().getYear();
                        month = datePickerDialog.getDatePicker().getMonth() + 1;
                        day = datePickerDialog.getDatePicker().getDayOfMonth();
                        Toast.makeText(PromptActivity.this,""+year+"."+month+"."+day,Toast.LENGTH_SHORT).show();
                        datePickerDialog.dismiss();
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                PromptActivity.this,
                                new TimePickerDialog.OnTimeSetListener(){
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        hour = hourOfDay;
                                        min = minute;
                                        Toast.makeText(PromptActivity.this,""+hour+":"+min,Toast.LENGTH_SHORT).show();

                                        final EditText editText = new EditText(PromptActivity.this);
                                        editText.setSingleLine();
                                        editText.requestFocus();
                                        editText.setFocusable(true);
                                        AlertDialog.Builder inputDialog = new AlertDialog.Builder(PromptActivity.this);
                                        inputDialog.setTitle("输入详细信息");
                                        inputDialog.setView(editText);
                                        inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                detail = editText.getText().toString();

                                                //addtoui
                                                SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String time = year+"-"+month+"-"+day+" "+hour+":"+min+":"+"0";
                                                Date timetodo = null;
                                                try {
                                                    timetodo = ft.parse(time);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                Remind v = new Remind(title,detail, timetodo);
                                                RemindList.add(v);
                                                //addtosql
                                                try{
        //                                            Date dNow = new Date( );
        //                                            SimpleDateFormat fd = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        //                                            String date = fd.format(dNow);
                                                    //添加进数据库
                                                    String sql="insert into record(year,month,day,hour,min,title,detail) " +
                                                            "values("+year+","+month+","+day+","+hour+","+min+",'"+title+"','"+detail+"');";
                                                    dbHelper myhelper = new dbHelper(MyApplication.getContext());
                                                    SQLiteDatabase db = myhelper.getWritableDatabase();
                                                    db.execSQL(sql);

                                                    //Toast.makeText(PromptActivity.this,"记录已保存",Toast.LENGTH_LONG).show();
                                                    initReminds(remindAdapter);
                                                } catch (Exception e){
                                                    //插入失败
                                                    Toast.makeText(PromptActivity.this,detail,Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        inputDialog.show();

                                    }
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true);
                        timePickerDialog.show();

//                        timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
////                                hour = calendar.get(Calendar.HOUR_OF_DAY);
////                                min = calendar.get(Calendar.MINUTE);
//                                Toast.makeText(PromptActivity.this,""+hour+":"+min,Toast.LENGTH_SHORT).show();
//                                timePickerDialog.dismiss();
//
//                                final EditText editText = new EditText(PromptActivity.this);
//                                editText.setSingleLine();
//                                editText.requestFocus();
//                                editText.setFocusable(true);
//                                AlertDialog.Builder inputDialog = new AlertDialog.Builder(PromptActivity.this);
//                                inputDialog.setTitle("输入详细信息");
//                                inputDialog.setView(editText);
//                                inputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        detail = editText.getText().toString();
//
//                                        //addtoui
//                                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        String time = year+"-"+month+"-"+day+" "+hour+":"+min+":"+"0";
//                                        Date timetodo = null;
//                                        try {
//                                            timetodo = ft.parse(time);
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Remind v = new Remind(title,detail, timetodo);
//                                        RemindList.add(v);
//                                        //addtosql
//                                        try{
////                                            Date dNow = new Date( );
////                                            SimpleDateFormat fd = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
////                                            String date = fd.format(dNow);
//                                            //添加进数据库
//                                            String sql="insert into record(year,month,day,hour,min,title,detail) " +
//                                                    "values("+year+","+month+","+day+","+hour+","+min+",'"+title+"','"+detail+"');";
//                                            dbHelper myhelper = new dbHelper(MyApplication.getContext());
//                                            SQLiteDatabase db = myhelper.getWritableDatabase();
//                                            db.execSQL(sql);
//                                            //给用户提示添加成功
//                                            Toast.makeText(PromptActivity.this,"记录已保存",Toast.LENGTH_LONG).show();
//                                            initReminds(remindAdapter);
//                                        } catch (Exception e){
//                                            //插入失败
//                                            Toast.makeText(PromptActivity.this,detail,Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                                inputDialog.show();
//                            }
//                        });
                    }
                });


//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("d");
//                dialog.setMessage("e");
//                dialog.setCancelable(false);
//                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        image.setImageResource(R.drawable.ic_launcher_foreground);
//                    }
//
//                });
//                dialog.show();
            }
        });

//        btn_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String title = et_add.getText().toString();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(PromptActivity.this);
//                dialog.setTitle("d");
//                dialog.setMessage("e");
//                dialog.setCancelable(false);
//                dialog.setPositiveButton("添加", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                dialog.show();
//            }
//        });
    }

    private void showRemind() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RemindAdapter adapter = new RemindAdapter(RemindList);
        recyclerView.setAdapter(adapter);

        initReminds(adapter);
        toAdd(adapter);
    }

    private void initReminds(RemindAdapter remindAdapter) {
        dbHelper remindDB = new dbHelper(this);
        SQLiteDatabase db = remindDB.getWritableDatabase();

        RemindList.clear();
        remindAdapter.notifyDataSetChanged();

        try {
            String title = "";
            String detail = "";
            String time = "";
            Cursor c = db.rawQuery("select * from record where isfin=0 order by year,month,day,hour,min",null);
            if(c.getCount() > 0) {
                while (c.moveToNext()){
                    title = c.getString(7);
                    detail = c.getString(8);
                    time = c.getString(1)+"-"+c.getString(2)+"-"+c.getString(3)+" "+c.getString(4)+":"+c.getString(5)+":"+c.getString(6);
                    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = fmt.parse(time);
                    Remind item = new Remind(title,detail,date);
                    RemindList.add(item);
                }
            }
            //Log.d("datebase","find all ok");
        } catch (Exception e) {
            Toast.makeText(PromptActivity.this, e+"", Toast.LENGTH_LONG).show();
        }
    }



    /*
    *天气相关
    */
    private void showWeather() {
        //https://devapi.qweather.com/v7/weather/now?location=101010100&key=a2532ea459a74f10a3b3222284eac9d9
        TextView tv_weather = (TextView) findViewById(R.id.textView_weather);
        final String[] link = {null};

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request
                            .Builder()
                            .url("https://devapi.qweather.com/v7/weather/now?location=101190501&key=a2532ea459a74f10a3b3222284eac9d9")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responsedata = response.body().string();
                    Log.d("response", responsedata);
                    tv_weather.setText(parseJson(responsedata, 0));
                    link[0] = parseJson(responsedata, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        tv_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final WebView web = new WebView(PromptActivity.this);
                web.requestFocus();
                web.setFocusable(true);
                web.setWebViewClient(new WebViewClient());
                web.loadUrl(link[0]);
                AlertDialog.Builder webDialog = new AlertDialog.Builder(PromptActivity.this);
                webDialog.setView(web);
                webDialog.show();

//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link[0]));
//                startActivity(intent);
//                Toast.makeText(PromptActivity.this,link[0],Toast.LENGTH_SHORT).show();
            }
        });
        //new HttpTask().execute();
    }

    private String parseJson(String JsonData, int mode) {
        Gson gson = new Gson();
        WeatherInfo userInfo = gson.fromJson(JsonData, WeatherInfo.class);
        String code = userInfo.getCode();
        if(mode == 0){
            String Text = userInfo.getNow().getText();
            String Temp = userInfo.getNow().getFeelsLik();
            return (Text+"                                        "+Temp+"°C");
        }
        else{
            String url = null;
            url = userInfo.getFxLink();
            return url;
        }
    }





//    class HttpTask extends AsyncTask<Void,Void,String> {
//
//        @Override
//        protected String doInBackground(Void... voids) {  //耗时操作代码在后台进行
//            String url = "https://geoapi.qweather.com/v2/city/lookup?key=a2532ea459a74f10a3b3222284eac9d9&location=%E5%8D%97%E9%80%9A"; //域名字符串
//            OkHttpClient okHttpClient = new OkHttpClient();
//            Request request = new Request.Builder().url(url).get().build();
//            Response response = null;
//            try {
//                response = okHttpClient.newCall(request).execute();
//                String str = response.body().string();
//                return parseJSONWithJSONObject(str);
//            } catch (IOException e) {
//                return null;
//            }
//
//        }
//
//        private String parseJSONWithJSONObject(String str) {
////            JSONArray jsonArray = null;
////            try {
////                jsonArray = new JSONArray(str);
////                for (int i=0; i<jsonArray.length(); i++) {
////                    JSONObject jsonObject = jsonArray.getJSONObject(i);
////                    return jsonObject.getString("code");
////                }
////            } catch (JSONException e) {
////                return str;
////            }
//
//            Gson gson = new Gson();
//            //Temp temp = gson.fromJson(str, Temp.class);
//            return str;
//        }
//
//        @Override
//        protected void onPostExecute(String resultData) {  //在后台数据提交后更新UI主线程
//            TextView tv_HttpData = (TextView) findViewById(R.id.textView_weather);
//            if (resultData != null)
//                tv_HttpData.setText(resultData);  //原生用法，更新UI工作在onPostExecute()方法里
//            else
//                tv_HttpData.setText("Sorry,the content is null");
//        }
//    }
}