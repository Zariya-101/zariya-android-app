package com.zariya.zariya.auth.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.messaging.FirebaseMessaging
import com.zariya.zariya.R
import com.zariya.zariya.auth.data.model.User
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val args: SignUpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        initView()
        setUpListeners()
    }

    private fun getArgs() {
        args.user?.let { user ->
            if (user.name.isNullOrEmpty().not()) {
                binding.tilName.editText?.setText(user.name)
            }
            if (user.phone.isNullOrEmpty().not()) {
                binding.tilPhone.editText?.setText(user.phone)
                binding.tilPhone.isEnabled = false
            }
            if (user.email.isNullOrEmpty().not()) {
                binding.tilEmail.editText?.setText(user.email)
                binding.tilEmail.isEnabled = false
            }
        }
    }

    private fun initView() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpListeners() {
        uiEventListener()

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
                showProgress(binding.root)
                signUpUser()
            }
        }
    }

    private fun signUpUser() {
        val countryCode = if (args.user?.countryCode.isNullOrEmpty().not()) {
            args.user?.countryCode
        } else {
            binding.countryCodePicker.selectedCountryCodeWithPlus
        }
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                authViewModel.signUpUser(
                    User(
                        id = args.user?.id,
                        name = binding.tilName.editText?.text.toString(),
                        phone = binding.tilPhone.editText?.text.toString(),
                        email = binding.tilEmail.editText?.text.toString(),
                        dob = binding.tilDOB.editText?.text.toString(),
                        countryCode = countryCode,
                        fcmToken = token
                    )
                )
            }
            .addOnFailureListener {
                hideProgress()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }
    }

    private fun uiEventListener() {
        authViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    if (uiEvent.loading) showProgress(binding.root) else hideProgress()
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context,
                        uiEvent.message ?: "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(it)
                    }
                }

                is UIEvents.RefreshUi -> {}
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

        if (binding.tilEmail.editText?.text?.isEmpty() == true) {
            binding.tilEmail.error = getString(R.string.validation_empty_email)
            return false
        }
        binding.tilEmail.error = ""

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