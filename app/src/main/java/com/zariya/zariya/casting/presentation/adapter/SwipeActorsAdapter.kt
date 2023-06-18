package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zariya.zariya.R
import com.zariya.zariya.casting.data.model.ActorProfile
import com.zariya.zariya.databinding.ItemSwipeActorBinding

class SwipeActorsAdapter(private val list: List<ActorProfile?>) :
    RecyclerView.Adapter<SwipeActorsAdapter.SwipeActorsViewHolder>() {

    inner class SwipeActorsViewHolder(val binding: ItemSwipeActorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SwipeActorsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_swipe_actor,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SwipeActorsViewHolder, position: Int) {
        Glide.with(holder.binding.iv)
            .load(list[position]?.imageList?.get(0))
            .into(holder.binding.iv)
    }
}