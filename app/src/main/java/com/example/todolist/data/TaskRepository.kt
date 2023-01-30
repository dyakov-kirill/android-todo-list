package com.example.todolist.data

import androidx.lifecycle.LiveData
import com.example.todolist.model.TodoItemEntity

interface TaskRepository {
    val allTasks: LiveData<List<TodoItemEntity>>
    suspend fun insertTask(task: TodoItemEntity)
    suspend fun deleteTask(task: TodoItemEntity)
    suspend fun updateTask(task: TodoItemEntity)
    fun getById(id: Long): LiveData<TodoItemEntity>
    suspend fun setTaskFlag(id: Long, flag: Int)
}