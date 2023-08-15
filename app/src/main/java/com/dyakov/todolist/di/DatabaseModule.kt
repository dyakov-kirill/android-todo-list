package com.dyakov.todolist.di

import android.content.Context
import androidx.room.Room
import com.dyakov.todolist.data.Database
import com.dyakov.todolist.data.TodoItemDao
import com.dyakov.todolist.data.mappers.TypeConverters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

/*    @Provides
    @Singleton
    fun provideRepository(dao: TodoItemDao) : TodoItemRepository {
        return TodoItemRepositoryImpl(dao)
    }*/

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : Database {
        return Room.databaseBuilder(context, Database::class.java, "task_database")
            .addTypeConverter(TypeConverters())
            .build()
    }

    @Provides
    fun provideDao(database: Database) : TodoItemDao {
        return database.getTaskDao()
    }
}