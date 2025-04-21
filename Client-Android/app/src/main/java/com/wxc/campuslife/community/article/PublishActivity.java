package com.wxc.campuslife.community.article;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxc.campuslife.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity {

    Handler handler = new Handler();
    SharedPreferences preferences;
    String title;
    String content;
    String tag;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        Intent intent =getIntent();
        String catalog = intent.getStringExtra("catalog");

        EditText et_title = findViewById(R.id.editTextArticleTitle);
        EditText et_content = findViewById(R.id.editTextArticleContent);
        EditText et_tag = findViewById(R.id.editTextArticleTag);
        et_tag.setText(catalog);
        Button btn_publishnew = findViewById(R.id.button_publishnew);
        btn_publishnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString();
                content = et_content.getText().toString();
                tag = et_tag.getText().toString();
                preferences = getSharedPreferences("account", MODE_PRIVATE);
                id = preferences.getString("id", "");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("title", title)
                                .add("content", content)
                                .add("usr_id", id)
                                .add("tagname",tag)
                                .build();
                        Request postRequest = new Request.Builder()
                                .url(getResources().getString(R.string.url_usb_phone)+"article/article_publish/")
                                .post(requestBody)
                                .build();

                        try {
                            Response response = client.newCall(postRequest).execute();
                            String r = response.body().string();
                            if(r.equals("ok")){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishActivity.this, "已发布", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}