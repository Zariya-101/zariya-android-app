package com.zariya.zariya.payment.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentMyCartBinding

class MyCartFragment :  BaseFragment() {


    private lateinit var binding: FragmentMyCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setActionBar(binding.toolbar)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setDisplayShowHomeEnabled(true)
        activity?.actionBar?.title = "My Cart"
        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

    }
}