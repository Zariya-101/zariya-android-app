package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectRoleBinding

class SelectRoleFragment : BaseFragment() {

    private lateinit var binding: FragmentSelectRoleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectRoleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.btnNext.setOnClickListener {
            if (binding.rbActor.isChecked) {
                Navigation.findNavController(binding.root)
                    .navigate(SelectRoleFragmentDirections.actionSelectRoleToActorDetails())
            } else if (binding.rbAgency.isChecked) {
                Navigation.findNavController(binding.root)
                    .navigate(SelectRoleFragmentDirections.actionSelectRoleToAgencyDetails())
            }
        }
    }
}