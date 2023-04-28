package com.zariya.zariya.auth.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.core.view.isEmpty
import androidx.navigation.findNavController
import com.zariya.zariya.R
import com.zariya.zariya.databinding.FragmentSignUpBinding
import java.util.Calendar

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        binding.tilDOB.editText?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val cal = Calendar.getInstance()
                activity?.let {
                    val datePickerDialog = DatePickerDialog(
                        it, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                            val selectedDate =
                                dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                            binding.tilDOB.editText?.setText(selectedDate)
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )

                    val maxDate = Calendar.getInstance()
                    maxDate.add(Calendar.YEAR, -16)

                    datePickerDialog.datePicker.maxDate = maxDate.timeInMillis
                    datePickerDialog.show()
                }
            }

            return@setOnTouchListener false
        }

        binding.tvTerms.setOnClickListener {
            it.findNavController()
                .navigate(SignUpFragmentDirections.actionSignUpToWebView("temp_link"))
        }

        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            binding.btnSignUp.isEnabled = isChecked
        }

        binding.btnSignUp.setOnClickListener {
            if (validate()) {

            }
        }
    }

    private fun validate(): Boolean {
        if (binding.tilName.editText?.text?.isEmpty() == true) {
            binding.tilName.error = getString(R.string.validation_empty_name)
            return false
        }
        binding.tilName.error = ""

        if (binding.tilDOB.editText?.text?.isEmpty() == true) {
            binding.tilDOB.error = getString(R.string.validation_empty_dob)
            return false
        }
        binding.tilDOB.error = ""

        if (binding.countryCodePicker.selectedCountryCode.isNullOrEmpty()) {
            binding.tilPhone.error = getString(R.string.validation_select_country)
            return false
        }
        binding.tilPhone.error = ""

        if (binding.tilPhone.editText?.text?.isEmpty() == true) {
            binding.tilPhone.error = getString(R.string.validation_empty_phone)
            return false
        }
        binding.tilPhone.error = ""

        if (binding.tilPhone.editText?.text?.toString()?.length != 10) {
            binding.tilPhone.error = getString(R.string.validation_invalid_phone)
            return false
        }
        binding.tilPhone.error = ""

        return true
    }
}