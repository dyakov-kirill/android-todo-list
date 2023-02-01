package com.example.todolist.view.EditTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import com.example.todolist.model.TodoItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class EditTaskViewModel : ViewModel() {
    private val repository = TodoItemsRepository
    val currentTime = Calendar.getInstance()
    val deadline = Calendar.getInstance()
    lateinit var task : TodoItem

    fun setDeadline(day: Int, month: Int, year: Int) {
        deadline.set(Calendar.YEAR, year)
        deadline.set(Calendar.MONTH, month)
        deadline.set(Calendar.DAY_OF_MONTH, day)
    }

    fun getTask(id: Long): LiveData<TodoItemEntity> {
        return repository.database.getById(id)
    }

    fun updateTask(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.database.updateTask(TodoItem.toEntity(item))
        }
    }

    fun deleteTask(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.database.deleteTask(TodoItem.toEntity(item))
        }
    }
}