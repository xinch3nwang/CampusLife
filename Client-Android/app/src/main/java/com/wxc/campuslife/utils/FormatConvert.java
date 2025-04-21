package com.wxc.campuslife.utils;

import java.util.Calendar;
import java.util.Date;

public class FormatConvert {
    public Calendar datetocal(Date d){
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }
}
