package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.SelectUserTypeModel
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectUserTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectUserTypeFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentSelectUserTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectUserTypeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { ctx ->
            ContextCompat.getDrawable(ctx, R.drawable.lights)?.let {
                binding.layoutActor.userTypeModel = SelectUserTypeModel(
                    title = "I am an Actor",
                    subTitle = "Find acting gigs posted by Casting Agencies",
                    isSelected = false,
                    icon = it
                )
            }

            ContextCompat.getDrawable(ctx, R.drawable.lights)?.let {
                binding.layoutAgency.userTypeModel = SelectUserTypeModel(
                    title = "I am Casting Agency",
                    subTitle = "Find actors you are looking for",
                    isSelected = false,
                    icon = it
                )
            }
        }

        binding.layoutActor.cv.setOnClickListener {
            binding.layoutActor.userTypeModel =
                binding.layoutActor.userTypeModel?.copy(isSelected = true)
            binding.layoutAgency.userTypeModel =
                binding.layoutAgency.userTypeModel?.copy(isSelected = false)
            castingOnboardingViewModel.updateUserType("Actor")
        }

        binding.layoutAgency.cv.setOnClickListener {
            binding.layoutActor.userTypeModel =
                binding.layoutActor.userTypeModel?.copy(isSelected = false)
            binding.layoutAgency.userTypeModel =
                binding.layoutAgency.userTypeModel?.copy(isSelected = true)
            castingOnboardingViewModel.updateUserType("Agency")
        }
    }
}