package com.dyakov.todolist.domain.models

import java.io.Serializable
import java.util.Date

data class TodoItem (
    val id: String,
    val description: String,
    val priority: Priority,
    val isDone: Boolean,
    val deadline: Date?,
    val creationTime: Date,
    val editTime: Date?
) : Serializable
