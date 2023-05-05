package com.zariya.zariya.workshop.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.zariya.zariya.databinding.FragmentWorkshopsBinding
import com.zariya.zariya.workshop.data.model.FilterWorkshops
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.presentation.adapter.FilterWorkshopsAdapter
import com.zariya.zariya.workshop.presentation.adapter.WorkshopsAdapter

class WorkshopsFragment : Fragment() {

    private lateinit var binding: FragmentWorkshopsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        binding.rvFilter.apply {
            adapter = FilterWorkshopsAdapter(
                listOf(
                    FilterWorkshops(filter = "All", isSelected = true),
                    FilterWorkshops(filter = "Online"),
                    FilterWorkshops(filter = "Offline"),
                    FilterWorkshops(filter = "Productions")
                )
            )
        }

        binding.rvWorkshops.apply {
            adapter = WorkshopsAdapter(
                listOf(
                    Workshop(title = "An Actor's Mosaic", "₹ 100"),
                    Workshop(title = "An Actor's Mosaic", "₹ 100"),
                    Workshop(title = "An Actor's Mosaic", "₹ 100"),
                    Workshop(title = "An Actor's Mosaic", "₹ 100")
                ),
                onCardClicked = {
                    Navigation.findNavController(binding.root)
                        .navigate(WorkshopsFragmentDirections.actionWorkshopDetail())
                }
            )
        }
    }

    private fun setUpListeners() {
        binding.ivSearch.setOnClickListener {
            it.isVisible = false
            binding.etSearch.isVisible = true
        }

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }
}