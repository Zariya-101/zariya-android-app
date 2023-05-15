package com.zariya.zariya.casting.presentation.fragment.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.zariya.zariya.casting.presentation.adapter.ComplexionAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSelectComplexionBinding

class SelectComplexionFragment : BaseFragment() {

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
                    "Extremely Fair",
                    "Fair",
                    "Pale",
                    "Medium",
                    "Olive",
                    "Naturally Brown",
                    "Dark Brown"
                )
            )
        }
    }
}