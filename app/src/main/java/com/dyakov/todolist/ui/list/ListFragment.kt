package com.dyakov.todolist.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dyakov.todolist.R
import com.dyakov.todolist.TodoItem
import com.dyakov.todolist.collectOnLifecycle
import com.dyakov.todolist.databinding.FragmentListBinding
import com.dyakov.todolist.ui.list.adapter.RecyclerViewAdapter
import com.dyakov.todolist.ui.list.adapter.TaskTouchHelper
import com.dyakov.todolist.ui.list.adapter.TodoItemDecoration
import com.dyakov.todolist.ui.list.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * The screen that displays the created tasks and the buttons for creating a task
 */
@AndroidEntryPoint
class ListFragment : Fragment(), RecyclerViewAdapter.Callbacks {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val recyclerViewAdapter = RecyclerViewAdapter(this)
    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState.collectOnLifecycle(this) {
            renderData(it.data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        initRecyclerView()
        initListeners()
        return binding.root
    }

    private fun initRecyclerView() = with(binding.recyclerView) {
        adapter = recyclerViewAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(TodoItemDecoration(bottomOffset = 24))
        ItemTouchHelper(TaskTouchHelper(recyclerViewAdapter)).attachToRecyclerView(this)
    }

    private fun initListeners() = with(binding) {
        fabAdd.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_listFragment_to_createFragment)
        }
        buttonHide.setOnClickListener {
            viewModel.changeHideState()
        }
    }

    private fun renderData(list: List<TodoItem>) {
        recyclerViewAdapter.list = list
        var numOfDone = 0
        list.forEach { item -> if (item.isDone) numOfDone++ }
        binding.textDone.text = String.format(resources.getString(R.string.number_of_done), numOfDone)
        if (!viewModel.uiState.value.isDoneHidden) {
            setHideButtonDrawable(R.drawable.ic_visibility_on)
        } else {
            setHideButtonDrawable(R.drawable.ic_visibility_off)
        }
    }

    private fun setHideButtonDrawable(@DrawableRes drawable: Int) = with(binding) {
        buttonHide.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Adapter callbacks */
    override fun deleteTask(item: TodoItem) {
        viewModel.deleteTask(item)
    }

    override fun updateTask(item: TodoItem) {
        viewModel.updateTask(item)
    }
}
