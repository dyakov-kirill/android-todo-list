package com.dyakov.todolist.ui.list.viewmodel

import com.dyakov.todolist.TodoItem

data class ListUiState(
    val data: List<TodoItem>,
    val isDoneHidden: Boolean
)
