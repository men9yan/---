package com.wukong.logisticsproject.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
public class DateUtils {
    //日期转换成字符串
    public static LocalDateTime date2String(Date date ) {

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    //字符串转换成日期
    public static Date string2Date(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parse = sdf.parse(str);
        return parse;
    }
}
