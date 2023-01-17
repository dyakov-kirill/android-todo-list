package com.example.todolist

import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class TodoItemsRepository() {
    var tasks = mutableListOf<TodoItem>(
        TodoItem("Make breakfast", "Make coffee and something else", Utils.Importance.LOW,
        Utils.Flag.NOT_DONE, "2022-12-12", "2022-12-11", "2022-12-11"
        )
    )
    fun GetTaskList(): List<TodoItem> {
        return tasks
    }

    fun AddTask(task: TodoItem) {
        tasks.add(task)
    }

}