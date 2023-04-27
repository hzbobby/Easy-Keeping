package com.vividbobo.easy.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public static String getYYYYMM(Date today) {
        return yearMonthFormat.format(today);
    }


    public static Date getDateFrom(String dateString, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            java.util.Date date = sdf.parse(dateString);
            return new Date(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        // 将月份加 1，再减去 1 天，得到当月最后一天
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return new Date(calendar.getTime().getTime());
    }
}



