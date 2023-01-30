package com.example.todolist.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.model.TodoItemEntity

@Dao
interface TaskDAO {
    @Insert
    suspend fun insert(task: TodoItemEntity)
    @Delete
    suspend fun delete(task: TodoItemEntity)
    @Update
    suspend fun update(task: TodoItemEntity)
    @Query("SELECT * from TodoItemEntity")
    fun getAllTasks(): LiveData<List<TodoItemEntity>>
    @Query("SELECT * FROM TodoItemEntity WHERE id=:id")
    fun getById(id: Long): LiveData<TodoItemEntity>
    @Query("UPDATE TodoItemEntity SET flag=:flag WHERE id=:id")
    suspend fun setTaskFlag(id: Long, flag: Int)
}