package com.example.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.TaskCellBinding

class RecyclerViewAdapter(private val dataSet: List<TodoItem>) :
    RecyclerView.Adapter<RecyclerViewAdapter.TaskHolder>() {

    class TaskHolder(private val binding: TaskCellBinding, ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // Define click listener for the ViewHolder's View
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
        return TaskHolder(view)
    }

    override fun onBindViewHolder(taskHolder: TaskHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        taskHolder.bind(dataSet[position])
        Log.d("MyLog", "Task binded")

    }

    override fun getItemCount() = dataSet.size
}