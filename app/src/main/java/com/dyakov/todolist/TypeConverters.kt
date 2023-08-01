package com.dyakov.todolist

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.Date

@ProvidedTypeConverter
class TypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun priorityToInt(priority: Priority): Int {
        return priority.priority
    }

    @TypeConverter
    fun intToPriority(value: Int): Priority {
        return Priority.values()[value]
    }
}