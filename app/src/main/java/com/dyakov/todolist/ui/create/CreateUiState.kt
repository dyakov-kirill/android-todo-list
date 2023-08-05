package com.dyakov.todolist.ui.create

import com.dyakov.todolist.Priority
import java.util.*

data class CreateUiState(
    val description: String = "",
    val priority: Priority = Priority.NO,
    val deadline: Date? = null,
    val isDeadlineSet: Boolean = false
) {
}