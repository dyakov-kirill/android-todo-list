package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import java.util.*

class CreateTaskViewModel : ViewModel() {
    val currentTime = Calendar.getInstance()
    val deadline = Calendar.getInstance()


    fun setDeadline(day: Int, month: Int, year: Int) {
        deadline.set(Calendar.YEAR, year)
        deadline.set(Calendar.MONTH, month)
        deadline.set(Calendar.DAY_OF_MONTH, day)
    }

}