package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.viewmodel.CreateCastingCallViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentCreateCastingCallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCastingCallFragment : BaseFragment() {

    private lateinit var binding: FragmentCreateCastingCallBinding
    private val viewModel by viewModels<CreateCastingCallViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCastingCallBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiEventListener()

        val genderArray = resources.getStringArray(R.array.gender_array)
        binding.tvGender.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, genderArray)
        })

        val ageArray = resources.getStringArray(R.array.age_range_array)
        binding.tvAge.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, ageArray)
        })

        val complexionArray = resources.getStringArray(R.array.actor_complexion)
        binding.tvComplexion.setAdapter(context?.let {
            ArrayAdapter(it, R.layout.item_dropdown, complexionArray)
        })

        getVolunteersList()
    }

    private fun getVolunteersList() {
        viewModel.getVolunteersForMyAgency()
        viewModel.volunteersLiveData.observe(viewLifecycleOwner) { volunteers ->
            binding.tvAssignTo.setAdapter(context?.let {
                ArrayAdapter(it, R.layout.item_dropdown, volunteers.map { it?.name })
            })
        }
    }

    private fun uiEventListener() {
        viewModel.uiEvents.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvents.Loading -> {
                    if (uiEvent.loading) showProgress(binding.root) else hideProgress()
                }

                is UIEvents.ShowError -> {
                    Toast.makeText(
                        context, uiEvent.message ?: "Something went wrong", Toast.LENGTH_LONG
                    ).show()
                }

                is UIEvents.Navigate -> {
                    uiEvent.navDirections?.let {
                        Navigation.findNavController(binding.root).navigate(it)
                    }
                }

                is UIEvents.RefreshUi -> {}
            }
        }
    }
}