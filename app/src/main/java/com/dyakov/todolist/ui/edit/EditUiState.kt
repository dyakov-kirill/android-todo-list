package com.dyakov.todolist.ui.edit

import com.dyakov.todolist.Priority
import java.util.*

data class EditUiState(
    val id: String = "",
    val description: String = "",
    val priority: Priority = Priority.NO,
    val isDone: Boolean = false,
    val deadline: Date? = null,
    val isDeadlineSet: Boolean = false,
    val creationTime: Date = Date(0)
)
