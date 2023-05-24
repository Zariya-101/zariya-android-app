package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zariya.zariya.casting.presentation.adapter.ComplexionAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingOnboardingViewModel
import com.zariya.zariya.common.data.model.SelectableListElement
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectComplexionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectComplexionFragment(private val castingOnboardingViewModel: CastingOnboardingViewModel) :
    BaseFragment() {

    private lateinit var binding: FragmentSelectComplexionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectComplexionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvComplexion.apply {
            adapter = ComplexionAdapter(
                listOf(
                    SelectableListElement(text = "Extremely Fair"),
                    SelectableListElement(text = "Fair"),
                    SelectableListElement(text = "Pale"),
                    SelectableListElement(text = "Medium"),
                    SelectableListElement(text = "Olive"),
                    SelectableListElement(text = "Naturally Brown"),
                    SelectableListElement(text = "Dark Brown")
                ),
                onCardClicked = {
                    castingOnboardingViewModel.updateActorProfileDetails(
                        castingOnboardingViewModel.actorProfileDetails.copy(complexion = it)
                    )
                }
            )
        }
    }
}