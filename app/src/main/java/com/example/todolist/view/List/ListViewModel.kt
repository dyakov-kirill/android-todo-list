package com.example.todolist.view.List

import android.app.Application
import androidx.lifecycle.*
import com.example.todolist.data.database.TaskDatabase
import com.example.todolist.data.TaskRealization
import com.example.todolist.model.TodoItemEntity
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : AndroidViewModel(application) {
    val repository = TodoItemsRepository
    val context = application

    fun initDatabase() {
        val taskDAO = TaskDatabase.getInstance(context).getTaskDao()
        TodoItemsRepository.database = TaskRealization(taskDAO)
    }

    fun getAllTasks(): LiveData<List<TodoItemEntity>> {
        return repository.database.allTasks
    }

    fun setTaskFlag(taskId: Long, flag: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.database.setTaskFlag(taskId, flag)
        }
    }

    fun deleteTask(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.database.deleteTask(TodoItem.toEntity(item))
        }
    }



}