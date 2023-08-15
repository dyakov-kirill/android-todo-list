package com.dyakov.todolist.presentation.create

import com.dyakov.todolist.domain.models.Priority
import java.util.*

data class CreateUiState(
    val description: String = "",
    val priority: Priority = Priority.NO,
    val deadline: Date? = null,
    val isDeadlineSet: Boolean = false
)
