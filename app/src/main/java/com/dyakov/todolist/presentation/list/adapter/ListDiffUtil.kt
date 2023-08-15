package com.dyakov.todolist.presentation.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dyakov.todolist.domain.models.TodoItem

class ListDiffUtil(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].description != newList[newItemPosition].description -> {
                false
            }
            oldList[oldItemPosition].isDone != newList[newItemPosition].isDone -> {
                false
            }
            oldList[oldItemPosition].priority != newList[newItemPosition].priority -> {
                false
            }
            oldList[oldItemPosition].deadline != newList[newItemPosition].deadline -> {
                false
            }
            else -> true
        }
    }
}