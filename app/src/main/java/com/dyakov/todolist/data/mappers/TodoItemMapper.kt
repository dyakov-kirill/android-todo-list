package com.dyakov.todolist.data.mappers

import com.dyakov.todolist.data.models.TodoItemEntity
import com.dyakov.todolist.domain.models.TodoItem
import javax.inject.Inject

class TodoItemMapper @Inject constructor() {
    fun mapToEntity(task: TodoItem): TodoItemEntity = with(task) {
        TodoItemEntity(
            id = id,
            description = description,
            priority = priority,
            isDone = isDone,
            deadline = deadline,
            creationTime = creationTime,
            editTime = editTime
        )
    }

    fun mapToDomain(task: TodoItemEntity): TodoItem = with(task) {
        TodoItem(
            id = id,
            description = description,
            priority = priority,
            isDone = isDone,
            deadline = deadline,
            creationTime = creationTime,
            editTime = editTime
        )
    }
}
