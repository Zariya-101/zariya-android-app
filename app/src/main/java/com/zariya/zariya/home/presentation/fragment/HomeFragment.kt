package com.zariya.zariya.home.presentation.fragment

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.zariya.zariya.R
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentHomeBinding
import com.zariya.zariya.home.data.model.Feed
import com.zariya.zariya.home.presentation.adapter.FeedAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
    }

    private fun initView() {
        binding.layoutActing.service = "Acting \nWorkshops"
        binding.layoutCasting.service = "Casting \nRequirements"
        binding.layoutWellness.service = "Wellness \nWorkshops"
        binding.rvFeed.apply {
            adapter = FeedAdapter(
                listOf(
                    Feed(
                        userName = "Vivek_Sharma",
                        location = "Haridwar",
                        likes = 1200,
                        comments = 400,
                        feedImage = R.drawable.ic_virat,
                        profilePic = R.drawable.ic_virat
                    ),
                    Feed(
                        userName = "Suraj_Prakash",
                        location = "Delhi",
                        likes = 1300,
                        comments = 500,
                        feedImage = R.drawable.ic_sachin,
                        profilePic = R.drawable.ic_sachin
                    ),
                    Feed(
                        userName = "Vivek_Sharma",
                        location = "Haridwar",
                        likes = 1200,
                        comments = 400,
                        feedImage = R.drawable.ic_virat,
                        profilePic = R.drawable.ic_virat
                    ),
                    Feed(
                        userName = "Suraj_Prakash",
                        location = "Delhi",
                        likes = 1300,
                        comments = 500,
                        feedImage = R.drawable.ic_sachin,
                        profilePic = R.drawable.ic_sachin
                    )
                )
            )
        }
    }

    private fun setUpListeners() {
        binding.ivSearch.setOnClickListener {
            it.isVisible = false
            binding.etSearch.isVisible = true
        }

        binding.layoutActing.cv.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeToWorkshops())
        }

        binding.layoutCasting.cv.setOnClickListener {
            it.findNavController().navigate(HomeFragmentDirections.actionHomeToCasting())
        }
    }
}