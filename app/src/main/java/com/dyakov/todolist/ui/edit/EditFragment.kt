package com.dyakov.todolist.ui.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dyakov.todolist.Priority
import com.dyakov.todolist.R
import com.dyakov.todolist.collectOnLifecycle
import com.dyakov.todolist.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


/**
 * A screen which can edit task options
 */
@AndroidEntryPoint
class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setBasicState(args.task)
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
        initDeleteButton()
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
            viewModel.updateTask()
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

    private fun getSpinnerListener() = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.updatePriority(Priority.values()[position])
        }
    }

    private fun setFragmentState(state: EditUiState) = with(binding) {
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

    private fun showDeadline(deadline: Date) = with(binding) {
        val c = Calendar.getInstance().apply { time = deadline }
        switchDeadline.isChecked = true
        textCurrentDeadline.text = String.format(
            resources.getString(
                R.string.date_pattern
            ),
            c.get(Calendar.DAY_OF_MONTH),
            resources.getStringArray(R.array.month_array)[c.get(Calendar.MONTH)],
            c.get(Calendar.YEAR)
        )
        textCurrentDeadline.visibility = View.VISIBLE
    }

    private fun hideDeadline() {
        binding.textCurrentDeadline.visibility = View.INVISIBLE
    }

    private fun initDeleteButton() = with(binding) {
        binding.textDelete.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.textDelete.compoundDrawables[0].setTint(ContextCompat.getColor(requireContext(),
            R.color.red
        ))
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
