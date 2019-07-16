package com.example.reservabuses

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun localDateTimeTo (localDateTime: LocalDateTime): String? {
        return localDateTime.toString()
    }
}