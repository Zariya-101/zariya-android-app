package com.zariya.zariya.workshop.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentWorkshopDetailsBinding
import com.zariya.zariya.workshop.presentation.viewmodel.WorkshopDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkshopDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentWorkshopDetailsBinding
    private val args by navArgs<WorkshopDetailsFragmentArgs>()
    private val viewModel by viewModels<WorkshopDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
        uiEventListener()
        checkIfWorkshopIsLiked()
    }

    private fun checkIfWorkshopIsLiked() {
        viewModel.isWorkshopLikedByUser()
    }

    private fun initView() {
        binding.workshop = args.workshop

        activity?.setActionBar(binding.workshopToolbar)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setUpListeners() {
        binding.workshopToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewModel.workshopLikedLiveData.observe(viewLifecycleOwner) {
            binding.fabLike.isVisible = true
            binding.fabLike.isEnabled = true
            it?.let {
                binding.fabLike.setImageResource(R.drawable.ic_liked)
            } ?: run {
                binding.fabLike.setImageResource(R.drawable.ic_disliked)
            }
        }

        binding.btnAddToCart.setOnClickListener {
            it.findNavController()
                .navigate(WorkshopDetailsFragmentDirections.actionWorkshopDetailtoCheckout())
        }

        binding.fabLike.setOnClickListener {
            args.workshop?.workshopId?.let {
                binding.fabLike.isEnabled = false
                viewModel.likeWorkshop(workshopId = it)
            }
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

                is UIEvents.ShowSuccess -> {
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