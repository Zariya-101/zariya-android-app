package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.presentation.adapter.AgencyProfileTabAdapter
import com.zariya.zariya.casting.presentation.viewmodel.AgencyProfileViewModel
import com.zariya.zariya.common.data.model.Stats
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentAgencyProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgencyProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentAgencyProfileBinding
    private val viewModel by viewModels<AgencyProfileViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgencyProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiEventListener()
        getAgencyDetails()
    }

    private fun getAgencyDetails() {
        showProgress(binding.root)
        viewModel.getAgencyProfile()
        viewModel.getAgencyProfileDetails.observe(viewLifecycleOwner) { agency ->
            binding.agency = agency
            binding.layoutActors.stats = Stats(key = "Actors", value = "25")
            binding.layoutVolunteer.stats = Stats(key = "Volunteers", value = "40")
            binding.layoutRatings.stats = Stats(key = "Ratings", value = "4.5")
            setUpViewPager(agency)
        }
    }

    private fun setUpViewPager(agency: Agency) {
        val adapter = fragmentManager?.let { AgencyProfileTabAdapter(it, lifecycle, agency) }
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.agency_profile_tab)[position]
        }.attach()
    }

    private fun uiEventListener() {
        viewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
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

                is UIEvents.RefreshUi -> {}
            }
        }
    }
}