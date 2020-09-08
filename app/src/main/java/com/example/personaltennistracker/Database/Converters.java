package com.example.personaltennistracker.Database;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String fromStrokeType(StrokeDao.StrokeType strokeType){
        return  strokeType.name();
    }

    @TypeConverter
    public static StrokeDao.StrokeType toStrokeType(String strokeType){
        return StrokeDao.StrokeType.valueOf(strokeType);
    }
}
