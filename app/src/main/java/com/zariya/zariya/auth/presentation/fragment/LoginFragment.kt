package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.zariya.zariya.R
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentLoginBinding
import com.zariya.zariya.utils.AppUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    private fun setUpListeners() {
        uiEventListener()
        binding.btnLogin.setOnClickListener {
            if (binding.btnLogin.text.equals("Verify OTP")) {
                val credential = PhoneAuthProvider.getCredential(
                    storedVerificationId, binding.tilOTP.editText?.text.toString()
                )
                Log.d("LoginFragment", "login OTP entered")
                signInWithPhoneAuthCredential(credential)
            } else {
                if (validate()) {
                    initiateOTPLogin()
                }
            }
        }

        binding.btnFacebook.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginToHome())
        }
    }

    private fun initiateOTPLogin() {
        val phoneNumber =
            "${binding.countryCodePicker.selectedCountryCodeWithPlus}${binding.tilPhone.editText?.text.toString()}}"
        activity?.let {
            PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions.newBuilder()
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callbacks)
                    .build()
            )
        }
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("LoginFragment", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("LoginFragment", "onVerificationFailed", e)
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    // Invalid request
                }

                is FirebaseTooManyRequestsException -> {
                    // The SMS quota for the project has been exceeded
                }

                is FirebaseAuthMissingActivityForRecaptchaException -> {
                    // reCAPTCHA verification attempted with null Activity
                }
            }
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d("LoginFragment", "onCodeSent:$verificationId")
            storedVerificationId = verificationId
            resendToken = token
            showOTPUi(true)
        }
    }

    private fun showOTPUi(show: Boolean) {
        binding.apply {
            AppUtil.setVisibility(show, tilOTP)
            AppUtil.setVisibility(show.not(), btnGoogle, btnFacebook, tvOr, viewOrLeft, viewOrRight)
            if (show) {
                btnLogin.text = "Verify OTP"
            } else {
                btnLogin.text = "GET OTP"
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        authViewModel.authenticateWithPhone(credential)
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