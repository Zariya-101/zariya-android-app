package com.zariya.zariya.workshop.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.core.ui.UIEvents
import com.zariya.zariya.databinding.FragmentWorkshopsBinding
import com.zariya.zariya.workshop.data.model.FilterWorkshops
import com.zariya.zariya.workshop.data.model.Workshop
import com.zariya.zariya.workshop.data.model.WorkshopReview
import com.zariya.zariya.workshop.presentation.adapter.FilterWorkshopsAdapter
import com.zariya.zariya.workshop.presentation.adapter.RecentReviewsAdapter
import com.zariya.zariya.workshop.presentation.adapter.WorkshopsAdapter
import com.zariya.zariya.workshop.presentation.viewmodel.WorkshopListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkshopsFragment : BaseFragment() {

    private lateinit var binding: FragmentWorkshopsBinding
    private val viewModel by viewModels<WorkshopListViewModel>()

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
        uiEventListener()
        setUpListeners()
        getWorkshops()
        workshopListObserver()
    }

    private fun getWorkshops(type: String? = null) {
        viewModel.getWorkshopsList(type)
    }

    private fun workshopListObserver() {
        viewModel.workshopsLiveData.observe(viewLifecycleOwner) {
            binding.rvWorkshops.apply {
                adapter = WorkshopsAdapter(it.mapNotNull { it }, onCardClicked = {
                    val action = WorkshopsFragmentDirections.actionWorkshopDetail()
                    action.workshop = it
                    Navigation.findNavController(binding.root)
                        .navigate(action)
                })
            }
        }
    }

    private fun initView() {
        binding.rvFilter.apply {
            adapter = FilterWorkshopsAdapter(
                listOf(
                    FilterWorkshops(filter = "All", isSelected = true),
                    FilterWorkshops(filter = "Online"),
                    FilterWorkshops(filter = "Offline"),
                    FilterWorkshops(filter = "Productions")
                ),
                onItemClicked = {
                    if (it.filter == "All") getWorkshops()
                    else getWorkshops(it.filter)
                }
            )
        }

        binding.rvRecentReviews.apply {
            adapter = RecentReviewsAdapter(
                listOf(
                    WorkshopReview(
                        reviewerName = "Vivek Sharma",
                        reviewerImage = "",
                        workshopName = "An Actor's Mosaic",
                        rating = 4.0f,
                        review = "Workshop was super awesome. I liked the workshop a lot"
                    ),
                    WorkshopReview(
                        reviewerName = "Vivek Sharma",
                        reviewerImage = "",
                        workshopName = "An Actor's Mosaic",
                        rating = 4.0f,
                        review = "Workshop was super awesome. I liked the workshop a lot"
                    ),
                    WorkshopReview(
                        reviewerName = "Vivek Sharma",
                        reviewerImage = "",
                        workshopName = "An Actor's Mosaic",
                        rating = 4.0f,
                        review = "Workshop was super awesome. I liked the workshop a lot"
                    ),
                    WorkshopReview(
                        reviewerName = "Vivek Sharma",
                        reviewerImage = "",
                        workshopName = "An Actor's Mosaic",
                        rating = 4.0f,
                        review = "Workshop was super awesome. I liked the workshop a lot"
                    ),
                    WorkshopReview(
                        reviewerName = "Vivek Sharma",
                        reviewerImage = "",
                        workshopName = "An Actor's Mosaic",
                        rating = 4.0f,
                        review = "Workshop was super awesome. I liked the workshop a lot"
                    )
                )
            )
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

    private fun setUpListeners() {
        binding.ivSearch.setOnClickListener {
            if (binding.cvFilter.isVisible) {
                binding.cvFilter.isVisible = false
                binding.tvTitle.text = "Available Workshops"
            } else {
                binding.cvFilter.isVisible = true
                binding.tvTitle.text = "Filter Workshops"
            }
        }

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }
}