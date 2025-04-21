package com.wxc.campuslife.mine;

public class User {
    private String nickname;
    private String id;
    private int age;
    private String gender;
    private String email;
    private String status;
    private String intro;
    private int fans_count;
    private int usrs_liked_count;
    private int article_count;
    private int article_collected_count;
    private int article_liked_count;
    private String[] usrs_liked;
    private String[] fans;
    private String[] articles_liked;
    private String[] articles_collected;

    public User(String nickname, String id, int age, String gender, String email, String status,
                String intro, int fans_count, int usrs_liked_count, int article_count,
                int article_collected_count, int article_liked_count, String[] usrs_liked,
                String[] fans, String[] articles_liked, String[] articles_collected) {
        this.nickname = nickname;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.status = status;
        this.intro = intro;
        this.fans_count = fans_count;
        this.usrs_liked_count = usrs_liked_count;
        this.article_count = article_count;
        this.article_collected_count = article_collected_count;
        this.article_liked_count = article_liked_count;
        this.usrs_liked = usrs_liked;
        this.fans = fans;
        this.articles_liked = articles_liked;
        this.articles_collected = articles_collected;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getUsrs_liked_count() {
        return usrs_liked_count;
    }

    public void setUsrs_liked_count(int usrs_liked_count) {
        this.usrs_liked_count = usrs_liked_count;
    }

    public int getArticle_count() {
        return article_count;
    }

    public void setArticle_count(int article_count) {
        this.article_count = article_count;
    }

    public int getArticle_collected_count() {
        return article_collected_count;
    }

    public void setArticle_collected_count(int article_collected_count) {
        this.article_collected_count = article_collected_count;
    }

    public int getArticle_liked_count() {
        return article_liked_count;
    }

    public void setArticle_liked_count(int article_liked_count) {
        this.article_liked_count = article_liked_count;
    }

    public String[] getUsrs_liked() {
        return usrs_liked;
    }

    public void setUsrs_liked(String[] usrs_liked) {
        this.usrs_liked = usrs_liked;
    }

    public String[] getFans() {
        return fans;
    }

    public void setFans(String[] fans) {
        this.fans = fans;
    }

    public String[] getArticles_liked() {
        return articles_liked;
    }

    public void setArticles_liked(String[] articles_liked) {
        this.articles_liked = articles_liked;
    }

    public String[] getArticles_collected() {
        return articles_collected;
    }

    public void setArticles_collected(String[] articles_collected) {
        this.articles_collected = articles_collected;
    }
}
