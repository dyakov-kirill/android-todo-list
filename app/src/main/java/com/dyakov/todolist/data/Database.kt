package com.dyakov.todolist.data

import androidx.room.RoomDatabase
import com.dyakov.todolist.data.mappers.TypeConverters
import com.dyakov.todolist.data.models.TodoItemEntity

@androidx.room.Database(entities = [TodoItemEntity::class], version = 1)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun getTaskDao(): TodoItemDao

}