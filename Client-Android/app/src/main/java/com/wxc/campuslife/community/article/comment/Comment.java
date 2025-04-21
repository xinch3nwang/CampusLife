package com.wxc.campuslife.community.article.comment;

public class Comment {
    private String id;
    private String create_time;
    private String content;
    private String article;
    private String username;
    private String parent;
    private String userid;


    public Comment(String id, String create_time, String content, String article, String username, String userid, String parent) {
        this.id = id;
        this.create_time = create_time;
        this.content = content;
        this.article = article;
        this.username = username;
        this.parent = parent;
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
