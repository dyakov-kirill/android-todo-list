package com.example.todolist.data

import androidx.lifecycle.MutableLiveData
import com.example.todolist.model.TodoItem
import com.example.todolist.model.Utils
import java.util.*

object TodoItemsRepository {
    var tasks = mutableListOf(
        TodoItem("1", "Make coffee and something else", Utils.Importance.LOW,
        Utils.Flag.NOT_DONE, null, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("2", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.NOT_DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("3", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("4", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.DONE, null, Calendar.getInstance().time, Calendar.getInstance().time
        )
    )

    var numOfDone = MutableLiveData<Int>()
    init {
        numOfDone.value = 0
    }
    fun getTaskList(): List<TodoItem> {
        return tasks
    }

    fun addTask(task: TodoItem) {
        tasks.add(task)
    }

    fun replaceTask(task: TodoItem, taskPos: Int) {
        tasks[taskPos] = task
    }

}