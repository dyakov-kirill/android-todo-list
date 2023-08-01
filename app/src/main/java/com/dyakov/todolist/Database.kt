package com.dyakov.todolist

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@androidx.room.Database(entities = [TodoItem::class], version = 1)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class Database : RoomDatabase() {
    abstract fun getTaskDao(): TodoItemDao

    companion object {
        private var database: Database?= null
        @Synchronized
        fun getInstance(context: Context): Database =
            if (database == null) {
                database = Room.databaseBuilder(context, Database::class.java, "task_database")
                    .addTypeConverter(TypeConverters()).build()
                database as Database
            } else {
                database as Database
            }
    }
}