package com.dyakov.todolist.data.repositories

import com.dyakov.todolist.data.TodoItemDao
import com.dyakov.todolist.data.mappers.TodoItemMapper
import com.dyakov.todolist.domain.models.TodoItem
import com.dyakov.todolist.domain.repositories.TodoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val localSource: TodoItemDao,
    private val mapper: TodoItemMapper) : TodoItemRepository {
    override fun getAllTasks(): Flow<List<TodoItem>> {
        return flow {
            localSource.getAllTasks().collect {
                emit(it.map { item -> mapper.mapToDomain(item) })
            }
        }
    }

    override fun getDoneTasks(): Flow<List<TodoItem>> {
        return flow {
            localSource.getDoneTasks().collect {
                emit(it.map { item -> mapper.mapToDomain(item) })
            }
        }
    }

    override suspend fun addTask(item: TodoItem) = withContext(Dispatchers.IO) {
        localSource.addTask(mapper.mapToEntity(item))
    }

    override suspend fun updateTask(item: TodoItem) = withContext(Dispatchers.IO) {
        localSource.updateTask(mapper.mapToEntity(item))
    }

    override suspend fun deleteTask(item: TodoItem) = withContext(Dispatchers.IO) {
        localSource.deleteTask(mapper.mapToEntity(item))
    }
}