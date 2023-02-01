package com.example.todolist.view.List

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.TodoItemEntity
import com.example.todolist.model.Utils
import com.example.todolist.view.List.RecyclerView.RecyclerViewAdapter
import com.example.todolist.view.List.RecyclerView.SwipeToDeleteCallback


class ListFragment : Fragment(),
    RecyclerViewAdapter.ClickListeners {

    private lateinit var binding : FragmentListBinding
    private lateinit var adapter : RecyclerViewAdapter
    private lateinit var viewModel : ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ListViewModel::class.java]
        viewModel.initDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)

        initRecyclerView()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        viewModel.getAllTasks().observe(this.viewLifecycleOwner) {
            adapter.setList(it.map { entity ->
                TodoItemEntity.toModel(entity)
            })
            viewModel.repository.numOfDone.value = 0
            it.forEach { item ->
                if (item.flag == Utils.DONE) {
                    val temp = TodoItemsRepository.numOfDone.value
                    if (temp != null) {
                        TodoItemsRepository.numOfDone.value = temp + 1
                    }
                }
            }
            setListeners()
        }
    }

    private fun initRecyclerView() {
        adapter = RecyclerViewAdapter(this)
        binding.recyclerView.adapter = adapter
        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false
        val touchHelperCallback = SwipeToDeleteCallback(adapter)
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setListeners() {
        viewModel.repository.numOfDone.observe(viewLifecycleOwner) {
            binding.textViewDone.text =
                String.format(resources.getString(R.string.done), it)
        }

        binding.createTaskButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_listFragment_to_createTaskFragment)
        }
        registerForContextMenu(binding.buttonSetTheme)

        binding.buttonSetTheme.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add(0, 0, 0, "Системная тема")
            contextMenu.add(1, 1, 1, "Светлая тема")
            contextMenu.add(2, 2, 2, "Темная тема")
        }

        binding.buttonSetTheme.setOnClickListener {
            requireActivity().openContextMenu(it)
        }
    }


    override fun onOpenEditor(taskPos: Int) {
        val bundle = Bundle()
        bundle.putInt("taskId", taskPos)
        Navigation.findNavController(binding.root).navigate(R.id.action_listFragment_to_editTaskFragment,
            bundle)
    }

    override fun setItemFlag(task: Long, flag: Int) {
        viewModel.setTaskFlag(task, flag)
    }

    override fun deleteItem(task: TodoItem) {
        viewModel.deleteTask(task)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            Utils.SYSTEM_THEME -> {
                val sharedPreference =  requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE)
                with (sharedPreference.edit()) {
                    putInt("theme", Utils.SYSTEM_THEME).commit()
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
            }
            Utils.LIGHT_THEME -> {
                val sharedPreference =  requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE)
                with (sharedPreference.edit()) {
                    putInt("theme", Utils.LIGHT_THEME).commit()
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Utils.DARK_THEME -> {
                val sharedPreference =  requireActivity().getSharedPreferences("theme", Context.MODE_PRIVATE)
                with (sharedPreference.edit()) {
                    putInt("theme", Utils.DARK_THEME).commit()
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
        return super.onContextItemSelected(item)
    }
}