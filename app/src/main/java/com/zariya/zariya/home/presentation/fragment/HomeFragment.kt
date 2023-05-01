package com.zariya.zariya.home.presentation.fragment

import android.animation.AnimatorInflater
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
//        binding.rvServices.apply {
//            adapter = ServicesHomeAdapter(
//                listOf(
//                    "Acting Workshops",
//                    "Casting Requirements",
//                    "Wellness Workshops"
//                )
//            )
//        }

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
        animateCards()
    }

    private fun setUpListeners() {
        binding.ivSearch.setOnClickListener {
            it.isVisible = false
            binding.etSearch.isVisible = true
        }
    }

    private fun animateCards() = launch {
        delay(1000)

        AnimatorInflater.loadAnimator(context, R.animator.anim_horizontal_flip).apply {
            setTarget(binding.layoutActing.cv)
            duration = 1000
            start()
        }

        AnimatorInflater.loadAnimator(context, R.animator.anim_vertical_flip_ahead).apply {
            setTarget(binding.layoutCasting.cv)
            duration = 1000
            start()
        }

        AnimatorInflater.loadAnimator(context, R.animator.anim_vertical_flip_behind).apply {
            setTarget(binding.layoutWellness.cv)
            duration = 1000
            start()
        }
    }
}