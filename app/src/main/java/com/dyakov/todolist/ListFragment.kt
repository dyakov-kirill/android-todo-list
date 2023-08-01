package com.dyakov.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.dyakov.todolist.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListFragment : Fragment(), RecyclerViewAdapter.Callbacks {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var data: Flow<List<TodoItem>>
    private var hideDoneTasks = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val adapter = RecyclerViewAdapter(this)
        data = Database.getInstance(requireContext()).getTaskDao().getAllTasks()
        lifecycleScope.launch(Dispatchers.IO) {
          data.collect {
               withContext(Dispatchers.Main) {
                   adapter.setNewList(it)
                   var numOfDone = 0
                   it.forEach { item -> if (item.isDone) numOfDone++ }
                   binding.textDone.text = String.format(resources.getString(R.string.number_of_done), numOfDone)
              }
           }
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(TodoItemDecoration(bottomOffset = 24))
        ItemTouchHelper(TaskTouchHelper(adapter)).attachToRecyclerView(binding.recyclerView)
        binding.fabAdd.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_listFragment_to_createFragment)
        }
        binding.buttonHide.setOnClickListener {
            hideDoneTasks = !hideDoneTasks
            if (!hideDoneTasks) {
                binding.buttonHide.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_visibility_on))
                data = Database.getInstance(requireContext()).getTaskDao().getAllTasks()
                lifecycleScope.launch(Dispatchers.IO) {
                    data.collect {
                        withContext(Dispatchers.Main) {
                            adapter.setNewList(it)
                            var numOfDone = 0
                            it.forEach { item -> if (item.isDone) numOfDone++ }
                            binding.textDone.text = String.format(resources.getString(R.string.number_of_done), numOfDone)
                        }
                    }
                }
            } else {
                binding.buttonHide.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_visibility_off))
                data = Database.getInstance(requireContext()).getTaskDao().getAllTasks2()
                lifecycleScope.launch(Dispatchers.IO) {
                    data.collect {
                        withContext(Dispatchers.Main) {
                            adapter.setNewList(it)
                            var numOfDone = 0
                            it.forEach { item -> if (item.isDone) numOfDone++ }
                            binding.textDone.text = String.format(resources.getString(R.string.number_of_done), numOfDone)
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun addTask(item: TodoItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(this@ListFragment.requireContext()).getTaskDao().addTask(item)
        }
    }

    override fun deleteTask(item: TodoItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(requireContext()).getTaskDao().deleteTask(item)
        }
    }

    override fun updateTask(item: TodoItem) {
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(requireContext()).getTaskDao().updateTask(item)
        }
    }

}