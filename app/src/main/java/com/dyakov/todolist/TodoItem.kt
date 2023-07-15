package com.dyakov.todolist

data class TodoItem(
    val id: String,
    val text: String,
    val priority: Priority
)
