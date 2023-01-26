package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem

class ListViewModel : ViewModel() {
    val repository = TodoItemsRepository

    fun addTask(task: TodoItem) {
        repository.addTask(task)
    }

}