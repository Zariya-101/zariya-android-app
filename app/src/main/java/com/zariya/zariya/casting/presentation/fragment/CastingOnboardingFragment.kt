package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.adapter.ViewPagerAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentCastingOnboardingBinding

class CastingOnboardingFragment : BaseFragment() {

    private lateinit var binding: FragmentCastingOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCastingOnboardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        val layouts = intArrayOf(
            R.layout.layout_select_user_type,
            R.layout.layout_select_user_type,
            R.layout.layout_select_user_type
        )
        binding.viewPager.adapter = ViewPagerAdapter(layouts)
    }
}