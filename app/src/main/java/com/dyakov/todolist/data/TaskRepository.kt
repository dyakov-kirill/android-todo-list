package com.dyakov.todolist.data

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dyakov.todolist.TodoItem
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TodoItem>>

    fun getDoneTasks(): Flow<List<TodoItem>>

    suspend fun addTask(item: TodoItem)

    suspend fun updateTask(item: TodoItem)

    suspend fun deleteTask(item: TodoItem)
}