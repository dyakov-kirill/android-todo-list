package com.example.todolist.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.Utils
import java.util.*

class TaskFragment(parentFragment: ListFragment) : Fragment() {
    lateinit var binding : FragmentTaskBinding

    var repository = TodoItemsRepository
    var listFragment = parentFragment

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater)
        binding.buttonClose.setOnClickListener {
            closeFragment()
        }
        val spinnerArray: MutableList<String> = ArrayList()

        spinnerArray.add("Низкая")
        spinnerArray.add("Средняя")
        spinnerArray.add("Высокая")

        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray)

        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        val sItems: Spinner = binding.spinnerImportance
        sItems.adapter = adapter


        binding.buttonSave.setOnClickListener {
            repository.addTask(
                TodoItem(
                    TodoItemsRepository.tasks.size.toString(), binding.editTextInfo.text.toString(), Utils.Importance.fromInt(binding.spinnerImportance.selectedItemPosition),
                    Utils.Flag.NOT_DONE, c.time,
                    Calendar.getInstance().time, Calendar.getInstance().time)
            )
            closeFragment()
        }

        binding.switchDeadline.setOnCheckedChangeListener { button, checked ->
            if (checked) {
                val listener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, month)
                    c.set(Calendar.DAY_OF_MONTH, day)
                }
                DatePickerDialog(requireContext(), listener,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)).show()

            }
            closeFragment()
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLog", "Destroyed task fragment")
    }

    fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .show(listFragment).commit()
    }
}