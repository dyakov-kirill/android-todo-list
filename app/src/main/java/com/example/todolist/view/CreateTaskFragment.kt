package com.example.todolist.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.R
import com.example.todolist.data.TodoItemsRepository
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.Utils
import com.example.todolist.viewmodel.CreateTaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskFragment(private val listFragment: ListFragment,
                         private val listeners: ClickListeners) : Fragment() {
    private lateinit var binding : FragmentTaskBinding
    private lateinit var viewModel: CreateTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater)

        setSpinner()
        setListeners()

        return binding.root
    }

    private fun setSpinner() {
        val spinnerArray: MutableList<String> = ArrayList()

        spinnerArray.add(resources.getString(R.string.low_importance))
        spinnerArray.add(resources.getString(R.string.medium_importance))
        spinnerArray.add(resources.getString(R.string.high_importance))

        val adapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerArray)
        val sItems: Spinner = binding.spinnerImportance

        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        sItems.adapter = adapter
    }

    private fun setListeners() {
        binding.buttonClose.setOnClickListener {
            closeFragment()
        }

        binding.textViewDate.setOnClickListener {
            setDate()
        }

        binding.buttonSave.setOnClickListener {
            listeners.onCreateTask(TodoItem(
                TodoItemsRepository.tasks.size.toString(),
                binding.editTextInfo.text.toString(),
                Utils.Importance.fromInt(binding.spinnerImportance.selectedItemPosition),
                Utils.Flag.NOT_DONE,
                if (binding.switchDeadline.isChecked) viewModel.deadline.time else null,
                viewModel.currentTime.time, viewModel.currentTime.time))
            closeFragment()
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setDate()
            }
        }
    }

    private fun setDate() {
        DatePickerDialog(requireContext(), { _, year, month, day ->
            viewModel.setDeadline(day, month, year)
            binding.textViewDate.text =
                String.format(SimpleDateFormat("dd.MM.yyyy", Locale.US).format(viewModel.deadline.time))
        },
            viewModel.currentTime.get(Calendar.YEAR),
            viewModel.currentTime.get(Calendar.MONTH),
            viewModel.currentTime.get(Calendar.DAY_OF_MONTH)
        ).show()

    }

    private fun closeFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .remove(this).commit()
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .show(listFragment).commit()
    }

    interface ClickListeners {
        fun onCreateTask(task: TodoItem)
    }
}