package com.wxc.campuslife.community.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wxc.campuslife.R;
import com.wxc.campuslife.community.article.comment.Comment;
import com.wxc.campuslife.community.article.comment.CommentAdapter;
import com.wxc.campuslife.community.article.comment.CommentInfo;
import com.wxc.campuslife.mine.AccountDetailActivity;
import com.wxc.campuslife.utils.AccessServerData;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {

    Handler handler = new Handler();
    String r;
    String author;
    List<Comment> commentList = new ArrayList<Comment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        Button btn_readed = findViewById(R.id.button_readed);
        Button btn_liked = findViewById(R.id.button_liked);
        Button btn_collected = findViewById(R.id.button_collected);
        TextView tv_author = findViewById(R.id.textView_dauthor);
        TextView tv_date = findViewById(R.id.textView_ddate);
        TextView tv_title = findViewById(R.id.textView_dtitle);
        TextView tv_content = findViewById(R.id.textView_dcontent);
        Button btn_follow = findViewById(R.id.button_follow);
        EditText et_comment = findViewById(R.id.editTextComment);
        Button btn_comment = findViewById(R.id.button_comment);

        Intent intent =getIntent();
        Article article = (Article) intent.getSerializableExtra("article");
        Log.d("serial", article.getAuthor());
        String article_id = article.getArticleid();
        author = article.getAuthor();
        String date = article.getDate();
        String title = article.getTitle();
        String content = article.getContent();
        int readedcount = article.getReaded_count();
        int likedcount  = article.getLiked_count();
        int collectedcount = article.getCollected_count();

        author = author.substring(0,author.length()-1);
        tv_author.setText(author);
        tv_date.setText(date+" ");
        tv_title.setText(title);
        tv_content.setText(content);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String count = new AccessServerData().getArticleRead(article_id);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        btn_readed.setText("阅读数：" + count);
                        Log.d("data","+++++"+count);
                    }
                });
            }
        }).start();

        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        String user_id = preferences.getString("id","");
        String nickname = preferences.getString("nickname","");
        String username = preferences.getString("username","");
        //Log.i("data",nickname);

        if(author.equals(nickname)||author.equals(username)){
            btn_follow.setText("编辑");
            //Toast.makeText(ArticleDetailActivity.this, nickname, Toast.LENGTH_SHORT).show();
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ArticleDetailActivity.this, EditArticleActivity.class);
                    intent.putExtra("tag", article.getTag());
                    intent.putExtra("title", title);
                    intent.putExtra("content", content);
                    intent.putExtra("articleid", article_id);
                    startActivity(intent);
                }
            });
        }
        else {
            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            r = new AccessServerData().userFollow(author);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (r.equals("f"))
                                        Toast.makeText(ArticleDetailActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                                    else if (r.equals("un"))
                                        Toast.makeText(ArticleDetailActivity.this, "取关成功", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(ArticleDetailActivity.this, r, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }
            });
        }


        showComments(article_id);


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                String comment = et_comment.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = new AccessServerData().publishTopComment(article_id,comment,user_id);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(result.equals("pub")) {
                                    et_comment.setText("");
                                    Toast.makeText(ArticleDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                    commentList.clear();
                                    showComments(article_id);
                                }
                            }
                        });
                    }
                }).start();


            }
        });

        tv_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticleDetailActivity.this, AccountDetailActivity.class);
                intent.putExtra("author", author);
                startActivity(intent);
            }
        });


        btn_collected.setText("收藏数：" + collectedcount);
        btn_liked.setText("点赞数：" + likedcount);

        btn_liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String count = new AccessServerData().likeArticle(article_id, user_id);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_liked.setText("点赞数：" + count);
                            }
                        });
                    }
                }).start();
            }
        });

        btn_collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String count = new AccessServerData().collectArticle(article_id, user_id);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btn_collected.setText("收藏数：" + count);
                            }
                        });
                    }
                }).start();
            }
        });

    }



    private void showComments(String articleid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentInfo commentInfo = new AccessServerData().getCommentByArticle(articleid);
                int count = commentInfo.getCount();
                for(int i = 0; i < count; i++) {
                    String id = commentInfo.getResult()[i].getId();
                    String create_time = commentInfo.getResult()[i].getCreate_time().substring(0,19).replace('T',' ');
                    String content = commentInfo.getResult()[i].getContent();
                    String article = commentInfo.getResult()[i].getArticle();
                    String username = commentInfo.getResult()[i].getNickname();
                    String parent = commentInfo.getResult()[i].getParent();
                    String userid = commentInfo.getResult()[i].getUser();
                    Comment comment = new Comment(id,create_time,content,article,username,userid,parent);
                    commentList.add(comment);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_comment);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ArticleDetailActivity.this);
                        recyclerView.setLayoutManager(layoutManager);

                        CommentAdapter adapter = new CommentAdapter(commentList);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
}