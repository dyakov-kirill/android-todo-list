package com.example.todolist.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.todolist.model.TodoItem
import com.example.todolist.model.Utils
import java.util.*

object TodoItemsRepository {
    lateinit var database: TaskRealization

    var numOfDone = MutableLiveData<Int>()
    init {
        numOfDone.value = 0
    }


}