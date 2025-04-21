package com.wxc.campuslife.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.wxc.campuslife.R;
import com.wxc.campuslife.community.article.ArticleInfo;
import com.wxc.campuslife.community.article.comment.CommentInfo;
import com.wxc.campuslife.mine.UserInfo;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccessServerData {

    /*
    文章相关
     */
    public ArticleInfo getArticleByTag(String tag){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("tagname", tag)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "article/articles_tag/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            ArticleInfo articleInfo = gson.fromJson(r, ArticleInfo.class);
            return articleInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArticleInfo getArticleByAuthor(String authorid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usr_id", authorid)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "article/articles_author/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            ArticleInfo articleInfo = gson.fromJson(r, ArticleInfo.class);
            return articleInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateArticle(String title, String content, String tag, String articleid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("title", title)
                .add("content", content)
                .add("articleid", articleid)
                .add("tagname",tag)
                .build();
        Request postRequest = new Request.Builder()
                .url(getUrl()+"article/article_update/")
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            if(r.equals("ok")) return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delArticle(String id){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("articleid", id)
                .build();
        Log.d("response",id);
        String url = getUrl() + "article/article_delete/";
        //Log.d("response",url);
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(url)
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /*
    用户相关
     */
    public String getUserid(String author){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("nickname",author)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/get_id_nickname/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String userFollow(String author){
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        String fan_id = preferences.getString("id","" );

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("fanid", fan_id)
                .add("authornickname",author)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/user_follow/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }


    public String followUsers(String userid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usrid", userid)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/follow_users/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String fans(String userid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usrid", userid)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/fans/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public UserInfo getUserProfile(String userid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usrid", userid)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/user_profile/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(r, UserInfo.class);
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserInfo updateUserProfile(String nickname, String age, String email, String gender,
                                      String intro, String status, String userid){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("usrid", userid)
                .add("nickname", nickname)
                .add("age", age)
                .add("email", email)
                .add("gender", gender)
                .add("intro", intro)
                .add("status", status)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "account/user_profile_update/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(r, UserInfo.class);
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
    评论相关
     */

    public CommentInfo getCommentByArticle(String article_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("article_id", article_id)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "comment/comment_get/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            CommentInfo commentInfo = gson.fromJson(r, CommentInfo.class);
            return commentInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public CommentInfo getSubComment(String article_id, String parent_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("article_id", article_id)
                .add("parent_id", parent_id)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "comment/comment_get/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            Log.d("response",r);
            Gson gson = new Gson();
            CommentInfo commentInfo = gson.fromJson(r, CommentInfo.class);
            return commentInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String publishTopComment(String article_id, String comment, String user_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", user_id)
                .add("article_id", article_id)
                .add("content", comment)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "comment/comment_publish/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String publishSubComment(String article_id, String comment, String user_id, String parent) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", user_id)
                .add("article_id", article_id)
                .add("content", comment)
                .add("parent_id", parent)
                .build();
        Request postRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "comment/comment_publish/")
                .build();
        try {
            Response response = client.newCall(postRequest).execute();
            String r = response.body().string();
            return r;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getArticleRead(String article_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("articleid", article_id)
                .build();
        Request getRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "article/article_detail/")
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String likeArticle(String article_id ,String usr_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("article_id", article_id)
                .add("usr_id", usr_id)
                .build();
        Request getRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "article/article_like/")
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String collectArticle(String article_id ,String usr_id) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("article_id", article_id)
                .add("usr_id", usr_id)
                .build();
        Request getRequest = new Request
                .Builder()
                .post(requestBody)
                .url(getUrl() + "article/article_collect/")
                .build();
        try {
            Response response = client.newCall(getRequest).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public String getUrl() {
        String url = MyApplication.getContext().getResources().getString(R.string.url_wireless);
        return url;
    }
}
