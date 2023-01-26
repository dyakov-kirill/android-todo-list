package com.example.todolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import com.example.todolist.databinding.TaskCellBinding
import com.example.todolist.model.Utils
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewAdapter(private val dataSet: List<TodoItem>,
                          private val listeners : ClickListeners) :
    RecyclerView.Adapter<RecyclerViewAdapter.TaskHolder>() {


    class TaskHolder(private val binding: TaskCellBinding,
                     private val listeners : ClickListeners
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.button2.setOnClickListener {
                listeners.onOpenEditor(adapterPosition)
            }

            binding.checkBox2.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val a = TodoItemsRepository.numOfDone.value
                    if (a != null) {
                        TodoItemsRepository.numOfDone.value = a + 1
                    }

                } else {
                    val a = TodoItemsRepository.numOfDone.value
                    if (a != null) {
                        TodoItemsRepository.numOfDone.value = a - 1
                    }
                }
            }
        }

        fun bind(item: TodoItem) {
            binding.taskInfo.text = item.info
            if (item.deadline != null) {
                binding.textViewDeadline.text = String.format("Выполнить до " +
                        SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.deadline))
            }
            if (item.flag == Utils.Flag.DONE) {
                binding.checkBox2.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskHolder {
        val view = TaskCellBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return TaskHolder(view, listeners)
    }

    override fun onBindViewHolder(taskHolder: TaskHolder, position: Int) {
        taskHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    interface ClickListeners {
        fun onOpenEditor(taskPos: Int)
    }
}