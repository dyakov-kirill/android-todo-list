package com.dyakov.todolist.domain

import com.dyakov.todolist.domain.models.TodoItem
import com.dyakov.todolist.domain.repositories.TodoItemRepository
import javax.inject.Inject

class TodoItemInteractor @Inject constructor(
    private val repository: TodoItemRepository
) {
    fun getAllTasks() = repository.getAllTasks()

    fun getDoneTasks() = repository.getDoneTasks()

    suspend fun addTask(item: TodoItem) {
        repository.addTask(item)
    }
    suspend fun deleteTask(item: TodoItem) {
        repository.deleteTask(item)
    }
    suspend fun updateTask(item: TodoItem) {
        repository.updateTask(item)
    }
}