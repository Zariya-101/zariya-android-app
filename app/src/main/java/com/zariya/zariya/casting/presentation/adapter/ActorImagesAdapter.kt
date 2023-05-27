package com.zariya.zariya.casting.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemActorProfileImageBinding

class ActorImagesAdapter(private val list: List<String>) :
    RecyclerView.Adapter<ActorImagesAdapter.ActorImagesViewHolder>() {

    inner class ActorImagesViewHolder(val binding: ItemActorProfileImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ActorImagesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_actor_profile_image,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ActorImagesViewHolder, position: Int) {
        Glide.with(holder.binding.iv)
            .load(list[position])
            .into(holder.binding.iv)
    }
}