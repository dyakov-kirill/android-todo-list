package com.dyakov.todolist

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Date
import kotlin.random.Random

object TempTodoItemRepository {

    val taskList: List<TodoItem> = listOf(
        TodoItem(
            "0",
            "Make coffee",
            Priority.NO,
            true,
            null,
            Date(System.currentTimeMillis()),
            null
        ),
        TodoItem(
            "1",
            "Make eggs",
            Priority.NO,
            true,
            Date(System.currentTimeMillis() - 100000),
                    Date(System.currentTimeMillis()),
            null
        )
    )
}