package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.presentation.adapter.ActorImagesAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentActorProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActorProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentActorProfileBinding
    private val castingOnboardingViewModel by viewModels<CastingOnboardingViewModel>()
    private val args: ActorProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActorProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        args.actor?.let {
            populateActorDetails(it)
            binding.fabFeeds.isVisible = false
        } ?: run {
            getActorData()
            binding.fabFeeds.isVisible = true
        }
    }

    private fun setUpListeners() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun getActorData() {
        castingOnboardingViewModel.getActorProfile()
        castingOnboardingViewModel.getActorProfileDetails.observe(viewLifecycleOwner) {
            populateActorDetails(it)
        }
    }

    private fun populateActorDetails(actorProfile: ActorProfile) {
        binding.viewPager.adapter = ActorImagesAdapter(actorProfile.imageList)
        binding.actor = actorProfile
    }
}