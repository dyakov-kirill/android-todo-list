package com.example.todolist.view.List.RecyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.todolist.model.TodoItem

class DiffUtils(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].info != newList[newItemPosition].info -> {
                false
            }
            oldList[oldItemPosition].flag != newList[newItemPosition].flag -> {
                false
            }
            oldList[oldItemPosition].importance != newList[newItemPosition].importance -> {
                false
            }
            oldList[oldItemPosition].deadline != newList[newItemPosition].deadline -> {
                false
            }
            oldList[oldItemPosition].createDate != newList[newItemPosition].createDate -> {
                false
            }
            oldList[oldItemPosition].editDate != newList[newItemPosition].editDate -> {
                false
            }
            else -> {
                true
            }
        }
    }
}