package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var adapter: RecyclerViewAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var repository: TodoItemsRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = TodoItemsRepository
        repository.numOfDone.observe(this, Observer {
            binding.textViewDone.text = it.toString()
        })
        binding.createTaskButton.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
        val tasks = repository.getTaskList()
        adapter = RecyclerViewAdapter(tasks)
        repository.adapter = adapter
        val manager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

    }
}