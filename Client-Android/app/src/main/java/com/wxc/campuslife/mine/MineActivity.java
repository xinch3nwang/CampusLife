package com.wxc.campuslife.mine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wxc.campuslife.R;
import com.wxc.campuslife.community.CommunityActivity;
import com.wxc.campuslife.community.article.Article;
import com.wxc.campuslife.community.article.ArticleDetailActivity;
import com.wxc.campuslife.community.article.ArticleInfo;
import com.wxc.campuslife.prompt.PromptActivity;
import com.wxc.campuslife.utils.AccessServerData;

import java.util.ArrayList;
import java.util.List;

public class MineActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        Button btn_prompt = findViewById(R.id.button_prompt2);
        btn_prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, PromptActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        Button btn_community = findViewById(R.id.button_community2);
        btn_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, CommunityActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        String userid = preferences.getString("id","");
        String nickname = preferences.getString("nickname","");
        String username = preferences.getString("username","");
        if(nickname.equals("")){
            nickname = username;
        }
        TextView tv_username = findViewById(R.id.textview_mine_name);
        tv_username.setText(nickname);

        TextView tv_follow = findViewById(R.id.textView_mine_follow);
        tv_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = new AccessServerData().followUsers(userid);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = null;
                                String[] items = {"生活分享", "失物招领", "问答平台", "随机"};
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MineActivity.this);
                                alertBuilder.setTitle("我的关注");
                                alertBuilder.setMessage(data);
                //                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                //                    @Override
                //                    public void onClick(DialogInterface dialogInterface, int i) {
                //                        Toast.makeText(MineActivity.this, items[i], Toast.LENGTH_SHORT).show();
                //                    }
                //                });
                                alertDialog = alertBuilder.create();
                                alertDialog.show();
                            }
                        });
                    }
                }).start();

            }
        });

        TextView tv_fans = findViewById(R.id.textView_mine_fans);
        tv_fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = new AccessServerData().fans(userid);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = null;
                                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MineActivity.this);
                                alertBuilder.setTitle("我的粉丝");
                                alertBuilder.setMessage(data);
                                alertDialog = alertBuilder.create();
                                alertDialog.show();
                            }
                        });
                    }
                }).start();
            }
        });

        TextView tv_article = findViewById(R.id.textView_mine_article);
        tv_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Article> articleList = new ArrayList<Article>();
                List<String> list = new ArrayList<>();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
                        String userid = preferences.getString("id","");
                        ArticleInfo articleInfo = new AccessServerData().getArticleByAuthor(userid);

                        int count = articleInfo.getCount();
                        for(int i = 0; i < count; i++) {
                            String title = articleInfo.getResult()[i].getFields().getTitle();
                            list.add(title);
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
                        AlertDialog alertDialog = null;
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MineActivity.this);
                        alertBuilder.setTitle("我的发文");
                        String[] items= new String[list.size()];
                        list.toArray(items);
                        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(MineActivity.this, items[i], Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(view.getContext(), ArticleDetailActivity.class);
                                intent.putExtra("article", articleList.get(i));
                                view.getContext().startActivity(intent);
                            }
                        });
                        alertDialog = alertBuilder.create();
                        alertDialog.show();
                    }
                },300);
            }
        });

        TextView tv_update = findViewById(R.id.textView_mine_update);
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, AccountDetailActivity.class);
                startActivity(intent);
            }
        });


    }
}