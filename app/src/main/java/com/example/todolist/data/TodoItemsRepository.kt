package com.example.todolist.data

import androidx.lifecycle.MutableLiveData

object TodoItemsRepository {
    lateinit var database: TaskRealization

    var numOfDone = MutableLiveData<Int>()
    init {
        numOfDone.value = 0
    }


}