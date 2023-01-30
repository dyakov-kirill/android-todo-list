package com.example.todolist.view.CreateTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class CreateTaskViewModel : ViewModel() {
    val currentTime = Calendar.getInstance()
    val deadline = Calendar.getInstance()

    fun addTask(task: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            TodoItemsRepository.database.insertTask(TodoItem.toEntity(task))
        }
    }

    fun setDeadline(day: Int, month: Int, year: Int) {
        deadline.set(Calendar.YEAR, year)
        deadline.set(Calendar.MONTH, month)
        deadline.set(Calendar.DAY_OF_MONTH, day)
    }

}