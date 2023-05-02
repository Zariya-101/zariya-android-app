package com.zariya.zariya.merchandise.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.databinding.FragmentMerchandiseBinding

class MerchandiseFragment : Fragment() {

    private lateinit var binding: FragmentMerchandiseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMerchandiseBinding.inflate(layoutInflater)
        return binding.root
    }
}