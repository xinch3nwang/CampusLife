package com.wxc.campuslife.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wxc.campuslife.R;
import com.wxc.campuslife.prompt.PromptActivity;
import com.wxc.campuslife.utils.AccessServerData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //之前登录可直接登录
        if(getAccountState()){
            Intent intent = new Intent(LoginActivity.this, PromptActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        EditText username = findViewById(R.id.editText_username);
        EditText password = findViewById(R.id.editText_password);


        //login
        Button btn_login = findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户输入的用户名和密码
                String user = username.getText().toString();
                String pass = password.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int code = 0;
                        String id = "";

                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("username", user)
                                .add("password", pass)
                                .build();
                        Request postRequest = new Request.Builder()
                                .url(new AccessServerData().getUrl() + "account/login/")
                                .post(requestBody)
                                .build();

                        try {
                            Response response = client.newCall(postRequest).execute();
                            String r = response.body().string();
                            Log.d("response",r);
                            code = getCode(r);
                            id = getId(r);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 验证用户名和密码是否正确（可switch状态码确定具体情况）
                        switch (code) {
                            case 1:
                                // 登录成功，跳转到主界面
                                //记住密码（存储信息到SharedPreferences）
                                remeberAccount(user, pass, id);
                                Log.d("id","--------------"+id);
                                Intent intent = new Intent(LoginActivity.this, PromptActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case 2:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            case 3:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "用户未注册", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            default:
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        }
                    }
                }).start();

            }
        });


        //signup
        Button btn_signup = findViewById(R.id.button_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取用户输入的用户名和密码
                String user = username.getText().toString();
                String pass = password.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        int code = 0;
                        String id = "";

                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("username", user)
                                .add("password", pass)
                                .add("nickname", user)
                                .build();
                        Request postRequest = new Request.Builder()
                                .url(new AccessServerData().getUrl() +"account/signup/")
                                .post(requestBody)
                                .build();

                        try {
                            Response response = client.newCall(postRequest).execute();
                            String r = response.body().string();
                            Log.d("response",r);
                            code = getCode(r);
                            id = getId(r);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        switch (code){
                            case 1:
                                //Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                //存储信息
                                remeberAccount(user, pass, id);

                                Intent intent = new Intent(LoginActivity.this, PromptActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            default:
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, "错误", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                        }
                    }
                }).start();

            }
        });

    }


//    private int register(String user, String pass) {
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("username", user)
//                .add("password", pass)
//                .add("nickname", user)
//                .build();
//        Request postRequest = new Request.Builder()
//                .url("http://114.213.64.171:8000/account/signup/")
//                .post(requestBody)
//                .build();
//
//        try {
//            Response response = client.newCall(postRequest).execute();
//            String r = response.body().string();
//            System.out.println(r);
//            if(getCode(r)==200)
//                return 1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 2;
//    }

//    //用json格式传给服务器，返回状态码：OK1 WRONG0 NE-1
//    private int verify(String user, String pass) {
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder()
//                .add("username", user)
//                .add("password", pass)
//                .build();
//        Request postRequest = new Request.Builder()
//                .url("http://114.213.64.171:8000/account/login/")
//                .post(requestBody)
//                .build();
//
//        try {
//            Response response = client.newCall(postRequest).execute();
//            String r = response.body().string();
//            if(getCode(r)==200)
//                return 1;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }


    private void remeberAccount(String user, String pass, String id) {
        SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
        editor.putInt("status", 1);
        editor.putString("username", user);
        editor.putString("password", pass);
        editor.putString("id", id);
        editor.apply();

//        Looper.prepare();
//        Toast.makeText(LoginActivity.this, "已保存", Toast.LENGTH_SHORT).show();
//        Looper.loop();
    }


    //判断是否之前已登陆
    public boolean getAccountState() {
//        Looper.prepare();
//        Toast.makeText(LoginActivity.this, "正在查询状态", Toast.LENGTH_SHORT).show();
//        Looper.loop();

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        if(preferences.getInt("status",0 )==1)
            return true;
        return false;
    }


    private int getCode(String s) {
        int code = 0;
        try {
            JSONObject resultObject = new JSONObject(s);
            code = resultObject.getInt("code");
        }
         catch (JSONException e){
            Log.d("TAG", "JSONException:" + e.getMessage());
        }
        return code;
    }

    private String getId(String s) {
        String id = "";
        try {
            JSONObject resultObject = new JSONObject(s);
            id = resultObject.getString("id");
            Log.d("id", "+++++++++++++++"+id);
        }
        catch (JSONException e){
            Log.d("TAG", "JSONException:" + e.getMessage());
        }
        return id;
    }

}