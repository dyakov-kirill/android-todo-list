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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentListBinding
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.Utils
import java.util.*


class EditTaskFragment(parentFragment: ListFragment, private val taskPosition: Int) : Fragment() {
    private lateinit var binding : FragmentTaskBinding
    private var repository = TodoItemsRepository
    private var listFragment = parentFragment
    private var task = repository.tasks[taskPosition]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(inflater)
        val tasks = repository.getTaskList()
        binding.buttonClose.setOnClickListener {
            closeFragment()
        }
        binding.buttonSave.setOnClickListener {
            task.info = binding.editTextInfo.text.toString()
            task.editDate = Calendar.getInstance().time
            repository.adapter.notifyItemChanged(taskPosition)
            closeFragment()
        }

        binding.switchDeadline.setOnCheckedChangeListener { button, checked ->


        }

        val spinnerArray: MutableList<String> = ArrayList()

        spinnerArray.add("Низкая")
        spinnerArray.add("Средняя")
        spinnerArray.add("Высокая")

        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray)

        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        val sItems: Spinner = binding.spinnerImportance
        sItems.adapter = adapter

        binding.spinnerImportance.setSelection(task.importance.value)
        binding.editTextInfo.setText(task.info)
        if (task.deadline != null) {
            binding.switchDeadline.isChecked = true
        }

        binding.imageButton2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red_dark))
        binding.textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_light))

        binding.deleteLayout.setOnClickListener {
            repository.tasks.removeAt(taskPosition)
            repository.adapter.notifyItemRemoved(taskPosition)
            closeFragment()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLog", "Destroyed edit fragment")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MyLog", "Paused edit fragment")
    }

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .show(listFragment).commit()
    }
}