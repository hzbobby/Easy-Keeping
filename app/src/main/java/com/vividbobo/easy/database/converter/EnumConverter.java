package com.vividbobo.easy.database.converter;

import androidx.room.TypeConverter;

import com.vividbobo.easy.database.model.Resource;

import java.time.LocalTime;

/**
 * 将美剧类型转化为int类型
 */
public class EnumConverter {
    @TypeConverter
    public static int toOrdinal(Resource.ResourceType enumValue) {
        return enumValue.ordinal();
    }

    @TypeConverter
    public static Resource.ResourceType toResourceType(int value) {
        for (Resource.ResourceType type : Resource.ResourceType.values()) {
            if (type.ordinal() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ResourceType value: " + value);
    }
}
