package com.wxc.campuslife.community.catalog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.wxc.campuslife.R;
import com.wxc.campuslife.community.article.Article;
import com.wxc.campuslife.community.article.ArticleAdapter;
import com.wxc.campuslife.community.article.ArticleInfo;
import com.wxc.campuslife.community.article.PublishActivity;
import com.wxc.campuslife.utils.AccessServerData;

import java.util.ArrayList;
import java.util.List;

public class RandomActivity extends AppCompatActivity {

    private List<Article> articleList = new ArrayList<Article>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        Intent intent = getIntent();
        String tag = intent.getStringExtra("catalog");
        showArticles(tag);

        Button btn_tag = findViewById(R.id.button_tag);
        btn_tag.setText(tag);

        Button btn_random = findViewById(R.id.button_random);
        btn_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RandomActivity.this, PublishActivity.class);
                intent.putExtra("catalog", tag);
                startActivity(intent);
            }
        });

    }

    private void showArticles(String tag) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArticleInfo articleInfo = new AccessServerData().getArticleByTag(tag);

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
            }
        }).start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_random);
                LinearLayoutManager layoutManager = new LinearLayoutManager(RandomActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                ArticleAdapter adapter = new ArticleAdapter(articleList);
                recyclerView.setAdapter(adapter);
            }
        },300);

    }
}