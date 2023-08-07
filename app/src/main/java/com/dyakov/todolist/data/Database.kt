package com.dyakov.todolist.data

import androidx.room.RoomDatabase
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.TypeConverters

@androidx.room.Database(entities = [TodoItem::class], version = 1)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun getTaskDao(): TodoItemDao

}