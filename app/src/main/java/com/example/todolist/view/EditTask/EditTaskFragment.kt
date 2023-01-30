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
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTaskBinding
import com.example.todolist.model.TodoItem
import com.example.todolist.model.TodoItemEntity
import com.example.todolist.model.Utils
import com.example.todolist.view.List.ListFragment
import java.text.SimpleDateFormat
import java.util.*


class EditTaskFragment(private val listFragment: ListFragment, private val taskId: Long) : Fragment() {
    private lateinit var binding : FragmentTaskBinding

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
        viewModel.getTask(taskId).observe(this.viewLifecycleOwner) {
            viewModel.task = TodoItemEntity.toModel(it)
            setSpinner()
            setDefaultValues(viewModel.task)
            setListeners()
        }
        return binding.root
    }

    private fun setDefaultValues(item: TodoItem) {
        binding.spinnerImportance.setSelection(item.importance)
        binding.editTextInfo.setText(item.info)
        if (item.deadline != null) {
            binding.switchDeadline.isChecked = true
            binding.textViewDate.text =
                String.format(SimpleDateFormat("dd.MM.yyyy", Locale.US).format(item.deadline!!.time))
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
            closeFragment()
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
            closeFragment()
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setDate()
            } else {
                binding.textViewDate.text = ""
            }
        }
        binding.deleteLayout.setOnClickListener {
            if (viewModel.task.flag == Utils.DONE){
                val a = viewModel.repository.numOfDone.value
                if (a != null) {
                    viewModel.repository.numOfDone.value = a - 1
                }
            }
            viewModel.deleteTask(viewModel.task)
            closeFragment()
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


}