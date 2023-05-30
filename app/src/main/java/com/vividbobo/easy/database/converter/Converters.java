package com.vividbobo.easy.database.converter;

import android.util.Log;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vividbobo.easy.database.model.Tag;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Converters {
//    public static Integer[] toIntegerArray(Set<Integer> integerSet) {
//        Integer[] res = new Integer[integerSet.size()];
//        int cnt = 0;
//        for (Integer num :
//                integerSet) {
//            res[cnt] = num;
//            cnt++;
//        }
//        return res;
//    }
//
//    public static Set<Integer> toIntegerSet(Integer[] integerArray) {
//        Set<Integer> res = new HashSet<>();
//        for (int i = 0; i < integerArray.length; i++) {
//            res.add(integerArray[i]);
//        }
//        return res;
//    }


    @TypeConverter
    public static Timestamp toTimestamp(String value) {
        Type timestampType = new TypeToken<Timestamp>() {
        }.getType();
        return new Gson().fromJson(value, timestampType);
    }

    @TypeConverter
    public static String toTimestampString(Timestamp value) {
        Gson gson = new Gson();
        return gson.toJson(value);
    }

    @TypeConverter
    public static List<Tag> fromString(String value) {
        Type listType = new TypeToken<List<Tag>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromTagList(List<Tag> tags) {
        Gson gson = new Gson();
        return gson.toJson(tags);
    }


    @TypeConverter
    public static String fromIntegerListToString(List<Integer> integerList) {
        if (integerList == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < integerList.size(); i++) {
            stringBuilder.append(integerList.get(i));
            if (i < integerList.size() - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public static List<Integer> fromStringToIntegerList(String string) {
        if (string == null) {
            return null;
        }

        List<String> stringList = Arrays.asList(string.split(","));
        List<Integer> integerList = new ArrayList<>();
        for (String str : stringList) {
            integerList.add(Integer.parseInt(str));
        }

        return integerList;
    }

    @TypeConverter
    public static String fromStringListToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            stringBuilder.append(stringList.get(i));
            if (i < stringList.size() - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public static List<String> fromStringToStringList(String string) {
        if (string == null) {
            return null;
        }
        String[] splitStrArr = string.strip().split(",");
        ArrayList<String> res = new ArrayList<String>();
        for (int i = 0; i < splitStrArr.length; i++) {
            if (splitStrArr[i].strip().isEmpty()) continue;
            res.add(splitStrArr[i]);
        }
        return res;
    }

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @TypeConverter
    public static String fromDateToDateText(Date date) {
        if (date == null) {
            return null;
        }
        return dateFormat.format(date);
    }

    @TypeConverter
    public static Date fromDateStringToDate(String dateText) {
        if (dateText == null) {
            return null;
        }
        try {
            return new Date(dateFormat.parse(dateText).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static long fromTimeToLong(Time time) {
        if (time == null) {
            return 0;
        }
        return time.getTime();
    }

    @TypeConverter
    public static Time fromLongToTime(long time) {
        if (time == 0) {
            return null;
        }
        return new Time(time);
    }

    @TypeConverter
    public static Timestamp fromLong(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long toLong(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getTime();
    }
}
