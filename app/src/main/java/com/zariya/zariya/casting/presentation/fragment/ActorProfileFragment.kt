package com.zariya.zariya.casting.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.zariya.zariya.R
import com.zariya.zariya.casting.presentation.adapter.ActorImagesAdapter
import com.zariya.zariya.core.ui.BaseFragment
import com.zariya.zariya.databinding.FragmentActorProfileBinding

class ActorProfileFragment : BaseFragment() {

    private lateinit var binding: FragmentActorProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActorProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { ctx ->
            binding.viewPager.adapter =
                ActorImagesAdapter(
                    listOf(
                        ContextCompat.getDrawable(ctx, R.drawable.ic_sachin),
                        ContextCompat.getDrawable(ctx, R.drawable.ic_virat),
                        ContextCompat.getDrawable(ctx, R.drawable.ic_workshop_temp)
                    )
                )
        }
    }
}