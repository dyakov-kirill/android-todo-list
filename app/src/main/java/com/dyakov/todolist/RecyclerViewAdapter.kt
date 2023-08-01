package com.dyakov.todolist

import android.opengl.Visibility
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dyakov.todolist.databinding.ButtonAddNewBinding
import com.dyakov.todolist.databinding.FragmentTaskBinding
import java.util.*

class RecyclerViewAdapter(val callbacks: Callbacks) : RecyclerView.Adapter<ViewHolder>() {

    var list: List<TodoItem> = emptyList()
//        set(newList) {
//            val diffUtil = ListDiffUtil(newList, field)
//            val diffResult = DiffUtil.calculateDiff(diffUtil)
//            field = newList
//          //  notifyDataSetChanged()
//            diffResult.dispatchUpdatesTo(this)
//        }

    fun setNewList(newList: List<TodoItem>) {
        val diffUtil = ListDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList
      //  notifyDataSetChanged()
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
            binding.checkboxTaskInfo.text = item.description
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

    companion object {
        const val VIEW_TYPE_TASK = 0
        const val VIEW_TYPE_BUTTON = 1
    }

    interface Callbacks {
        fun addTask(item: TodoItem)
        fun deleteTask(item: TodoItem)
        fun updateTask(item: TodoItem)
    }
}