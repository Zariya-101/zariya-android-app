package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentVolunteerOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VolunteerOnboardingFragment : BaseFragment() {

    private lateinit var binding: FragmentVolunteerOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVolunteerOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }


}