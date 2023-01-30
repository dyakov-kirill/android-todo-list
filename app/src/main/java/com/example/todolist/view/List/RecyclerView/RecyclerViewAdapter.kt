package com.example.todolist.view.List.RecyclerView

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.model.TodoItem
import com.example.todolist.databinding.TaskCellBinding
import com.example.todolist.model.Utils
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter(private val listeners : ClickListeners) :
    RecyclerView.Adapter<RecyclerViewAdapter.TaskHolder>(),
    ItemTouchHelperAdapter {

    var dataSet = emptyList<TodoItem>()


    class TaskHolder(private val binding: TaskCellBinding,
                     private val listeners : ClickListeners,
                     private val dataSet: List<TodoItem>
    ) : RecyclerView.ViewHolder(binding.root) {
        var id = 0
        init {
            binding.buttonEdit.setOnClickListener {
                listeners.onOpenEditor(id)
            }

            binding.checkBox2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listeners.setItemFlag(id.toLong(), Utils.DONE)
                } else {
                    listeners.setItemFlag(id.toLong(), Utils.NOT_DONE)
                }
            }
        }

        fun bind(item: TodoItem) {
            binding.taskInfo.text = item.info
            if (item.deadline != null) {
                binding.textViewDeadline.text = String.format("Выполнить до " +
                        SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.deadline))
            } else {
                binding.textViewDeadline.text = ""
            }
            if (item.flag == Utils.DONE) {
                binding.checkBox2.isChecked = true
                Log.d("MyLog", "Set checked")
            } else {
                binding.checkBox2.isChecked = false
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskHolder {
        val view = TaskCellBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TaskHolder(view, listeners, dataSet)
    }

    override fun onBindViewHolder(taskHolder: TaskHolder, position: Int) {
        taskHolder.id = dataSet[position].id!!
        taskHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    fun setList(list: List<TodoItem>) {
        val diffUtil = DiffUtils(dataSet, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        dataSet = list
        diffResult.dispatchUpdatesTo(this)
    }

    interface ClickListeners {
        fun onOpenEditor(taskPos: Int)
        fun setItemFlag(task: Long, flag: Int)
        fun deleteItem(task: TodoItem)
    }

    override fun onItemDismiss(position: Int) {
        listeners.deleteItem(dataSet[position])
        notifyItemChanged(position)
    }


}