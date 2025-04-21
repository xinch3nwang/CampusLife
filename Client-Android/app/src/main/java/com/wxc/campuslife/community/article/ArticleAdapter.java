package com.wxc.campuslife.community.article;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wxc.campuslife.R;
import com.wxc.campuslife.utils.AccessServerData;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>{

    private List<Article> mArticleList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View articleView;
        TextView tv_name;
        TextView tv_title;
        TextView tv_time;
        Button btn_follow;

        public ViewHolder(View view) {
            super(view);
            articleView = view;
            tv_name = view.findViewById(R.id.textView_fusername);
            tv_title = view.findViewById(R.id.textView_ftitle);
            tv_time = view.findViewById(R.id.textView_ftime);
            //btn_follow = view.findViewById(R.id.button_follow);
        }
    }

    public ArticleAdapter(List<Article> articleList) {
        mArticleList = articleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);
                Intent intent = new Intent(view.getContext(), ArticleDetailActivity.class);
                intent.putExtra("article", article);
                view.getContext().startActivity(intent);
            }
        });
        holder.articleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                Article article = mArticleList.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(parent.getContext());
                dialog.setTitle("删除待办？");
                dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String article_id = article.getArticleid();
                        Handler handler = new Handler();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if(new AccessServerData().delArticle(article_id)){
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mArticleList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }).start();

                    }
                });
                dialog.show();
                return true;
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        holder.tv_title.setText(article.getTitle());
        holder.tv_name.setText(article.getAuthor());
        holder.tv_time.setText(article.getDate().toString());

    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

}