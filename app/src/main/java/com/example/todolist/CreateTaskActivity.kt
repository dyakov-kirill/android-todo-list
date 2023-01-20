package com.example.todolist

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist.databinding.ActivityCreateTaskBinding
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class CreateTaskActivity() : AppCompatActivity() {
    lateinit var binding: ActivityCreateTaskBinding
    val repository = TodoItemsRepository
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spinnerArray: MutableList<String> = ArrayList()

        spinnerArray.add("Низкая")
        spinnerArray.add("Средняя")
        spinnerArray.add("Высокая")

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, spinnerArray)

        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        val sItems: Spinner = binding.spinnerImportance
        sItems.adapter = adapter

        binding.buttonClose.setOnClickListener {
            finish()
        }
        binding.buttonSave.setOnClickListener {
            repository.addTask(TodoItem(TodoItemsRepository.tasks.size.toString(), binding.editTextInfo.text.toString(), Utils.Importance.fromInt(binding.spinnerImportance.selectedItemPosition),
            Utils.Flag.NOT_DONE, c.time,
                Calendar.getInstance().time, Calendar.getInstance().time))
            finish()
        }

        binding.switchDeadline.setOnCheckedChangeListener { button, checked ->
            if (checked) {
                val listener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, month)
                    c.set(Calendar.DAY_OF_MONTH, day)
                }
                DatePickerDialog(this, listener,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)).show()

            }
        }


    }
}