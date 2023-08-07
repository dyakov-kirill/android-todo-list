package com.dyakov.todolist.data

import com.dyakov.todolist.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskRoomStorage(private val dao: TodoItemDao) : TaskRepository {
    override fun getAllTasks() = dao.getAllTasks()

    override fun getDoneTasks() = dao.getDoneTasks()

    override suspend fun addTask(item: TodoItem) = withContext(Dispatchers.IO) {
        dao.addTask(item)
    }

    override suspend fun updateTask(item: TodoItem)= withContext(Dispatchers.IO) {
        dao.updateTask(item)
    }

    override suspend fun deleteTask(item: TodoItem) = withContext(Dispatchers.IO) {
        dao.deleteTask(item)
    }
}