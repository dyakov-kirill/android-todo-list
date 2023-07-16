package com.dyakov.todolist

import java.sql.Time

data class TodoItem(
    val id: String,
    val text: String,
    val priority: Priority,
    val isDone: Boolean,
    val creationTime: Time,
    val editTime: Time
)
