package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectAgeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAgeFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentSelectAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectAgeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.setLabelFormatter { value ->

            val age = "${value.toInt()} years"

            binding.tilAge.editText?.setText(age)
            castingOnboardingViewModel.updateActorProfileDetails(
                castingOnboardingViewModel.actorProfileDetails.copy(age = value.toInt().toString())
            )

            return@setLabelFormatter age
        }
    }
}