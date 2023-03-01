package com.fs.hc.fhir.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    //private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYY-MM-DD'T'hh:mm:ss.sss+zz:zz");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.sss'Z'");

    public static String getInstant(){
        return simpleDateFormat.format(new Date());
    }

    public static Date getDate(String value){
        try {
            return simpleDateFormat.parse(value);
        }catch (ParseException pe){
            return null;
        }
    }

    public static String convertInstant(Date date){
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args){
        System.out.println(DateTimeUtil.getInstant());
    }
}
