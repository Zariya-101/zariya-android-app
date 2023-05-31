package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentCreateCastingCallBinding

class CreateCastingCallFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateCastingCallBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCastingCallBinding.inflate(layoutInflater)
        return binding.root
    }
}