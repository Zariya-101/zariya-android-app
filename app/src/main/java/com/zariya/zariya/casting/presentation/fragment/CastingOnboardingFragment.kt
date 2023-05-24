package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.zariya.zariya.casting.presentation.adapter.ViewPagerAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentCastingOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CastingOnboardingFragment : BaseFragment() {

    private lateinit var binding: FragmentCastingOnboardingBinding
    private val castingOnboardingViewModel by viewModels<CastingOnboardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCastingOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter =
            fragmentManager?.let { ViewPagerAdapter(it, lifecycle, castingOnboardingViewModel) }
    }

    private fun setUpListeners() {
        uiEventListener()
        binding.ivBack.setOnClickListener {
            if (binding.viewPager.currentItem > 2) {
                binding.viewPager.currentItem -= 1
            } else {
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.sliderProgress.value = position.toFloat()
            }
        })
        binding.btnNext.setOnClickListener {
            when (binding.viewPager.currentItem) {
                0 -> {
                    if (castingOnboardingViewModel.userType != null) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(context, "Please Select Type of account", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                1 -> {
                    if (castingOnboardingViewModel.actorProfileDetails.age.isEmpty().not()) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(context, "Please Select your Age", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                2 -> {
                    if (castingOnboardingViewModel.actorProfileDetails.complexion.isEmpty().not()) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(
                            context, "Please Select your Complexion type", Toast.LENGTH_LONG
                        ).show()
                    }
                }

                3 -> {
                    if (castingOnboardingViewModel.actorProfileDetails.height.isEmpty().not()) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(
                            context, "Please Select your Height", Toast.LENGTH_LONG
                        ).show()
                    }
                }

                4 -> {
                    binding.viewPager.currentItem += 1
                }

                5 -> {
                    castingOnboardingViewModel.createActorProfile()
                }

                else -> {

                }
            }
        }
    }

    private fun uiEventListener() {
        castingOnboardingViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
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
}