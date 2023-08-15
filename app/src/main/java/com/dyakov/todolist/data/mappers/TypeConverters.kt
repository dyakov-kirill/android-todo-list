package com.dyakov.todolist.data.mappers

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dyakov.todolist.domain.models.Priority
import java.util.Date

@ProvidedTypeConverter
class TypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
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
