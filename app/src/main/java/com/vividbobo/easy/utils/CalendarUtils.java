package com.vividbobo.easy.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarUtils {
    private static final Calendar calendar = Calendar.getInstance();
    private static final SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
    private static final SimpleDateFormat yearMonthFormat = new SimpleDateFormat("YYYY-MM");
    private static final Map<Integer, String> dayOfWeek = new HashMap<>();

    static {
        dayOfWeek.put(1, "周日");
        dayOfWeek.put(2, "周一");
        dayOfWeek.put(3, "周二");
        dayOfWeek.put(4, "周三");
        dayOfWeek.put(5, "周四");
        dayOfWeek.put(6, "周五");
        dayOfWeek.put(7, "周六");
    }

    public static String getDateMMdd(Date date) {
        return monthDayFormat.format(date);
    }


    public static int getDayOfWeek(Date date) {
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayOfWeekCN(Date date) {
        return dayOfWeek.get(getDayOfWeek(date));
    }

    public static String getDateYYYYMM(Date today) {
        return yearMonthFormat.format(today);
    }
}
