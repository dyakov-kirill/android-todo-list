package com.example.todolist.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.model.TodoItem


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    private var repository = TodoItemsRepository
    private lateinit var adapter : RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        val tasks = repository.getTaskList()
        adapter = RecyclerViewAdapter(tasks, ::openEditor)
        repository.adapter = adapter

        repository.numOfDone.observe(viewLifecycleOwner, Observer {
           binding.textViewDone.text = String.format(resources.getString(R.string.done), it)//it.toString()
       })

        val manager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        binding.createTaskButton.setOnClickListener {
            closeFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragmentPlaceHolder, CreateTaskFragment(this))
                .commit()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLog", "Destroyed list fragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyLog", "Paused list fragment")
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .hide(this)
            .commit()

    }

    fun openEditor(task: Int) {
        closeFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.fragmentPlaceHolder, EditTaskFragment(this, task))
            .commit()
    }


}