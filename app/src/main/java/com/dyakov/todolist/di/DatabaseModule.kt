package com.dyakov.todolist.di

import android.content.Context
import androidx.room.Room
import com.dyakov.todolist.TypeConverters
import com.dyakov.todolist.data.Database
import com.dyakov.todolist.data.TaskRepository
import com.dyakov.todolist.data.TaskRoomStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideRepository(@ApplicationContext context: Context) : TaskRepository {
        val database = Room.databaseBuilder(context, Database::class.java, "task_database").addTypeConverter(
            TypeConverters()
        ).build()
        return TaskRoomStorage(database.getTaskDao())
    }
}