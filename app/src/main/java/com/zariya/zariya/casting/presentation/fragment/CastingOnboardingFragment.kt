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
import com.zariya.zariya.utils.ACTOR
import com.zariya.zariya.utils.AGENCY
import com.zariya.zariya.utils.VOLUNTEER
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

        uiEventListener()
        fetchUserFromRemote()
    }

    private fun fetchUserFromRemote() {
        showProgress(binding.root)
        castingOnboardingViewModel.fetchUserDetailsFromRemote()
    }

    private fun initView() {
        setUpViewPager()
    }

    private fun setUpViewPager() {
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
            if (castingOnboardingViewModel.userType.isNullOrEmpty().not()) {
                when (castingOnboardingViewModel.userType) {
                    ACTOR -> nextClickListenerForActor()
                    AGENCY -> {
                        Navigation.findNavController(binding.root)
                            .navigate(CastingOnboardingFragmentDirections.actionAgencyForm())
                    }

                    VOLUNTEER -> {
                        Navigation.findNavController(binding.root)
                            .navigate(CastingOnboardingFragmentDirections.actionVolunteerForm())
                    }
                }
            } else {
                Toast.makeText(context, "Please Select Type of account", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun nextClickListenerForActor() {
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
                val size =
                    if (castingOnboardingViewModel.actorProfileDetails.imageList.contains("")) {
                        castingOnboardingViewModel.actorProfileDetails.imageList.size - 1
                    } else {
                        castingOnboardingViewModel.actorProfileDetails.imageList.size
                    }
                if (size < 2) {
                    Toast.makeText(
                        context, "Please Select atleast 2 images", Toast.LENGTH_LONG
                    ).show()
                } else if (size > 5) {
                    Toast.makeText(
                        context, "You can select maximum of 5 images", Toast.LENGTH_LONG
                    ).show()
                } else {
                    binding.viewPager.currentItem += 1
                }
            }

            5 -> {
                castingOnboardingViewModel.createActorProfile()
            }

            else -> {

            }
        }
    }

    private fun uiEventListener() {
        castingOnboardingViewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    if (uiEvent.loading) showProgress(binding.root) else hideProgress()
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context, uiEvent.message ?: "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(it)
                    }
                }

                is UIEvents.RefreshUi -> {
                    initView()
                    setUpListeners()
                }
            }
        }
    }
}