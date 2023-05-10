package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectAgeBinding

class SelectAgeFragment : BaseFragment() {

    private lateinit var binding: FragmentSelectAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectAgeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.slider.setLabelFormatter { value ->

            val age = "${value.toInt()} years"

            binding.tilAge.editText?.setText(age)

            return@setLabelFormatter age
        }
    }
}