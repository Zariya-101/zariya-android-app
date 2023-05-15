package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
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
        binding.btnNext.setOnClickListener {
//            if (binding.viewPager.currentItem < 3) {
//                binding.viewPager.currentItem += 1
//            }
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
                    if (castingOnboardingViewModel.userAge != null) {
                        binding.viewPager.currentItem += 1
                    } else {
                        Toast.makeText(context, "Please Select your Age", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                else -> {

                }
            }
        }
    }
}