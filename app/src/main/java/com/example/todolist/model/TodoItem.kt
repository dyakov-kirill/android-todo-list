package com.example.todolist.model

import com.example.todolist.model.Utils
import java.util.*

data class TodoItem(
    var id: String,
    var info: String,
    var importance: Utils.Importance,
    var flag: Utils.Flag,
    var deadline: Date?,
    var createDate: Date,
    var editDate: Date
    )