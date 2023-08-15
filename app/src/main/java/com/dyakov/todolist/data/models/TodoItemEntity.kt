package com.dyakov.todolist.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dyakov.todolist.domain.models.Priority
import java.util.Date

@Entity(tableName = "tasks")
data class TodoItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "priority") val priority: Priority,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    @ColumnInfo(name = "deadline") val deadline: Date?,
    @ColumnInfo(name = "creation_time") val creationTime: Date,
    @ColumnInfo(name = "edit_time") val editTime: Date?
)
