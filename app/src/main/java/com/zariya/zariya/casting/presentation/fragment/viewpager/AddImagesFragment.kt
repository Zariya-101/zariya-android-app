package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentAddImagesBinding

class AddImagesFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentAddImagesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddImagesBinding.inflate(layoutInflater)
        return binding.root
    }
}