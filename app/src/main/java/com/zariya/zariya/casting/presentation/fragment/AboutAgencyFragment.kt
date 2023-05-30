package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.Agency
import com.zariya.zariya.casting.presentation.adapter.SpecialityAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentAboutAgencyBinding
import com.zariya.zariya.utils.AGENCY

class AboutAgencyFragment : BaseFragment() {

    private lateinit var binding: FragmentAboutAgencyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutAgencyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (it.containsKey(AGENCY) && (it.getParcelable(AGENCY) as Agency?) != null) {
                populateData((it.getParcelable(AGENCY) as Agency?)!!)
            }
        }
    }

    private fun populateData(agency: Agency) {
        binding.agency = agency
        binding.rvSpeciality.apply {
            adapter = SpecialityAdapter(agency.speciality)
        }
    }
}