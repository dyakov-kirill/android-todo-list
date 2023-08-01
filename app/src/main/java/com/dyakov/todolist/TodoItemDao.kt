package com.dyakov.todolist

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TodoItem>>

    @Query("SELECT * FROM tasks WHERE is_done=false")
    fun getAllTasks2(): Flow<List<TodoItem>>

    @Insert
    suspend fun addTask(item: TodoItem)

    @Update
    suspend fun updateTask(item: TodoItem)

    @Delete
    suspend fun deleteTask(item: TodoItem)
}