package com.zariya.zariya.casting.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zariya.zariya.casting.presentation.fragment.viewpager.AddAuditionVideoFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.AddImagesFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectAgeFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectComplexionFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectHeightFragment
import com.zariya.zariya.casting.presentation.fragment.viewpager.SelectUserTypeFragment
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val castingOnboardingViewModel: CastingOnboardingViewModel
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            1 -> SelectAgeFragment(castingOnboardingViewModel)
            2 -> SelectComplexionFragment(castingOnboardingViewModel)
            3 -> SelectHeightFragment(castingOnboardingViewModel)
            4 -> AddImagesFragment(castingOnboardingViewModel)
            5 -> AddAuditionVideoFragment(castingOnboardingViewModel)
            else -> SelectUserTypeFragment(castingOnboardingViewModel)
        }
        return fragment
    }
}