package com.example.todolist

import androidx.lifecycle.MutableLiveData
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

object TodoItemsRepository {
    var tasks = mutableListOf<TodoItem>(
        TodoItem("1", "Make coffee and something else", Utils.Importance.LOW,
        Utils.Flag.NOT_DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("2", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.NOT_DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("3", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.NOT_DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        ),
        TodoItem("4", "Make coffee and something else", Utils.Importance.LOW,
            Utils.Flag.NOT_DONE, Calendar.getInstance().time, Calendar.getInstance().time, Calendar.getInstance().time
        )
    )

    var numOfDone = MutableLiveData<Int>()
    lateinit var adapter: RecyclerViewAdapter
    init {
        numOfDone.value = 0
    }
    fun getTaskList(): List<TodoItem> {
        return tasks
    }

    fun addTask(task: TodoItem) {
        tasks.add(task)
        adapter.notifyItemInserted(tasks.size - 1)
    }

}