package com.example.todolist

import java.time.LocalDate
import java.util.*

data class TodoItem(
    var id: String,
    var info: String,
    var importance: Utils.Importance,
    var flag: Utils.Flag,
    var deadline: String,
    var createDate: String,
    var editDate: String
    )