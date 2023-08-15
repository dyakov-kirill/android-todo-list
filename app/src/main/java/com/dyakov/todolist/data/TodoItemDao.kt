package com.dyakov.todolist.data

import androidx.room.*
import com.dyakov.todolist.data.models.TodoItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TodoItemEntity>>

    @Query("SELECT * FROM tasks WHERE is_done=false")
    fun getDoneTasks(): Flow<List<TodoItemEntity>>

    @Insert
    suspend fun addTask(item: TodoItemEntity)

    @Update
    suspend fun updateTask(item: TodoItemEntity)

    @Delete
    suspend fun deleteTask(item: TodoItemEntity)
}