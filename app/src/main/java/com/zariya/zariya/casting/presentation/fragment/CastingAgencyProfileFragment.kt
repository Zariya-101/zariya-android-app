package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.adapter.AgencyProfileTabAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentCastingAgencyProfileBinding

class CastingAgencyProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentCastingAgencyProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCastingAgencyProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
    }

    private fun setUpViewPager() {
        val adapter = fragmentManager?.let { AgencyProfileTabAdapter(it, lifecycle) }
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.agency_profile_tab)[position]
        }.attach()
    }
}