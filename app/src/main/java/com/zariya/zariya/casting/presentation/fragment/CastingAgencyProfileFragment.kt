package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentCastingAgencyProfileBinding

class CastingAgencyProfileFragment : BaseFragment() {

    var tabLayout: TabLayout? = null
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

        tabLayout = binding.tabLayout

        tabLayout!!.addTab(tabLayout!!.newTab().setText("About"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Casting Calls"))

    }
}