package com.example.todolist.model

import com.example.todolist.model.Utils
import java.util.*

data class TodoItem(
    var id: Int?,
    var info: String,
    var importance: Int,
    var flag: Int,
    var deadline: Date?,
    var createDate: Date,
    var editDate: Date
    ) {

    companion object {
        fun toEntity(task: TodoItem): TodoItemEntity {
            return TodoItemEntity(
                task.id,
                task.info,
                task.importance,
                task.flag,
                task.deadline?.time,
                task.createDate.time,
                task.editDate.time
            )
        }
    }
}
