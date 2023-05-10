package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private val introSliders: List<IntroSlide>) :
    RecyclerView.Adapter<ViewPagerAdapter.IntroSliderViewHolder>() {

    class ViewPagerViewHolder(binding: ItemIntroSliderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return IntroSliderViewHolder(
            ItemIntroSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvSliderHeading).text = introSliders[position].title
            findViewById<TextView>(R.id.tvSliderText).text = introSliders[position].description
            findViewById<ImageView>(R.id.ivSlider).setImageResource(introSliders[position].icon)
        }
    }

    override fun getItemCount() = introSliders.size
}