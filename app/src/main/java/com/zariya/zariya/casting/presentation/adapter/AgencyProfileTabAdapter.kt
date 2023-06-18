package com.zariya.zariya.casting.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.presentation.fragment.AboutAgencyFragment
import com.zariya.zariya.casting.presentation.fragment.AgencyCastingCallsFragment
import com.zariya.zariya.utils.AGENCY

class AgencyProfileTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val agency: Agency
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val bundle = Bundle()
        bundle.putParcelable(AGENCY, agency)
        val fragment = when (position) {
            1 -> AgencyCastingCallsFragment()
            else -> AboutAgencyFragment()
        }
        fragment.arguments = bundle
        return fragment
    }
}