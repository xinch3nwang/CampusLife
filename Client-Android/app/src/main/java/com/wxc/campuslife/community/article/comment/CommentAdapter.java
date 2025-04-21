package com.wxc.campuslife.community.article.comment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.wxc.campuslife.R;
import com.wxc.campuslife.mine.AccountDetailActivity;
import com.wxc.campuslife.utils.AccessServerData;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private List<Comment> mCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View commentView;
        TextView tv_name;
        TextView tv_content;
        ImageButton tv_subcomment;

        public ViewHolder(View view) {
            super(view);
            commentView = view;
            tv_name = view.findViewById(R.id.textView_commenter);
            tv_content = view.findViewById(R.id.textView_comment_content);
            tv_subcomment = view.findViewById(R.id.button_subcomment);

        }
    }

    public CommentAdapter(List<Comment> commentList) {
        mCommentList = commentList;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);

        final CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);
        holder.commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Comment comment = mCommentList.get(position);

            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Comment comment = mCommentList.get(position);

                Intent intent = new Intent(parent.getContext(), AccountDetailActivity.class);
                intent.putExtra("author", comment.getUsername());
                parent.getContext().startActivity(intent);

            }
        });
        holder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Comment comment = mCommentList.get(position);

                EditText et_reply = new EditText(parent.getContext());
                AlertDialog replyDialog = null;
                AlertDialog.Builder replyBuilder = new AlertDialog.Builder(parent.getContext());
                replyBuilder.setTitle("回复"+comment.getUsername()+":");
                replyBuilder.setView(et_reply);
                replyBuilder.setPositiveButton("回复", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String com = et_reply.getText().toString();
                        SharedPreferences preferences = parent.getContext().getSharedPreferences("account", MODE_PRIVATE);
                        String user_id = preferences.getString("id","");

                        Handler handler = new Handler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = new AccessServerData().publishSubComment(comment.getArticle(),com,user_id,comment.getId());

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(result.equals("pub")) {
                                            Toast.makeText(parent.getContext(), "回复评论成功", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }).start();
                    }
                });
                replyDialog = replyBuilder.create();
                replyDialog.show();
            }
        });
        holder.tv_subcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                int position = holder.getAdapterPosition();
                Comment comment = mCommentList.get(position);

                final CommentInfo[] commentInfo = new CommentInfo[1];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        commentInfo[0] = new AccessServerData().getSubComment(comment.getArticle(), comment.getId());
                        int count = commentInfo[0].getCount();
                        for(int i = 0; i < count; i++) {
                            String id = commentInfo[0].getResult()[i].getId();
                            String create_time = commentInfo[0].getResult()[i].getCreate_time().substring(0,19).replace('T',' ');
                            String content = "回‘"+comment.getContent()+"’:"+commentInfo[0].getResult()[i].getContent();
                            String article = commentInfo[0].getResult()[i].getArticle();
                            String username = commentInfo[0].getResult()[i].getNickname();
                            String parent = commentInfo[0].getResult()[i].getParent();
                            String userid = commentInfo[0].getResult()[i].getUser();
                            Comment subcomment = new Comment(id,create_time,content,article,username,userid,parent);
                            mCommentList.add(position+1, subcomment);
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });

                    }
                }).start();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.tv_name.setText(comment.getUsername()+":");
        holder.tv_content.setText(comment.getContent());

//        new CommentAdapter(mCommentList).notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

}