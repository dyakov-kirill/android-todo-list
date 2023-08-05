package com.dyakov.todolist.ui.list.utils

import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpannable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dyakov.todolist.R
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.databinding.ButtonAddNewBinding
import com.dyakov.todolist.databinding.FragmentTaskBinding
import com.dyakov.todolist.ui.list.ListFragmentDirections
import java.util.*

class RecyclerViewAdapter(val callbacks: Callbacks) : RecyclerView.Adapter<ViewHolder>() {

    private var list: List<TodoItem> = emptyList()

    fun setNewList(newList: List<TodoItem>) {
        val diffUtil = ListDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class TaskHolder(val binding: FragmentTaskBinding) : ViewHolder(binding.root) {
        lateinit var holdingItem: TodoItem
        fun bind(item: TodoItem) {
            holdingItem = item
            binding.checkboxTaskInfo.isChecked = item.isDone
            if (item.deadline == null) {
                binding.textDeadline.visibility = View.GONE
                binding.textDeadline.text = ""
            } else {
                val c = Calendar.getInstance()
                c.time = item.deadline
                binding.textDeadline.text = String
                    .format(binding.root.context.resources.getString(R.string.date_pattern),
                        c.get(Calendar.DAY_OF_MONTH),
                        binding.root.resources.getStringArray(R.array.month_array)[c.get(Calendar.MONTH)],
                        c.get(Calendar.YEAR))
                binding.textDeadline.visibility = View.VISIBLE
            }
            binding.buttonInfo.setOnClickListener {
                callbacks.deleteTask(holdingItem)
            }

            binding.textView.text = String
                .format(binding.root.resources.getStringArray(R.array.priority_patterns_array)[item.priority.priority],
                item.description).toSpannable()

            binding.root.setOnClickListener {
                binding.root.animate().scaleX(1.1F).scaleY(1.1F).start()
            }
            binding.buttonInfo.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToEditFragment(holdingItem)
                Navigation.findNavController(binding.root).navigate(action)
            }
            binding.checkboxTaskInfo.setOnCheckedChangeListener { _, c ->
                if (c) callbacks.updateTask(holdingItem.copy(isDone = true))
                else callbacks.updateTask(holdingItem.copy(isDone = false))
            }
        }
    }

    inner class ButtonHolder(binding: ButtonAddNewBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_TASK) {
            val binding = FragmentTaskBinding.inflate(inflater, parent, false)
            TaskHolder(binding)
        } else {
            val binding = ButtonAddNewBinding.inflate(inflater, parent, false)
            binding.textNewTask.setOnClickListener {
                Navigation.findNavController(binding.root).navigate(R.id.action_listFragment_to_createFragment)
            }
            ButtonHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) VIEW_TYPE_BUTTON else VIEW_TYPE_TASK
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TaskHolder) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount() = list.size + 1

    fun onItemDismiss(item: TodoItem) {
        callbacks.deleteTask(item)
    }

    fun onItemChecked(item: TodoItem) {
        callbacks.updateTask(item.copy(isDone = true))
    }

    companion object {
        const val VIEW_TYPE_TASK = 0
        const val VIEW_TYPE_BUTTON = 1
    }

    interface Callbacks {
        fun deleteTask(item: TodoItem)
        fun updateTask(item: TodoItem)
    }
}