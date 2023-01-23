package com.example.todolist.view

import android.content.Intent
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


class ListFragment : Fragment() {
    lateinit var binding : FragmentListBinding
    var repository = TodoItemsRepository
    lateinit var adapter : RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater)
        repository.numOfDone.observe(viewLifecycleOwner, Observer {
           binding.textViewDone.text = it.toString()
       })

        val tasks = repository.getTaskList()
        adapter = RecyclerViewAdapter(tasks)
        repository.adapter = adapter
        val manager = LinearLayoutManager(context)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.createTaskButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .hide(this)
                .commit()
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.fragmentPlaceHolder, TaskFragment(this))
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
}