package com.example.todolist.view.Main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.model.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        when (getSharedPreferences("theme", Context.MODE_PRIVATE).getInt("theme", -1)) {
            Utils.LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            Utils.DARK_THEME -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        }
        setContentView(binding.root)
    }
}