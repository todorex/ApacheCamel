package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pengwan on 2017/7/13.
 */
public class MyDateUtils {
    public static String generateDayStr(int dayDiff){
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        date.set(Calendar.DATE, date.get(Calendar.DATE)+dayDiff);
        return (new SimpleDateFormat("yyyyMMdd")).format(date.getTime());
    }
}
