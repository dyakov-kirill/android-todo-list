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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dyakov.todolist.*
import com.dyakov.todolist.databinding.FragmentEditBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class EditFragment : Fragment() {
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: EditViewModel by viewModels()

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setBasicState(args.task)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    setFragmentState(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        initSpinner()
        setListeners()
        binding.textDelete.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        binding.textDelete.compoundDrawables[0].setTint(ContextCompat.getColor(requireContext(),
            R.color.red
        ))
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
            if (switchDeadline.isChecked) {
                val dialog = DatePickerDialog(requireContext())
                dialog.setOnDateSetListener { _, y, m, d ->
                    val c = Calendar.getInstance()
                    c.set(y, m, d)
                    viewModel.setDeadline(c)
                }
                dialog.setOnCancelListener { binding.switchDeadline.isChecked = false }
                dialog.show()
            } else {
                viewModel.removeDeadline()
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

    private fun setFragmentState(state: EditUiState) {
        with(binding) {
            if (state.description != editTask.text.toString()) {
                editTask.setText(state.description)
                editTask.setSelection(state.description.length - 1)
            }
            prioritySpinner.setSelection(state.priority.priority)
            if (state.isDeadlineSet && state.deadline != null) {
                showDeadline(state.deadline)
            } else {
                hideDeadline()
            }
        }
    }

    private fun showDeadline(deadline: Date) {
        val c = Calendar.getInstance()
        c.time = deadline
        binding.switchDeadline.isChecked = true
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
}