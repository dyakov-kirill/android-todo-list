package com.example.todolist.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.model.TodoItem
import com.example.todolist.databinding.TaskCellBinding

class RecyclerViewAdapter(private val dataSet: List<TodoItem>,
                          val listenerOpen: (taskPos: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerViewAdapter.TaskHolder>() {

    class TaskHolder(private val binding: TaskCellBinding,
                     val listenerOpen: (taskPos: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {

            binding.button2.setOnClickListener {
                listenerOpen(adapterPosition)
            }

            binding.checkBox2.setOnCheckedChangeListener { button, isChecked ->
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
            Log.d("MyLog", "Holder created")
        }

        fun bind(item: TodoItem) {
            binding.checkBox2.text = item.info

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskHolder {
        // Create a new view, which defines the UI of the list item
        val view = TaskCellBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        Log.d("MyLog", "Holder created")
        return TaskHolder(view, listenerOpen)
    }

    override fun onBindViewHolder(taskHolder: TaskHolder, position: Int) {
        taskHolder.bind(dataSet[position])
        Log.d("MyLog", "Task binded")

    }

    override fun getItemCount() = dataSet.size
}