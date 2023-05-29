package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentVolunteerProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VolunteerProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentVolunteerProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVolunteerProfileBinding.inflate(layoutInflater)
        return binding.root
    }
}