package com.example.todolist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todolist.model.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDAO

    companion object {
        private var database: TaskDatabase?= null
        @Synchronized
        fun getInstance(context: Context): TaskDatabase =
            if (database == null) {
                database = Room.databaseBuilder(context, TaskDatabase::class.java, "task_database").build()
                database as TaskDatabase
            } else {
                database as TaskDatabase
            }
    }
}