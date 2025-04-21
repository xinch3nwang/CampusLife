package com.wxc.campuslife.community;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wxc.campuslife.R;
import com.wxc.campuslife.community.article.Article;
import com.wxc.campuslife.community.article.ArticleInfo;
import com.wxc.campuslife.community.article.PublishActivity;
import com.wxc.campuslife.community.article.ArticleAdapter;
import com.wxc.campuslife.community.catalog.LifeActivity;
import com.wxc.campuslife.community.catalog.LostActivity;
import com.wxc.campuslife.community.catalog.QuestionActivity;
import com.wxc.campuslife.community.catalog.RandomActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumActivity extends AppCompatActivity {

    private List<Article> articleList = new ArrayList<Article>();
    private Handler handler = new Handler();
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        showArticles();

//        Button btn_hall = findViewById(R.id.button_hall);
//        btn_hall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onRestart();
//            }
//        });

        Button button_cata = findViewById(R.id.button_category);
        button_cata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = null;
                final String[] items = {"生活分享", "失物招领", "问答平台", "搜索分类"};
                final String[] catalog = {"default"};
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ForumActivity.this);
                alertBuilder.setTitle("分类");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ForumActivity.this, items[i], Toast.LENGTH_SHORT).show();
                        final Intent[] intent = new Intent[1];
                        switch (i){
                            case 0:
                                intent[0] = new Intent(ForumActivity.this, LifeActivity.class);
                                startActivity(intent[0]);
                                break;
                            case 1:
                                intent[0] = new Intent(ForumActivity.this, LostActivity.class);
                                startActivity(intent[0]);
                                break;
                            case 2:
                                intent[0] = new Intent(ForumActivity.this, QuestionActivity.class);
                                startActivity(intent[0]);
                                break;
                            case 3:

                                EditText et_search = new EditText(getApplicationContext());
                                AlertDialog searchDialog = null;
                                AlertDialog.Builder searchBuilder = new AlertDialog.Builder(ForumActivity.this);
                                searchBuilder.setTitle("输入tag:");
                                searchBuilder.setView(et_search);
                                searchBuilder.setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        catalog[0] = et_search.getText().toString();
                                        intent[0] = new Intent(ForumActivity.this, RandomActivity.class);
                                        intent[0].putExtra("catalog", catalog[0]);
                                        startActivity(intent[0]);
                                    }
                                });
                                searchDialog = searchBuilder.create();
                                searchDialog.show();

                        }

                    }


                });
                alertDialog = alertBuilder.create();
                alertDialog.show();
            }
        });

        Button button_pub = findViewById(R.id.button_publish);
        button_pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForumActivity.this, PublishActivity.class);
                intent.putExtra("catalog", "");
                startActivity(intent);
            }
        });


    }

    private void showArticles() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request getRequest = new Request
                        .Builder()
                        .url(getResources().getString(R.string.url_usb_phone) + "article/articles_today/")
                        .build();
                try {
                    Response response = client.newCall(getRequest).execute();
                    String r = response.body().string();
                    Log.d("response",r);
                    Gson gson = new Gson();
                    ArticleInfo articleInfo = gson.fromJson(r, ArticleInfo.class);

                    int count = articleInfo.getCount();
                    for(int i = 0; i < count; i++) {
                        String title = articleInfo.getResult()[i].getFields().getTitle();
                        String tag = articleInfo.getResult()[i].getFields().getTag_name();
                        String content = articleInfo.getResult()[i].getFields().getContent();
                        String time = articleInfo.getResult()[i].getFields().getPub_time().substring(0,19).replace('T',' ');
                        String article_id = articleInfo.getResult()[i].getPk();
                        String author = articleInfo.getResult()[i].getFields().getAuthor();
                        int readed_count = articleInfo.getResult()[i].getFields().getReaded_count();
                        int liked_count = articleInfo.getResult()[i].getFields().getLiked_count();
                        int collected_count = articleInfo.getResult()[i].getFields().getCollected_count();

                        Article article = new Article(title, tag, content, author+":", time,
                                article_id, readed_count, liked_count, collected_count);
                        articleList.add(article);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_forum);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ForumActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                ArticleAdapter adapter = new ArticleAdapter(articleList);
                recyclerView.setAdapter(adapter);
            }
        },300);

    }

}