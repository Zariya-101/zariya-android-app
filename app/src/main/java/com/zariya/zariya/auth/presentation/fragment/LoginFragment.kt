package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.zariya.zariya.R
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()

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
            if (validate()) {
                authViewModel.login(binding.tilPhone.editText?.text.toString(), null)
            }
        }

        binding.btnFacebook.setOnClickListener {
            it.findNavController().navigate(LoginFragmentDirections.actionLoginToHome())
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