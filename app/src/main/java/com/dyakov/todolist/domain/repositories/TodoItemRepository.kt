package com.dyakov.todolist.domain.repositories

import com.dyakov.todolist.domain.models.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {
    fun getAllTasks(): Flow<List<TodoItem>>

    fun getDoneTasks(): Flow<List<TodoItem>>

    suspend fun addTask(item: TodoItem)

    suspend fun updateTask(item: TodoItem)

    suspend fun deleteTask(item: TodoItem)
}