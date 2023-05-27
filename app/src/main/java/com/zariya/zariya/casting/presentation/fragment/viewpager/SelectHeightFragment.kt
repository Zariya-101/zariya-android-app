package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectHeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectHeightFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentSelectHeightBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectHeightBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.setLabelFormatter { value ->
            val feet = value.toInt() / 12
            val inch = value.toInt() % 12

            val height = "$feet' $inch\""

            binding.tilHeight.editText?.setText(height)
            castingOnboardingViewModel.updateActorProfileDetails(
                castingOnboardingViewModel.actorProfileDetails.copy(height = height)
            )

            return@setLabelFormatter height
        }
    }
}