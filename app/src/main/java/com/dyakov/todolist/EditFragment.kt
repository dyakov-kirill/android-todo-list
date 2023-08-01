package com.dyakov.todolist

import android.app.DatePickerDialog
import android.content.res.Resources.Theme
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dyakov.todolist.databinding.FragmentEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class EditFragment : Fragment() {
    private val args: EditFragmentArgs by navArgs()

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private var deadline = Calendar.getInstance()
    private var isDeadlineSet = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.editTask.setText(args.task.description)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            R.layout.spinner_item_selected
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.prioritySpinner.adapter = adapter
        }
        binding.prioritySpinner.setSelection(args.task.priority.priority)
        if (args.task.deadline != null) {
            binding.switchDeadline.isChecked = true
            deadline.time = args.task.deadline!!
            binding.textCurrentDeadline.setText(String.format(resources.getString(R.string.date_pattern),
                deadline.get(Calendar.DAY_OF_MONTH),
                resources.getStringArray(R.array.month_array)[deadline.get(Calendar.MONTH)],
                deadline.get(Calendar.YEAR)
            ))
            binding.textCurrentDeadline.visibility = View.VISIBLE
        }
        binding.switchDeadline.setOnCheckedChangeListener { _, b ->
            if (b) {
                val dialog = DatePickerDialog(
                    requireContext(), { _, y, m, d ->
                        deadline.set(y, m, d)
                        isDeadlineSet = true
                        binding.textCurrentDeadline.setText(String.format(resources.getString(R.string.date_pattern),
                            d, resources.getStringArray(R.array.month_array)[m], y
                        ))
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
        binding.textDelete.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.textDelete.compoundDrawables[0].setTint(ContextCompat.getColor(requireContext(), R.color.red))
        binding.textSave.setOnClickListener {
            updateTask()
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.buttonClose.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.textDelete.setOnClickListener {
            deleteTask()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateTask() {
        val item = TodoItem(args.task.id,
            binding.editTask.text.toString(),
            Priority.values()[binding.prioritySpinner.selectedItemPosition],
            args.task.isDone,
            if (isDeadlineSet) Date(deadline.timeInMillis) else null,
            args.task.creationTime,
            Date(System.currentTimeMillis())
        )
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(requireContext()).getTaskDao().updateTask(item)
        }
    }

    private fun deleteTask() {
        lifecycleScope.launch(Dispatchers.IO) {
            Database.getInstance(requireContext()).getTaskDao().deleteTask(args.task)
        }
        Navigation.findNavController(binding.root).navigateUp()
    }
}