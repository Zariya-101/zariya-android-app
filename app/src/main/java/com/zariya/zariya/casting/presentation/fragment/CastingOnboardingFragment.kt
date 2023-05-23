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
import com.zariya.zariya.databinding.FragmentCastingOnboardingBinding

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
                    if (castingOnboardingViewModel.userAge != null) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(context, "Please Select your Age", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                2 -> {
                    if (castingOnboardingViewModel.userComplexion != null) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(
                            context, "Please Select your Complexion type", Toast.LENGTH_LONG
                        ).show()
                    }
                }

                3 -> {
                    if (castingOnboardingViewModel.userHeight != null) {
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
                    Navigation.findNavController(binding.root)
                        .navigate(CastingOnboardingFragmentDirections.actionActorProfile())
                }

                else -> {

                }
            }
        }
    }
}