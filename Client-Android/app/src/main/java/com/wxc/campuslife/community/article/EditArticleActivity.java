package com.wxc.campuslife.community.article;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.AccessServerData;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditArticleActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String title;
    String content;
    String tag;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        Intent intent =getIntent();
        content = intent.getStringExtra("content");
        title = intent.getStringExtra("title");
        tag = intent.getStringExtra("tag");
        String articleid = intent.getStringExtra("articleid");

        EditText et_title = findViewById(R.id.editTextArticleTitleUp);
        EditText et_content = findViewById(R.id.editTextArticleContentUp);
        EditText et_tag = findViewById(R.id.editTextArticleTagUp);
        et_title.setText(title);
        et_content.setText(content);
        et_tag.setText(tag);

        Button btn_publishUp = findViewById(R.id.button_publishUp);
        btn_publishUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = et_title.getText().toString();
                content = et_content.getText().toString();
                tag = et_tag.getText().toString();

                preferences = getSharedPreferences("account", MODE_PRIVATE);
                id = preferences.getString("id", "");
                Toast.makeText(EditArticleActivity.this, "已修改", Toast.LENGTH_SHORT).show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AccessServerData().updateArticle(title, content, tag, articleid);
                    }
                }).start();
            }
        });

        Button btn_del = findViewById(R.id.button_deleteArticle);
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new AccessServerData().delArticle(articleid);
                    }
                }).start();
                Toast.makeText(EditArticleActivity.this, "已删除", Toast.LENGTH_SHORT).show();
            }
        });
    }
}