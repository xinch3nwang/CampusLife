package com.wxc.campuslife.community.article;

public class ArticleInfo {
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

            private String title;
            private String content;
            private String pub_time;
            private String update_time;
            private int readed_count;
            private int admired_count;
            private int liked_count;
            private int collected_count;
            private int commented_count;
            private String up_time;
            private String status;
            private String author;
            private String tag_name;


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPub_time() {
                return pub_time;
            }

            public void setPub_time(String pub_time) {
                this.pub_time = pub_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getReaded_count() {
                return readed_count;
            }

            public void setReaded_count(int readed_count) {
                this.readed_count = readed_count;
            }

            public int getAdmired_count() {
                return admired_count;
            }

            public void setAdmired_count(int admired_count) {
                this.admired_count = admired_count;
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

            public int getCommented_count() {
                return commented_count;
            }

            public void setCommented_count(int commented_count) {
                this.commented_count = commented_count;
            }

            public String getUp_time() {
                return up_time;
            }

            public void setUp_time(String up_time) {
                this.up_time = up_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }


        }
    }
}
