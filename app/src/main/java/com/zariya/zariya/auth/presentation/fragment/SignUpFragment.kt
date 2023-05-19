package com.zariya.zariya.auth.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.zariya.zariya.R
import com.zariya.zariya.auth.data.model.Customers
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentSignUpBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var auth: FirebaseAuth
    private var isOtpTriggered = false

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
//            if (validate()) {
//                val customer = Customers().apply {
//                    name = binding.tilName.editText?.text.toString()
//                    phone = binding.tilPhone.editText?.text.toString()
//                    dob = binding.tilDOB.editText?.text.toString()
//                    countryCode = binding.countryCodePicker.selectedCountryCode
//                }
//                authViewModel.register(customer)
//            }

            if (isOtpTriggered) {
                val credential = PhoneAuthProvider.getCredential(
                    storedVerificationId,
                    binding.tilName.editText?.text.toString()
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                val options = activity?.let { it1 ->
                    PhoneAuthOptions.newBuilder()
                        .setPhoneNumber("+91${binding.tilPhone.editText?.text.toString()}")
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(it1)
                        .setCallbacks(callbacks)
                        .build()
                }
                if (options != null) {
                    PhoneAuthProvider.verifyPhoneNumber(options)
                }
                isOtpTriggered = true
            }
        }

        binding.btnFacebook.setOnClickListener {
            it.findNavController().navigate(SignUpFragmentDirections.actionSignUpToHome())
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("SignUpFragment", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
// This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("SignUpFragment", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.e("SignUpFragment", e.localizedMessage)
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.e("SignUpFragment", e.localizedMessage)
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
                Log.e("SignUpFragment", e.localizedMessage)
            } else {
                Log.e("SignUpFragment", e.localizedMessage)
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("SignUpFragment", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        activity?.let {
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignUpFragment", "signInWithCredential:success")

                        val user = task.result?.user
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("SignUpFragment", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                        // Update UI
                    }
                }
        }
    }


    private fun uiEventListener() {
        authViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    // Handle Loading
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context,
                        uiEvent.message ?: "Something went wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(
                            it
                        )
                    }
                }
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