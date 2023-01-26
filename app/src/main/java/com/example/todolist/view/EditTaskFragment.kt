package com.example.todolist.view

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
import com.example.todolist.model.Utils
import com.example.todolist.viewmodel.EditTaskViewModel
import java.text.SimpleDateFormat
import java.util.*


class EditTaskFragment(private val listFragment: ListFragment, private val taskPosition: Int, private val listeners: ClickListeners) : Fragment() {
    private lateinit var binding : FragmentTaskBinding

    private lateinit var viewModel : EditTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EditTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater)
        viewModel.task = viewModel.repository.tasks[taskPosition]
        setDefaultValues()
        setListeners()
        setSpinner()


        return binding.root
    }

    private fun setDefaultValues() {
        binding.spinnerImportance.setSelection(viewModel.task.importance.value)
        binding.editTextInfo.setText(viewModel.task.info)
        if (viewModel.task.deadline != null) {
            binding.switchDeadline.isChecked = true
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
            viewModel.task.info = binding.editTextInfo.text.toString()
            viewModel.task.editDate = Calendar.getInstance().time
            if (binding.switchDeadline.isChecked) {
                viewModel.task.deadline = viewModel.deadline.time
            } else {
                viewModel.task.deadline = null
            }
            listeners.onTaskEdited(taskPosition)
            closeFragment()
        }

        binding.switchDeadline.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                setDate()
            }
        }
        binding.deleteLayout.setOnClickListener {
            if (viewModel.task.flag == Utils.Flag.DONE){
                val a = viewModel.repository.numOfDone.value
                if (a != null) {
                    viewModel.repository.numOfDone.value = a - 1
                }
            }
            viewModel.repository.tasks.removeAt(taskPosition)
            listeners.onTaskDeleted(taskPosition)
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

    interface ClickListeners {
        fun onTaskEdited(taskPosition: Int)
        fun onTaskDeleted(taskPosition: Int)
    }

}