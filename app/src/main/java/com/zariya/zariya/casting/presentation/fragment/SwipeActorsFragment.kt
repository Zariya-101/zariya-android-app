package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import com.yuyakaido.android.cardstackview.SwipeableMethod
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.presentation.adapter.SwipeActorsAdapter
import com.zariya.zariya.casting.presentation.viewmodel.SwipeActorsViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSwipeActorsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeActorsFragment : BaseFragment(), CardStackListener {

    private lateinit var binding: FragmentSwipeActorsBinding
    private val swipeActorsViewModel by viewModels<SwipeActorsViewModel>()
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private var actors: List<ActorProfile?>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwipeActorsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setUpListeners()
        getActors()
    }

    private fun initView() {
        cardStackLayoutManager = CardStackLayoutManager(context, this)
        cardStackLayoutManager.setStackFrom(StackFrom.Top)
        cardStackLayoutManager.setDirections(Direction.FREEDOM)
        cardStackLayoutManager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        binding.cardStack.layoutManager = cardStackLayoutManager
        showData(false)
    }

    private fun setUpListeners() {
        binding.cvInfo.setOnClickListener {
            viewActorProfile(cardStackLayoutManager.topPosition)
        }

        binding.cvLike.setOnClickListener {
            val likeSetting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()

            cardStackLayoutManager.setSwipeAnimationSetting(likeSetting)
            binding.cardStack.swipe()
        }

        binding.cvDisLike.setOnClickListener {
            val disLikeSetting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()

            cardStackLayoutManager.setSwipeAnimationSetting(disLikeSetting)
            binding.cardStack.swipe()
        }
    }

    private fun getActors() {
        swipeActorsViewModel.getActors()
        swipeActorsViewModel.actorsLiveData.observe(viewLifecycleOwner) {
            actors = it
            populateActors(it)
        }
    }

    private fun populateActors(actors: List<ActorProfile?>) {
        binding.cardStack.adapter = SwipeActorsAdapter(actors)
        showData(true)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> {
            }

            Direction.Left -> {

            }

            Direction.Top -> {
                viewActorProfile(cardStackLayoutManager.topPosition - 1)
            }

            Direction.Bottom -> {

            }

            else -> {}
        }
    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    private fun viewActorProfile(position: Int) {
        actors?.let {
            val actor = it[position]
            actor?.let {
                val action = SwipeActorsFragmentDirections.actionActorProfile()
                action.actor = actor
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    private fun showData(show: Boolean) {
        binding.shimmerLayout.apply {
            if (show) stopShimmer() else startShimmer()
            isVisible = !show
        }
        binding.cardStack.isVisible = show
        binding.cvLike.isVisible = show
        binding.cvDisLike.isVisible = show
        binding.ivInfo.isVisible = show
    }
}