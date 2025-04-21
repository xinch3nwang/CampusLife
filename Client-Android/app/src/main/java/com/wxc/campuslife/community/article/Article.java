package com.wxc.campuslife.community.article;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable{

    private String title;
    private String tag;
    private String content;
    private String author;
    private String date;
    private String articleid;
    private int readed_count;
    private int liked_count;
    private int collected_count;

    public Article(String title, String tag, String content, String author, String date, String id,
                   int readed_count, int liked_count, int collected_count){
        this.title = title;
        this.tag = tag;
        this.author = author;
        this.content = content;
        this.date = date;
        this.articleid = id;
        this.collected_count = collected_count;
        this.liked_count = liked_count;
        this.readed_count = readed_count;
    }


    public int getReaded_count() {
        return readed_count;
    }

    public void setReaded_count(int readed_count) {
        this.readed_count = readed_count;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }

    public int getCollected_count() {
        return collected_count;
    }

    public void setCollected_count(int collected_count) {
        this.collected_count = collected_count;
    }

    public String getArticleid() {
        return articleid;
    }

    public void setArticleid(String articleid) {
        this.articleid = articleid;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

