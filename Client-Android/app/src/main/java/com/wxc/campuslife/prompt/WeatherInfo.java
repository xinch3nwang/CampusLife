package com.wxc.campuslife.prompt;

public class WeatherInfo {
    private String code;
    private Now now;
    private String fxLink;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public String getFxLink(){
        return fxLink;
    }

    public void setNow(String text, String f){
        this.now.text = text;
        this.now.feelsLike = f;
    }

    public Now getNow(){
        return now;
    }

    public static class Now{
        private String feelsLike;
        private String text;
        public void setFeelsLike(String feelsLike){
            this.feelsLike = feelsLike;
        }

        public void setText(String text){
            this.text = text;
        }

        public String getFeelsLik(){
            return feelsLike;
        }

        public String getText(){
            return text;
        }
    }
}
