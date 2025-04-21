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

public class QuestionActivity extends AppCompatActivity {

    private List<Article> articleList = new ArrayList<Article>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        showArticles();

        Button btn_question = findViewById(R.id.button_quz);
        btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this, PublishActivity.class);
                intent.putExtra("catalog", "问答平台");
                startActivity(intent);
            }
        });

    }

    private void showArticles() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                    ArticleInfo articleInfo = new AccessServerData().getArticleByTag("问答平台");

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
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_quz);
                LinearLayoutManager layoutManager = new LinearLayoutManager(QuestionActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                ArticleAdapter adapter = new ArticleAdapter(articleList);
                recyclerView.setAdapter(adapter);
            }
        },300);

    }
}