package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import java.util.*

class EditTaskViewModel : ViewModel() {
    val repository = TodoItemsRepository
    val currentTime = Calendar.getInstance()
    val deadline = Calendar.getInstance()
    lateinit var task : TodoItem

    fun setDeadline(day: Int, month: Int, year: Int) {
        deadline.set(Calendar.YEAR, year)
        deadline.set(Calendar.MONTH, month)
        deadline.set(Calendar.DAY_OF_MONTH, day)
    }
}