package com.example.todolist.view.List

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.TodoItemEntity
import com.example.todolist.model.Utils
import com.example.todolist.view.CreateTask.CreateTaskFragment
import com.example.todolist.view.EditTask.EditTaskFragment
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
            closeFragment()
            openCreator()
        }
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .commit()

    }

    private fun openCreator() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.fragmentPlaceHolder, CreateTaskFragment(this))
            .commit()
    }

    override fun onOpenEditor(taskPos: Int) {
        closeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.fragmentPlaceHolder, EditTaskFragment(this, taskPos.toLong()))
            .commit()
    }

    override fun setItemFlag(taskId: Long, flag: Int) {
        viewModel.setTaskFlag(taskId, flag)
    }

    override fun deleteItem(task: TodoItem) {
        viewModel.deleteTask(task)
    }


}