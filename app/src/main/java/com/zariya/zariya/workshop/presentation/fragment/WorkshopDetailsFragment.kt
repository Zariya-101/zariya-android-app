package com.zariya.zariya.workshop.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.zariya.zariya.databinding.FragmentWorkshopDetailsBinding

class WorkshopDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWorkshopDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.setActionBar(binding.workshopToolbar)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setDisplayShowHomeEnabled(true)
        activity?.actionBar?.title = "An Actor's Mosiac"
        binding.workshopToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()


            }

        binding.btnAddToCart.setOnClickListener{
            it.findNavController().navigate(WorkshopDetailsFragmentDirections.actionWorkshopDetailtoCheckout())
        }
    }
}