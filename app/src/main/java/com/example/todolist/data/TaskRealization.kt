package com.example.todolist.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.todolist.data.database.TaskDAO
import com.example.todolist.model.TodoItemEntity

class TaskRealization(private val taskDAO: TaskDAO) : TaskRepository {
    override val allTasks: LiveData<List<TodoItemEntity>>
        get() = taskDAO.getAllTasks()

    override suspend fun insertTask(task: TodoItemEntity) {
        taskDAO.insert(task)
    }

    override suspend fun deleteTask(task: TodoItemEntity) {
        taskDAO.delete(task)
    }

    override suspend fun updateTask(task: TodoItemEntity) {
        taskDAO.update(task)
    }

    override fun getById(id: Long): LiveData<TodoItemEntity> {
        return taskDAO.getById(id)
    }

    override suspend fun setTaskFlag(id: Long, flag: Int) {
        taskDAO.setTaskFlag(id, flag)
    }
}