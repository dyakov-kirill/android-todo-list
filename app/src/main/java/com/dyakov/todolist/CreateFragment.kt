package com.dyakov.todolist

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.dyakov.todolist.databinding.FragmentEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class CreateFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private var deadline = Calendar.getInstance()
    private var isDeadlineSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            R.layout.spinner_item_selected
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.prioritySpinner.adapter = adapter
        }
        binding.switchDeadline.setOnCheckedChangeListener { _, b ->
            if (b) {
                val dialog = DatePickerDialog(
                    requireContext(), { _, y, m, d ->
                        deadline.set(y, m, d)
                        isDeadlineSet = true
                        binding.textCurrentDeadline.text = String.format(resources.getString(R.string.date_pattern),
                            d, resources.getStringArray(R.array.month_array)[m], y
                        )
                        binding.textCurrentDeadline.visibility = View.VISIBLE
                    },
                    deadline.get(Calendar.YEAR),
                    deadline.get(Calendar.MONTH),
                    deadline.get(Calendar.DAY_OF_MONTH)
                )
                dialog.setOnCancelListener { binding.switchDeadline.isChecked = false }
                dialog.show()
            } else {
                deadline = Calendar.getInstance()
                binding.textCurrentDeadline.visibility = View.INVISIBLE
                isDeadlineSet = false
            }
        }
        binding.textSave.setOnClickListener {
            addTask()
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.buttonClose.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addTask() {
        val item = TodoItem(Date(System.currentTimeMillis()).toString(),
            binding.editTask.text.toString(),
        Priority.values()[binding.prioritySpinner.selectedItemPosition],
        false,
            if (isDeadlineSet != false) Date(deadline.timeInMillis) else null,
            Date(System.currentTimeMillis()),
            null
        )
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(requireContext()).getTaskDao().addTask(item)
        }
    }
}

