package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentAgencyDetailsFormBinding

class AgencyDetailsFormFragment : BaseFragment() {

    private lateinit var binding: FragmentAgencyDetailsFormBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgencyDetailsFormBinding.inflate(layoutInflater)
        return binding.root
    }
}