package com.wxc.campuslife.mine;

public class UserInfo {
    private String code;
    private String msg;
    private int count;
    private Res[] result;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Res[] getResult() {
        return result;
    }

    public void setResult(Res[] result) {
        this.result = result;
    }

    public static class Res{

        private String model;
        private String pk;//id
        private Fld fields;
        private String nickname;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Fld getFields() {
            return fields;
        }

        public String getModel() {
            return model;
        }

        public String getPk() {
            return pk;
        }

        public void setFields(Fld fields) {
            this.fields = fields;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setPk(String pk) {
            this.pk = pk;
        }

        public static class Fld{

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
    }


}
