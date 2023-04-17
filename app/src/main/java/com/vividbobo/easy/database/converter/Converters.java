package com.vividbobo.easy.database.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vividbobo.easy.database.model.Tag;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static List<Tag> fromString(String value) {
        Type listType = new TypeToken<List<Tag>>() {}.getType();
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

        return new ArrayList<>(Arrays.asList(string.split(",")));
    }

    @TypeConverter
    public static long fromDateToLong(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    @TypeConverter
    public static Date fromLongToDate(long time) {
        if (time == 0) {
            return null;
        }
        return new Date(time);
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
