package com.dyakov.todolist.presentation.list.viewmodel

import com.dyakov.todolist.domain.models.TodoItem

data class ListUiState(
    val data: List<TodoItem>,
    val isDoneHidden: Boolean
)
