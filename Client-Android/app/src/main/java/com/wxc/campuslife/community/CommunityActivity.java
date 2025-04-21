package com.wxc.campuslife.community;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wxc.campuslife.R;
import com.wxc.campuslife.community.news.NewsActivity;
import com.wxc.campuslife.mine.MineActivity;
import com.wxc.campuslife.prompt.PromptActivity;

public class CommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Button btn_prompt = findViewById(R.id.button_prompt1);
        btn_prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, PromptActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Button btn_mine = findViewById(R.id.button_mine1);
        btn_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, MineActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Button btn_news = findViewById(R.id.button_news);
        btn_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        Button btn_forum = findViewById(R.id.button_forum);
        btn_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });

        Button btn_contact = findViewById(R.id.button_contact);
        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = null;
                final String[] catalog = {"default"};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(CommunityActivity.this);
                alertBuilder.setTitle("即时通讯");
                final String[] items = {"公共聊天", "个人讯息"};
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent;
                        switch (i) {
                            case 0:
                                intent = new Intent(CommunityActivity.this, ContactActivity.class);
                                intent.putExtra("mode", "public");
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(CommunityActivity.this, ContactActivity.class);
                                intent.putExtra("mode", "private");
                                String contacter = "";
                                intent.putExtra("to", contacter);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                alertDialog = alertBuilder.create();
                alertDialog.show();

            }
        });
    }
}