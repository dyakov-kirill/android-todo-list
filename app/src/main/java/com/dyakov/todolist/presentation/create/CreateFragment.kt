package com.dyakov.todolist.presentation.create

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dyakov.todolist.*
import com.dyakov.todolist.databinding.FragmentEditBinding
import com.dyakov.todolist.domain.models.Priority
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * A fragment which creates new task
 */
@AndroidEntryPoint
class CreateFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState.collectOnLifecycle(this) {
            setFragmentState(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        initSpinner()
        setListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setListeners() = with(binding) {
        editTask.doOnTextChanged { text, _, _, _ ->
            viewModel.updateDescription(text.toString())
        }
        switchDeadline.setOnClickListener {
            when(switchDeadline.isChecked) {
                true -> createDatePickerDialog()
                false -> viewModel.removeDeadline()
            }
        }
        prioritySpinner.onItemSelectedListener = getSpinnerListener()
        textSave.setOnClickListener {
            viewModel.saveTask()
            Navigation.findNavController(binding.root).navigateUp()
        }
        binding.buttonClose.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.priority_array,
            R.layout.spinner_item_selected
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            binding.prioritySpinner.adapter = adapter
        }
    }
    private fun getSpinnerListener() = object : OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.updatePriority(Priority.values()[position])
        }
    }

    private fun setFragmentState(state: CreateUiState) = with(binding) {
        if (state.description != editTask.text.toString()) {
            editTask.setText(state.description)
            editTask.setSelection(state.description.length - 1)
        }
        prioritySpinner.setSelection(state.priority.priority)
        if (state.deadline != null) {
            showDeadline(state.deadline)
        } else {
            hideDeadline()
        }
    }

    private fun showDeadline(deadline: Date) {
        val c = Calendar.getInstance()
        c.time = deadline
        binding.textCurrentDeadline.text = String.format(
            resources.getString(
                R.string.date_pattern
            ),
            c.get(Calendar.DAY_OF_MONTH),
            resources.getStringArray(R.array.month_array)[c.get(Calendar.MONTH)],
            c.get(Calendar.YEAR)
        )
        binding.textCurrentDeadline.visibility = View.VISIBLE
    }

    private fun hideDeadline() {
        binding.textCurrentDeadline.visibility = View.INVISIBLE
    }

    private fun createDatePickerDialog() = DatePickerDialog(requireContext()).apply {
        setOnDateSetListener { _, y, m, d ->
            val c = Calendar.getInstance().apply { set(y, m, d) }
            viewModel.setDeadline(c)
        }
        setOnCancelListener { binding.switchDeadline.isChecked = false }
        show()
    }
}
