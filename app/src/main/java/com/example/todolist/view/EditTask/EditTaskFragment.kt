package com.example.todolist.view.EditTask

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.TodoItemEntity
import java.text.SimpleDateFormat
import java.util.*


class EditTaskFragment : Fragment() {
    private lateinit var binding : FragmentTaskBinding
    private var taskId : Long = 0
    private lateinit var viewModel : EditTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[EditTaskViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater)
        taskId = requireArguments().getInt("taskId").toLong()
        viewModel.getTask(taskId).observe(this.viewLifecycleOwner) {
            viewModel.task = TodoItemEntity.toModel(it)
            setSpinner()
            setDefaultValues(viewModel.task, savedInstanceState)
            setListeners()
        }
        return binding.root
    }

    private fun setDefaultValues(item: TodoItem, bundle: Bundle?) {
        if (bundle != null) {
            binding.spinnerImportance.setSelection(bundle.getInt("importance"))
            binding.editTextInfo.setText(bundle.getString("info"))
            binding.textViewDate.text = bundle.getString("date")
            binding.switchDeadline.isChecked = binding.textViewDate.text != ""
        } else {
            binding.spinnerImportance.setSelection(item.importance)
            binding.editTextInfo.setText(item.info)
            if (item.deadline != null) {
                binding.switchDeadline.isChecked = true
                binding.textViewDate.text =
                    String.format(SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.deadline!!.time))
            }
        }
        binding.imageButton2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red_dark))
        binding.textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_light))
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
            Navigation.findNavController(binding.root).navigate(R.id.action_editTaskFragment_to_listFragment)
        }

        binding.textViewDate.setOnClickListener {
            setDate()
        }

        binding.buttonSave.setOnClickListener {
            viewModel.updateTask(
                TodoItem(
                id = viewModel.task.id,
                info = binding.editTextInfo.text.toString(),
                importance = binding.spinnerImportance.selectedItemPosition,
                flag = viewModel.task.flag,
                if (binding.switchDeadline.isChecked) {
                    viewModel.deadline.time
                } else {
                    null
                }, createDate = viewModel.task.createDate,
                    editDate = Calendar.getInstance().time))
            Navigation.findNavController(binding.root).navigate(R.id.action_editTaskFragment_to_listFragment)
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setDate()
            } else {
                binding.textViewDate.text = ""
            }
        }
        binding.deleteLayout.setOnClickListener {
            viewModel.deleteTask(viewModel.task)
            Navigation.findNavController(binding.root).navigate(R.id.action_editTaskFragment_to_listFragment)
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("info", binding.editTextInfo.text.toString())
        outState.putInt("importance", binding.spinnerImportance.selectedItemPosition)
        outState.putString("date", binding.textViewDate.text.toString())
        super.onSaveInstanceState(outState)
    }
}