package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.auth.presentation.viewmodel.AuthViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment() {

    private lateinit var binding: FragmentSplashBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoImg.animate().alpha(1F).duration = 2000
        proceed()
    }

    private fun proceed() = launch {
        delay(2000)
        if(authViewModel.isUserLoggedIn()) {
            Navigation.findNavController(binding.root)
                .navigate(SplashFragmentDirections.actionSplashToHome())
        } else {
            Navigation.findNavController(binding.root)
                .navigate(SplashFragmentDirections.actionSplashToWelcome())
        }
    }
}