package com.zariya.zariya.auth.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zariya.zariya.app
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoImg.animate().alpha(1F).setDuration(2000);
        proceed()
    }

    private fun proceed() = launch {
        delay(2000)
        app.currentUser?.let {
            Navigation.findNavController(binding.root)
                .navigate(SplashFragmentDirections.actionSplashToHome())
        } ?: run {
            Navigation.findNavController(binding.root)
                .navigate(SplashFragmentDirections.actionSplashToWelcome())
        }
    }
}