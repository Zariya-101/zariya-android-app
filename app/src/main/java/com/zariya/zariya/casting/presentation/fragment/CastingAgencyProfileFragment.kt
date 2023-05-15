package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
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

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.about)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.casting_calls)))

    }
}