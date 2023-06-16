package com.zariya.zariya.auth.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.auth.data.model.IntroSlide
import com.zariya.zariya.databinding.ItemIntroSildeBinding

class IntroSlideAdapter(private val list: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSlideAdapter.IntroSlideViewHolder>() {

    inner class IntroSlideViewHolder(val binding: ItemIntroSildeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = IntroSlideViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_intro_silde,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.binding.apply {
            tvDescription.text =
                Html.fromHtml(tvDescription.context.getString(list[position].description))
            ivAnimation.setImageResource(list[position].drawable)
        }
    }
}