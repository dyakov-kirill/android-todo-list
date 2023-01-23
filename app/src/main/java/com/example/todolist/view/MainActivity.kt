package com.example.todolist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var adapter: RecyclerViewAdapter
    lateinit var binding: ActivityMainBinding
    lateinit var repository: TodoItemsRepository
    lateinit var listFragment: ListFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        repository = TodoItemsRepository

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragmentPlaceHolder, listFragment, "listFragment").commit()


    }
}