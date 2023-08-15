package com.dyakov.todolist.presentation.list.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dyakov.todolist.R
import com.dyakov.todolist.databinding.ButtonAddNewBinding
import com.dyakov.todolist.databinding.FragmentTaskBinding
import com.dyakov.todolist.domain.models.Priority
import com.dyakov.todolist.domain.models.TodoItem
import com.dyakov.todolist.presentation.list.ListFragmentDirections
import java.util.Calendar


/**
 * The adapter for ListFragment list
 */
class RecyclerViewAdapter(val callbacks: Callbacks) : RecyclerView.Adapter<ViewHolder>() {
    var list: List<TodoItem> = emptyList()
        set(newList) {
            val diffUtil = ListDiffUtil(list, newList)
            val diffResult = DiffUtil.calculateDiff(diffUtil)
            field = newList
            diffResult.dispatchUpdatesTo(this)
        }

    inner class TaskHolder(private val binding: FragmentTaskBinding) : ViewHolder(binding.root) {
        lateinit var holdingItem: TodoItem
        fun bind(item: TodoItem) = with(binding) {
            holdingItem = item
            checkboxTaskInfo.isChecked = item.isDone
            if (item.deadline == null) {
                textDeadline.visibility = View.GONE
                textDeadline.text = ""
            } else {
                val c = Calendar.getInstance().apply { time = item.deadline }
                textDeadline.text = String
                    .format(
                        binding.root.context.resources.getString(R.string.date_pattern),
                        c.get(Calendar.DAY_OF_MONTH),
                        root.resources.getStringArray(R.array.month_array)[c.get(Calendar.MONTH)],
                        c.get(Calendar.YEAR)
                    )
                textDeadline.visibility = View.VISIBLE
            }
            buttonInfo.setOnClickListener {
                callbacks.deleteTask(holdingItem)
            }
            val spannable: Spannable = SpannableString(String.format(root.resources
                .getStringArray(R.array.priority_patterns_array)[holdingItem.priority.priority], holdingItem.description))

            when (holdingItem.priority) {
                Priority.HIGH -> {
                    spannable.setSpan(ForegroundColorSpan(Color.RED), 0, 2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                Priority.LOW -> {
                    spannable.setSpan(ForegroundColorSpan(Color.GRAY), 0, 2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else -> {}
            }
            textView.setText(spannable, TextView.BufferType.SPANNABLE)
            buttonInfo.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToEditFragment(holdingItem)
                Navigation.findNavController(root).navigate(action)
            }
            checkboxTaskInfo.setOnCheckedChangeListener { _, c ->
                callbacks.updateTask(holdingItem.copy(isDone = c))
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
        callbacks.updateTask(item.copy(isDone = !item.isDone))
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
