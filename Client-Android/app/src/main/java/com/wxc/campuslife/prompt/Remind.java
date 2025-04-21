package com.wxc.campuslife.prompt;

import java.util.Date;

public class Remind {
    private String eventName;
    private String eventDetail;
    private Date eventTime;
    private int isFin;

    public Remind(String eventName, String eventDetail, Date eventTime, int isFin){
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.eventTime = eventTime;
        this.isFin = isFin;
    }

    public Remind(String eventName, String eventDetail, Date eventTime){
        this.eventName = eventName;
        this.eventDetail = eventDetail;
        this.eventTime = eventTime;
        this.isFin = 0;
    }

    public String getEventName(){
        return eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public Date getEventTime(){
        return eventTime;
    }

    public int getIsFin(){ return isFin; }
}
