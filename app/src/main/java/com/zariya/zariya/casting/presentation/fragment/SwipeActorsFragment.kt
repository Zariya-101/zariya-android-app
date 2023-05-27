package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.casting.presentation.adapter.SwipeActorsAdapter
import com.zariya.zariya.casting.presentation.viewmodel.CastingAgencyViewModel
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentSwipeActorsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeActorsFragment : BaseFragment(), CardStackListener {

    private lateinit var binding: FragmentSwipeActorsBinding
    private val castingAgencyViewModel by viewModels<CastingAgencyViewModel>()
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
        cardStackLayoutManager = CardStackLayoutManager(context, this)
        cardStackLayoutManager.setStackFrom(StackFrom.Top)
        binding.cardStack.layoutManager = cardStackLayoutManager

        getActors()

        binding.cvInfo.setOnClickListener {
            viewActorProfile(cardStackLayoutManager.topPosition)
        }
    }

    private fun getActors() {
        castingAgencyViewModel.getActors()
        castingAgencyViewModel.actorsLiveData.observe(viewLifecycleOwner) {
            actors = it
            populateActors(it)
        }
    }

    private fun populateActors(actors: List<ActorProfile?>) {
        binding.cardStack.adapter = SwipeActorsAdapter(actors)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        Log.v("SwipeActorsFragment", cardStackLayoutManager.topPosition.toString())
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
}