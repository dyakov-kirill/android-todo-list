package com.example.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo var info: String,
    @ColumnInfo var importance: Int,
    @ColumnInfo var flag: Int,
    @ColumnInfo var deadline: Long?,
    @ColumnInfo(name = "create_date") var createDate: Long,
    @ColumnInfo(name = "edit_date") var editDate: Long
) {
    companion object {
        fun toModel(entity: TodoItemEntity): TodoItem {
            return TodoItem(
                entity.id!!,
                entity.info,
                entity.importance,
                entity.flag,
                if (entity.deadline == null) null else Date(entity.deadline!!) ,
                Date(entity.createDate),
                Date(entity.editDate)
            )
        }
    }
}