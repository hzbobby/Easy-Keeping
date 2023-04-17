package com.vividbobo.easy.database.converter;

import androidx.room.TypeConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @TypeConverter
    public static LocalTime fromString(String value) {
        return value == null ? null : LocalTime.parse(value, TIME_FORMATTER);
    }

    @TypeConverter
    public static String localTimeToString(LocalTime time) {
        return time == null ? null : time.format(TIME_FORMATTER);
    }
}
