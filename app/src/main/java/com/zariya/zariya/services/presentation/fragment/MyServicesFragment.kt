package com.zariya.zariya.services.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.databinding.FragmentMyServicesBinding

class MyServicesFragment : Fragment() {

    private lateinit var binding: FragmentMyServicesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyServicesBinding.inflate(layoutInflater)
        return binding.root
    }
}