package com.example.todolist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.viewmodel.ListViewModel


class ListFragment : Fragment(),
    RecyclerViewAdapter.ClickListeners,
    CreateTaskFragment.ClickListeners,
    EditTaskFragment.ClickListeners {

    private lateinit var binding : FragmentListBinding
    private lateinit var adapter : RecyclerViewAdapter
    private lateinit var viewModel : ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater)
        adapter = RecyclerViewAdapter(viewModel.repository.getTaskList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.adapter = adapter

        setListeners()
        return binding.root
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
            .add(R.id.fragmentPlaceHolder, CreateTaskFragment(this, this))
            .commit()
    }

    override fun onOpenEditor(taskPos: Int) {
        closeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.fragmentPlaceHolder, EditTaskFragment(this, taskPos, this))
            .commit()
    }

    override fun onCreateTask(task: TodoItem) {
        viewModel.addTask(task)
        adapter.notifyItemInserted(viewModel.repository.tasks.size - 1)
    }

    override fun onTaskEdited(taskPosition: Int) {
        adapter.notifyItemChanged(taskPosition)
    }

    override fun onTaskDeleted(taskPosition: Int) {
        adapter.notifyItemRemoved(taskPosition)
    }


}